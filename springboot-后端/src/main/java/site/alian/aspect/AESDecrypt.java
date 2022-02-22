package site.alian.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 只有使用了该注解的方法才需要执行AES解密
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AESDecrypt { }