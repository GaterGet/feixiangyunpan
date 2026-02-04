package com.fx.pan.handle;

import cn.hutool.json.JSONUtil;
import com.fx.pan.common.HttpStatus;
import com.fx.pan.domain.ResponseResult;
import com.fx.pan.utils.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author leaving
 * @date 2022/1/21 14:46
 * @version 1.0
 */

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult msg = new ResponseResult(HttpStatus.FORBIDDEN,"您没有权限访问");
        String json = JSONUtil.toJsonStr(msg);
        WebUtil.renderString(response, json);
    }
}
