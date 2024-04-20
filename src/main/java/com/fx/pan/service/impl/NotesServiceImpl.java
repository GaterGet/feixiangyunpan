package com.fx.pan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fx.pan.domain.Document;
import com.fx.pan.domain.FileBean;
import com.fx.pan.mapper.DocumentMapper;
import com.fx.pan.mapper.FileMapper;
import com.fx.pan.service.NotesService;
import com.fx.pan.utils.DateUtil;
import com.fx.pan.utils.FileUtils;
import com.fx.pan.utils.RedisCache;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author leaving
 * @date 2024/2/15 09:09
 * @description:
 * @Version: 1.0
 */
@Service("notesService")
@Transactional
public class NotesServiceImpl extends ServiceImpl<FileMapper, FileBean> implements NotesService {
    @Resource
    private FileMapper fileMapper;

    @Resource
    private DocumentMapper documentMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private FileUtils fileUtils;

    @Value("${fx.absoluteFilePath}")
    private String absoluteFilePath;
    @Override
    public List<FileBean> getNoteList(String filePath, Long userId, Integer isDir) {

        LambdaQueryWrapper<FileBean> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(FileBean::getIsDir, isDir == 1 ? 1 : 0);
        queryWrapper.eq(FileBean::getAudit, 1);
        queryWrapper.eq(FileBean::getFilePath, filePath);
        queryWrapper.eq(FileBean::getUserId, userId);
        queryWrapper.eq(FileBean::getFileExt, "md");
        List<FileBean> list = fileMapper.selectList(queryWrapper);
        return list;
    }

    @SneakyThrows
    @Override
    public Document addNote(Long userId, String filePath, Integer dir, String fileName, String context) {
        FileBean createFileBean = new FileBean();
        // 获取时间戳

        String date = DateUtil.getDateByTimeStamp(String.valueOf(System.currentTimeMillis()));
        fileName = StringUtils.isEmpty(fileName) ? "新建笔记 - " + date + ".md" : fileName;
        createFileBean.setFileName(fileName);
        createFileBean.setVersion(1);
        createFileBean.setFilePath("/");
        createFileBean.setFileExt("md");
        createFileBean.setUserId(userId);
        createFileBean.setIsDir(0);
        createFileBean.setFileType(2);
        createFileBean.setAudit(1);
        createFileBean.setFileSize((long) context.getBytes().length);
        createFileBean.setFileCreateTime(new Date());
        createFileBean.setFileUpdateTime(new Date());


        int insert = fileMapper.insert(createFileBean);
        Document document = new Document();
        if (insert > 0) {
            String idStr = IdWorker.getIdStr();
            document.setId(idStr);
            document.setFileId(createFileBean.getId());
            document.setVersion(createFileBean.getVersion());
            document.setFileName(fileName);
            document.setContext(context);
            document.setSize((long) context.getBytes().length);
            document.setIdentifier(String.valueOf(context.hashCode()));
            document.setUpdateTime(new Date());
            documentMapper.insert(document);
        }

        return document;
    }

    @Override
    public Document selectById(String id) {
        Document document = documentMapper.selectById(id);
        return document;
    }

    @Override
    public Document loadByFileIdAndVersion(String id, int version) {
        LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Document::getFileId, Long.valueOf(id));
        queryWrapper.eq(Document::getVersion, version);
        Document document = documentMapper.selectOne(queryWrapper);
        return document;
    }

    @Override
    public Document updateDocument(Long id, String context, String updateVersion) {
        Document document = documentMapper.selectByFileId(id);
        FileBean fileBean = fileMapper.selectById(document.getFileId());
        fileBean.setFileUpdateTime(new Date());

        if ("1".equals(updateVersion)) {
            document.setId(IdWorker.getIdStr());
            document.setVersion(document.getVersion() + 1);
            document.setUpdateTime(new Date());
            documentMapper.updateById(document);

            fileBean.setVersion(document.getVersion());
            fileMapper.updateById(fileBean);
        } else {
            document.setContext(context);
            document.setUpdateTime(new Date());
            documentMapper.updateById(document);

            fileMapper.updateById(fileBean);
        }
        return document;
    }

    @Override
    public List<Document> getNoteVersions(List<Long> fileIds) {
        LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Document::getFileId, fileIds);
        List<Document> documents = documentMapper.selectList(queryWrapper);
        return documents;
    }
}
