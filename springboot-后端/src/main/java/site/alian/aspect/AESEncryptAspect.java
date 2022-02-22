package site.alian.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.alian.utils.BlogUtil;
import site.alian.utils.Constant;
import site.alian.utils.R;

import java.util.HashMap;

/**
 * Controller AES加密：针对所有请求的返回结果进行加密
 */
@Aspect
@Order(2)
@Component
public class AESEncryptAspect {

    @Pointcut("execution(* site.alian.controller..*.*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    @SuppressWarnings("unchecked")
    public Object decrypt(ProceedingJoinPoint point) throws Throwable {
        // 获取到控制器返回结果
        R r = (R) point.proceed(point.getArgs());
        // 如果包含结果集，将结果集进行加密后返回
        if (r.containsKey("data")) {
            String json = BlogUtil.mapToJson((HashMap<String, Object>)r.get("data"));
            String encrypt = BlogUtil.AESEncrypt(json);
            r.remove("data");
            r.setAttribute(Constant.SUCCESS_NAME, encrypt);
        }
        return r;
    }
}