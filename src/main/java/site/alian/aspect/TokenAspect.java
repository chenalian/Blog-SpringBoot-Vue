package site.alian.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.alian.utils.R;

import javax.servlet.http.HttpServletRequest;

/**
 * 在这里横切每一个Admin请求，并返回最新的Token，更新过期时间
 * 需要注意Order顺序的掌控，要在AESEncryptAspect加密之前将Token放进去，不然会很麻烦
 */
//@Aspect
//@Order(3)
//@Component
public class TokenAspect {

    @Around("execution(* site.alian.controller.admin..*.*(..))")
    @SuppressWarnings("all")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        R r = (R) joinPoint.proceed(args);
        HttpServletRequest req = this.getHttpServletRequest();
        return r;
    }

    // RequestContextHolder可以在Controller之外的地方获取到Request对象  赞！
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Object request = requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        return (HttpServletRequest) request;
    }

}