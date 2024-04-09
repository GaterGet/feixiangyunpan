package com.fx.pan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author leaving
 * @date 2022/3/23 22:28
 * @version 1.0
 */

public class Base64Util {

    /**
     * Base64
     */
    public static String base64(String str) {
        byte[] bytes = str.getBytes();
        //Base64 加密
        String encoded = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Base 64 加密后：" + encoded);
        return encoded;

    }

    /**
     * base64解密
     *
     * @param str
     * @return
     */
    public static String decodeBase64(String str) {
        byte[] decoded = Base64.getDecoder().decode(str);
        return new String(decoded);
    }

    /**
     * 文件转base64
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Arrays.toString(Base64.getEncoder().encode(buffer));
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String targetPath, String catalogue)
            throws Exception {
        File file = new File(catalogue);
        if (!file.exists()) {
            file.mkdirs();
        }
        byte[] buffer = Base64.getDecoder().decode(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }
}
