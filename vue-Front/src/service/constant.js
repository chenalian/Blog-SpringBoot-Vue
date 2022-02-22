// AES解密参数
export const AES_KEY = "1234567890hijklm";
export const AES_IV = "1234567890abcdef";
// 请求服务器地址

export const REQUEST_BASE_URL = "http://127.0.0.1:8080/api"; // 前后端分离
// export const REQUEST_BASE_URL = "/api"; //前后端不分离
export const REQUEST_TIMEOUT = 8000;
// 响应状态码
export const RESULT_CODE = {
    // 处理成功状态码
    SUCCESS: 100,
    // 找不到博客
    BLOG_NOT_FOUND: 201,
    // Token处理失败
    TOKEN_ERROR: 202
};
// 分页消息提示
export const NOT_PRE_PAGE_MESSAGE = "已经是第一页了";
export const NOT_NEXT_PAGE_MESSAGE = "已经是最后一页了";
export const PAGE_NUM_FAIL_MESSAGE = "页码不合法或不在范围内";
