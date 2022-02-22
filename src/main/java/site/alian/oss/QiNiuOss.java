package site.alian.oss;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.stereotype.Component;
import site.alian.exception.OssException;
import site.alian.oss.base.BaseOSS;
import site.alian.utils.Constant;

import java.io.*;

@Component
public class QiNiuOss implements BaseOSS {

    private UploadManager uploadManager;

    // 初始化upload对象
    public QiNiuOss() {
        Configuration configuration = new Configuration(Region.huanan());
        this.uploadManager = new UploadManager(configuration);
    }

    @Override
    public String upload(String name, String path) throws OssException {
        return upload(name, new File(path));
    }

    @Override
    public String upload(String name, File file) throws OssException {
        try {
            return upload(name, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new OssException("【七牛云对象存储】获取输入流失败");
        }
    }

    @Override
    public String upload(String name, byte[] bytes) throws OssException {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        return this.upload(name, is);
    }

    // 真正执行上传的地方，之前的重载方法最终都会调用这个方法
    @Override
    public String upload(String name, InputStream inputStream) throws OssException {
        String fileName = getFileName(Constant.OSS_Q_BASE_URL, name);
        String token = getToken();
        try {
            uploadManager.put(inputStream, fileName, token, null, null);
        } catch (QiniuException e) {
            throw new OssException("【七牛云对象存储】文件上传失败！");
        }
        return Constant.OSS_Q_ADDRESS + fileName;
    }

    // 获取文件上传令牌
    private String getToken() {
        StringMap map = new StringMap();
        StringMap params = map.put("fileType", Constant.OSS_Q_FILE_TYPE);
        Auth auth = Auth.create(Constant.OSS_Q_ACCESS_KEY, Constant.OSS_Q_SECRET_KEY);
        return auth.uploadToken(Constant.OSS_Q_BUCKET, null, 10, params);
    }

}