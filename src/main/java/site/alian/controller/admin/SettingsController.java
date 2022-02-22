package site.alian.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.alian.aspect.AESDecrypt;
import site.alian.entity.FieldEntity;
import site.alian.entity.LinkEntity;
import site.alian.service.FieldService;
import site.alian.service.LinkService;
import site.alian.utils.R;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class SettingsController {

    @Autowired
    private FieldService fieldService;
    @Autowired
    private LinkService linkService;

    @GetMapping(value = "/field")
    public R getFields() {
        List<FieldEntity> list = fieldService.selectList();
        return R.success("获取字典成功").setAttribute("fields", list);
    }
    @GetMapping("/field/{field}")
    public R getAccount(@PathVariable String field) {
        List<FieldEntity> list = fieldService.selectField(field);
        String value = list.get(0).getValue();
        return R.success().setAttribute("value", value);
    }

    @GetMapping("/link")
    public R getLink() {
        return R.success("有情链接获取成功！").setAttribute("list", linkService.selectLink());
    }

    @AESDecrypt
    @PutMapping("/field")
    public R updateField(@RequestBody FieldEntity entity) {
        fieldService.updateField(entity);
        return R.success("【" + entity.getName() + "】修改完成！需重启浏览器查看效果");
    }

    @AESDecrypt
    @PutMapping("/link")
    public R updateLink(@RequestBody LinkEntity entity) {
        linkService.updateLink(entity);
        return R.success("友链修改完成！需重启浏览器查看效果");
    }

    @AESDecrypt
    @PutMapping("/account")
    public R updateAccount(@RequestBody HashMap<String, Object> param) {
        String account = param.get("account").toString();
        fieldService.updateAccount(account);
        return R.success("登录账号修改成功");
    }

    @AESDecrypt
    @PutMapping("/password")
    public R updatePassword(@RequestBody HashMap<String, Object> param) {
        String password = param.get("password").toString();
        fieldService.updatePassword(password);
        return R.success("登录密码修改成功");
    }
}
