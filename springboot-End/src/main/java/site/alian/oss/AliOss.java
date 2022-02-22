package site.alian.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import site.alian.exception.OssException;
import site.alian.oss.base.BaseOSS;
import site.alian.utils.Constant;

import java.io.*;

//@Component
public class AliOss implements BaseOSS {

    private OSS build;

    // 初始化OSS实例
    public AliOss(){
        this.build = new OSSClientBuilder().build(Constant.ENDPOINT, Constant.OSS_A_ACCESS_KEY, Constant.OSS_A_SECRET_KEY);
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
            throw new OssException("【阿里云】获取字符输入流失败!");
        }
    }

    @Override
    public String upload(String name, byte[] bytes) {
        return upload(name, new ByteArrayInputStream(bytes));
    }

    @Override
    public String upload(String name, InputStream inputStream) {
        String fileName = getFileName(Constant.OSS_A_BASE_URL, name);
        build.putObject(Constant.OSS_A_BUCKET, fileName, inputStream);
        return Constant.OSS_A_ADDRESS + fileName;
    }
}