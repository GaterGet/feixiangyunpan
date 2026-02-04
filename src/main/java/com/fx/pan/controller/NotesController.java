package com.fx.pan.controller;

import com.fx.pan.annotation.Limit;
import com.fx.pan.common.Constants;
import com.fx.pan.domain.Document;
import com.fx.pan.domain.FileBean;
import com.fx.pan.domain.ResponseResult;
import com.fx.pan.service.FileService;
import com.fx.pan.service.NotesService;
import com.fx.pan.utils.CollectionUtil;
import com.fx.pan.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leaving
 * @date 2024/2/15 09:06
 * @description:
 * @Version: 1.0
 */
@Tag(name = "notes", description = "该接口为在线笔记操作接口，主要用来做一些笔记的基本操作，如创建笔记，删除，移动，复制等。")
@Slf4j
@RequestMapping(value = "/notes")
@RestController
public class NotesController {

    @Value("${fx.absoluteFilePath}")
    String absoluteFilePath;

    @Value("${fx.storageType}")
    Integer storageType;


    @Resource
    private FileService fileService;

    @Resource
    private NotesService notesService;

    /**
     * 添加
     *
     * @return
     */
    @Limit(key = "limit1", permitsPerSecond = 1, timeout = 500, msg = "请求过于频繁，请稍后再试！请求频率限制为1次/秒")
    @PostMapping("/add")
    public ResponseResult add(@RequestParam("userId") Long userId,
                                   @RequestParam(required = false, defaultValue = "/") String filePath,
                                   @RequestParam(required =
                                           false, defaultValue = "0") Integer dir,
                              @RequestParam String fileName, @RequestParam String context) {
        Document note = notesService.addNote(userId, filePath, dir, fileName, context);
        return ResponseResult.success(note);
    }

    @PostMapping("/delete")
    public ResponseResult delete(@RequestParam String id,@RequestParam int version) {
        Long userId = SecurityUtils.getUserId();
        Document document = notesService.selectById(id);
        FileBean fileBean = fileService.selectFileById(document.getFileId());
        if (fileBean.getUserId().equals(userId)) {
            boolean flag = fileService.deleteFile(document.getFileId(), userId);
            return ResponseResult.success("删除成功");
        } else {
            return ResponseResult.success("无删除权限");

        }
    }

    /**
     * 加载文档内容
     *
     * @return
     */
    @GetMapping("/load")
    public ResponseEntity load(@RequestParam String id,@RequestParam int version) {
        Document document = notesService.loadByFileIdAndVersion(id,version);
        String context = document.getContext();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(context, headers, HttpStatus.OK);
    }

    @PostMapping("/modify")
    public ResponseResult load(@RequestParam Long id,@RequestParam String context,
                               @RequestParam String updateVersion) {
        Document newDoc = notesService.updateDocument(id, context, updateVersion);
        return ResponseResult.success(newDoc);
    }


    /**
     * 获取在线笔记列表
     *
     * @return
     */
    @Limit(key = "limit1", permitsPerSecond = 1, timeout = 500, msg = "请求过于频繁，请稍后再试！请求频率限制为1次/秒")
    @GetMapping("/list")
    public ResponseResult fileList(@RequestParam("userId") Long userId,
                                   @RequestParam(required = false, defaultValue = "/") String filePath,
                                   @RequestParam(required =
                                           false, defaultValue = "0") Integer dir, @RequestParam Boolean refresh) {
        String key = Constants.REDIS_FILE_LIST_PREFIX + userId;
        List<FileBean> fl = null;
        Integer auditAccessCount = 0;
        Map map = new HashMap();
        // 判断是否是否强制刷新
        fl = notesService.getNoteList(filePath, userId, dir);
        List<Long> fileIds = fl.stream().map(item -> item.getId()).collect(Collectors.toList());
        List<Document> versions = notesService.getNoteVersions(fileIds);
        Map<Long, List<Document>> versionsMap =
                versions.stream().collect(Collectors.groupingBy(Document::getFileId,
                        CollectionUtil.toSortedList(Comparator.comparing(Document::getVersion))));
        map.put("list", fl);
        map.put("versions", versionsMap);
        map.put("path", filePath);
        map.put("total", auditAccessCount);
        map.put("ts", System.currentTimeMillis());
        return ResponseResult.success("获取成功", map);
    }
}
