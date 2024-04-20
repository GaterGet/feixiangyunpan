package com.fx.pan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fx.pan.domain.Document;
import com.fx.pan.service.DocumentService;
import com.fx.pan.mapper.DocumentMapper;
import org.springframework.stereotype.Service;

/**
* @author leaving
* @description 针对表【document】的数据库操作Service实现
* @createDate 2024-02-15 15:35:48
*/
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document>
    implements DocumentService{

}




