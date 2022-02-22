package site.alian.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import site.alian.exception.AESException;
import site.alian.exception.TypeConverterException;
import site.alian.utils.BlogUtil;

import java.util.HashMap;

/**
 * Controller AES解密：GET、DELETE请求
 *    由于AESOtherDecryptAspect实现的RequestBodyAdvice只针对拥有请求体的HTTP请求生效
 *    而GET、DELETE请求不包含请求体，所以这里单独使用AOP进行解密
 */
@Aspect
@Order(1)
@Component
public class AESGetDeleteDecryptAspect {

    // 切入点：只有使用了@AESDecrypt注解的GET请求才会执行解密
    @Pointcut("@annotation(site.alian.aspect.AESDecrypt) && " +
            "(@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping)) && " +
            "execution(* site.alian.controller..*.*(..))")
    public void pointcut() { }

    @Around("pointcut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取到请求的参数列表进行解密
        Object[] args = joinPoint.getArgs();
        this.decrypt(args);
        // 执行将解密的结果交给控制器进行处理，并返回处理结果
        return joinPoint.proceed(args);
    }

    // 解密方法
    @SuppressWarnings("unchecked")
    public void decrypt(Object[] args) throws AESException, TypeConverterException {
        // 获取请求参数并转换为字符串（密文）
        HashMap<String, Object> data = (HashMap<String, Object>) args[0];
        String encrypt = data.get("json").toString();
        // 将密文解密为JSON字符串
        String json = BlogUtil.AESDecrypt(encrypt);
        // 将JSON字符串转换为Map集合，并替换原本的参数
        args[0] = BlogUtil.jsonToMap(json);
    }
}
