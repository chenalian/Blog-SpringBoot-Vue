package site.alian.oss.base;

import site.alian.exception.OssException;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

public interface BaseOSS {

    // 根据File对象上传文件
    String upload(String name, File file) throws OssException;
    // 根据URL上传文件
    String upload(String name, String path) throws OssException;
    // 根据输入流上传文件
    String upload(String name, InputStream inputStream) throws OssException;
    // 根据字节数组上传文件
    String upload(String name, byte[] bytes) throws OssException;

    // 获取随机文件名称
    default String getFileName(String baseUrl, String name) {
        String[] split = name.split("\\.");
        String end = split[split.length-1];
        return baseUrl + UUID.randomUUID().toString() + "." + end;
    }
}