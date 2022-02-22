package site.alian.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import site.alian.exception.TypeConverterException;
import site.alian.utils.BlogUtil;
import site.alian.utils.Constant;
import site.alian.utils.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/*
*
*
* 自定义token验证
* 根据成功验证了的用户名和密码生成token
* token存储在redis中，且返回给前端
* 前端每次传输时候携带token和redis中进行匹配
*
*
* */

@Order(1)
@WebFilter("/api/admin/*")
public class TokenFilter implements Filter {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String token = req.getHeader("token");
        // 在redis判断是否存在
        Set token1 = redisTemplate.opsForSet().members("token");
        boolean flag=token1.contains(token);
        if(token==null||!flag)
        {
            R r = R.error(Constant.RESULT_CODE_TOKEN_ERROR, "请先登录");
            this.refuse(resp, r);
            return;
        }
        // 刷新token生存时间
        redisTemplate.opsForSet().add("token",token,30, TimeUnit.MINUTES);
        // 判断
        chain.doFilter(req, resp);
    }

    public void refuse(HttpServletResponse resp, R r) {
        resp.setCharacterEncoding("UTF-8");
        try {
            String json = BlogUtil.mapToJson(r);
            resp.getWriter().write(json);
        } catch (IOException | TypeConverterException e) {
            e.printStackTrace();
        }
    }
}