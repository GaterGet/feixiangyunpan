package com.fx.pan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fx.pan.domain.Document;
import com.fx.pan.domain.FileBean;

import java.util.List;

/**
 * @author leaving
 * @date 2024/2/15 09:08
 * @description:
 * @Version: 1.0
 */
public interface NotesService extends IService<FileBean> {
    List<FileBean> getNoteList(String filePath, Long userId, Integer dir);

    Document addNote(Long userId, String filePath, Integer dir, String fileName, String context);

    Document selectById(String id);

    Document loadByFileIdAndVersion(String id, int version);

    Document updateDocument(Long id, String context, String updateVersion);


    List<Document> getNoteVersions(List<Long> fileIds);
}
