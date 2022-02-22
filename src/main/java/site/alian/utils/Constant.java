package site.alian.utils;

public class Constant {

    // 底部分页条显示候选页码数量
    public static final int PAGING_BAR_SIZE = 7;

    // AES算法、补码方式
    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/NoPadding";

    // AES加密的密匙和偏移量
    public static final String AES_KEY = "1234567890hijklm";
    public static final String AES_IV = "1234567890abcdef";

    // 通用返回结果R：返回的加密参数的名称
    public static final String SUCCESS_NAME = "result";

    // 通用返回结果R：处理失败的状态码
    public static final int RESULT_CODE_BLOG_NOT_FOUND = 201;   // 找不到博客
    public static final int RESULT_CODE_TOKEN_ERROR = 202;    // Token异常

    // Token生存时间 单位分钟
    public static final int TOKEN_EXPIRES_TIME = 30;


    //服务器保存图片的目录
    public static final String WinImageDir="/src/main/resources/static/static/blogImage/";


    // 阿里云OSS
    public static final String OSS_A_ADDRESS = "";
    public static final String OSS_A_BASE_URL = "";
    public static final String ENDPOINT = "";
    public static final String OSS_A_ACCESS_KEY = "";
    public static final String OSS_A_SECRET_KEY = "";
    public static final String OSS_A_BUCKET = "";

    // 七牛云OSS
    public static final String OSS_Q_ADDRESS = "";
    public static final String OSS_Q_BASE_URL = "";
    public static final String OSS_Q_ACCESS_KEY = "";
    public static final String OSS_Q_SECRET_KEY = "";
    public static final String OSS_Q_BUCKET = "";
    public static final int OSS_Q_FILE_TYPE = 0;  // 0: 一般存储类型

}