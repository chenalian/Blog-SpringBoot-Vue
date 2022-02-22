package site.alian.controller.admin;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.alian.aspect.AESDecrypt;
import site.alian.entity.MessageEntity;
import site.alian.service.MessageService;
import site.alian.utils.BlogUtil;
import site.alian.utils.R;

import java.util.HashMap;

@RestController("AdminMessageController")
@RequestMapping("/api/admin")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @AESDecrypt
    @GetMapping("/message")
    public R selectList(@RequestParam HashMap<String, Object> param) {
        Integer pageNum = BlogUtil.objToInt(param.get("pageNum"));
        Integer pageSize = BlogUtil.objToInt(param.get("pageSize"));
        PageInfo<MessageEntity> info = messageService.selectList(pageNum, pageSize);
        return R.success("留言获取成功").setAttribute("info", info);
    }

    @AESDecrypt
    @PostMapping("/message")
    public R replyMessage(@RequestBody HashMap<String, Object> param) {
        String id = param.get("id").toString();
        String content = param.get("content").toString();
        messageService.replyMessage(id, content);
        return R.success("留言回复成功！");
    }

    @AESDecrypt
    @DeleteMapping("/message")
    public R deleteMessage(@RequestParam HashMap<String, Object> param) {
        String ids = param.get("ids").toString();
        messageService.deleteMessage(ids);
        return R.success("留言删除成功！");
    }

}
