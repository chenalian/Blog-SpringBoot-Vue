package site.alian.aspect;

import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import site.alian.utils.BlogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Controller AES解密：POST、PUT请求
 *    1. 使用@ControllerAdvice注解扫描Controller所在位置
 *    2. 实现RequestBodyAdvice接口来处理参数
 *
 *    经测试：RequestBodyAdvice执行优先级高于AOP
 */
@ControllerAdvice("site.alian.controller")
public class AESPostPutDecryptAspect implements RequestBodyAdvice {

    // 判断当前Controller是否需要进行参数解密
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        // 只有标识了AESDecrypt注解的控制器才需要解密
        return methodParameter.hasMethodAnnotation(AESDecrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                String json = null;
                try {
                    InputStream body = httpInputMessage.getBody();
                    HashMap<String, Object> map = BlogUtil.jsonToMap(IOUtils.toString(body));
                    String encrypt = map.get("json").toString();
                    json = BlogUtil.AESDecrypt(encrypt);
                } catch (Exception e) {
                    // TODO 异常处理待考究
                }
                return IOUtils.toInputStream(json);
            }
            @Override
            public HttpHeaders getHeaders() {
                return httpInputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

}
