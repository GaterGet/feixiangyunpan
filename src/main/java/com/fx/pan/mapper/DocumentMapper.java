package com.fx.pan.mapper;

import com.fx.pan.domain.Document;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author leaving
* @description 针对表【document】的数据库操作Mapper
* @createDate 2024-02-15 15:35:48
* @Entity com.fx.pan.domain.Document
*/
public interface DocumentMapper extends BaseMapper<Document> {

    Document selectByFileId(Long id);
}




