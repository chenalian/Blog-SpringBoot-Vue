package site.alian.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.alian.exception.*;
import site.alian.utils.Constant;
import site.alian.utils.R;

@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(AESException.class)
    public R aesException(AESException e) {
        return common(e);
    }

    @ExceptionHandler(BlogNotFoundException.class)
    public R aesException(BlogNotFoundException e) {
        String message = e.getMessage();
        return R.error(Constant.RESULT_CODE_BLOG_NOT_FOUND, message);
    }

    @ExceptionHandler(TypeConverterException.class)
    public R aesException(TypeConverterException e) {
        return common(e);
    }

    @ExceptionHandler(TypeExistsException.class)
    public R typeExistsException(TypeExistsException e) {
        return this.common(e);
    }

    @ExceptionHandler(TypeBlogNotZeroException.class)
    public R typeExistsException(TypeBlogNotZeroException e) {
        return this.common(e);
    }


    // 全局异常处理是由上至下的，如果没有任何一个匹配到结果，那么就使用这个Exception兜底
    @ExceptionHandler(Exception.class)
    public R exception(Exception e) {
        log.error("未捕获到的异常：" + e.getClass().getName());
        e.printStackTrace();
        return R.error(e.getMessage());
    }


    /* ------------------------  共用模块  ------------------------ */
    public R common(Exception e) {
        return R.error(e.getMessage());
    }

}