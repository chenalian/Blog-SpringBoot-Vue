package site.alian.controller;

//import org.apache.tomcat.util.net.jsse.JSSEUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import site.alian.aspect.AESDecrypt;
import site.alian.entity.FieldEntity;
import site.alian.service.FieldService;
import site.alian.service.LinkService;
import site.alian.utils.BlogUtil;
import site.alian.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 公共的接口都在这里
 */
@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private FieldService fieldService;
    @Autowired
    private LinkService linkService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/field")
//    @RequestMapping(value="/xxx",produces = {"application/json;charset=UTF-8"})
    public R fieldList(HttpServletRequest request) {
        List<FieldEntity> list = fieldService.selectList();
        return R.success("获取字典成功").setAttribute("fields", list);
    }

    @GetMapping("/link")
    public R linkList() {
        return R.success("有情链接获取成功！").setAttribute("list", linkService.selectLink());
    }

    @AESDecrypt
    @PostMapping("/login")
    public R login(@RequestBody HashMap<String, Object> param, HttpSession session) {
        String account = param.get("account").toString();
        String password = param.get("password").toString();
        boolean flag = fieldService.login(account, password);
        // 如果成功的话将用户名和密码加密成token并存入redis
        String token="";
        if (flag){
            token = BlogUtil.getToken(account+password);
            // 为每个token设置生存时间为1个小时
            redisTemplate.opsForSet().add("token",token,30, TimeUnit.MINUTES);
        }
        return flag ?
                R.success("登录成功！").setAttribute("token", token) :
                R.error("账号或密码输入错误");
    }
    @GetMapping("/test")
    public R test() {
        List<FieldEntity> list = fieldService.selectList();
        return R.success("查询完成").setAttribute("list", list);
    }

}