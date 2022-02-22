package site.alian.filters;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  1. 这里切记不能用@Component或者其他注解添加到IOC容器，会造成二次过滤，
 *     要在配置类中使用@ServletComponentScan进行扫描
 *
 *  2. 过滤路径中的通配符使用单星号*即可，不可使用双星号**
 */
@Order(1)
@WebFilter("/api/*")
public class CorsFilter implements Filter {


    //对每次请求拦截两次
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE,token");
        // 允许携带cookie
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(req, resp);
    }
}