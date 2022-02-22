package site.alian.exception;

/**
 * 处理博客异常
 */
public class BlogNotFoundException extends Exception {

    public BlogNotFoundException(String msg) {
        super(msg);
    }

}