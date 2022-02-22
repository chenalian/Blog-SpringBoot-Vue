package site.alian.exception;

/**
 * 分类已存在异常
 */
public class TypeExistsException extends Exception {

    public TypeExistsException(String msg) {
        super(msg);
    }

}