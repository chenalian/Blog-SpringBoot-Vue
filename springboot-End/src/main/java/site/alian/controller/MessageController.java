package site.alian.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.alian.aspect.AESDecrypt;
import site.alian.entity.MessageEntity;
import site.alian.service.MessageService;
import site.alian.utils.BlogUtil;
import site.alian.utils.R;

import java.util.HashMap;

/**
 * 留言板接口
 */
@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @AESDecrypt
    @GetMapping("/message")
    public R selectMessage(@RequestParam HashMap<String, Object> param) {
        Integer pageNum = BlogUtil.objToInt(param.get("pageNum"));
        Integer pageSize = BlogUtil.objToInt(param.get("pageSize"));
        PageInfo<MessageEntity> info = messageService.selectList(pageNum, pageSize);
        return R.success("浏览获取成功！").setAttribute("info", info);
    }

    @AESDecrypt
    @PostMapping("/message")
    public R guestMessage(@RequestBody HashMap<String, Object> param) {
        String name = param.get("name").toString();
        String email = param.get("email").toString();
        String content = param.get("content").toString();
        messageService.guestMessage(name, email, content);
        //return R.success("【" + name + "】留言成功");
        return R.success("留言成功");
    }

}