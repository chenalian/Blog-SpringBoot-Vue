package site.alian.controller.admin;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.alian.aspect.AESDecrypt;
import site.alian.entity.TypeEntity;
import site.alian.exception.TypeBlogNotZeroException;
import site.alian.exception.TypeExistsException;
import site.alian.service.TypeService;
import site.alian.utils.BlogUtil;
import site.alian.utils.R;

import java.util.HashMap;

@RestController("AdminTypeController")
@RequestMapping("/api/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @AESDecrypt
    @GetMapping("/type")
    public R selectList(@RequestParam HashMap<String, Object> param) {
        Integer pageNum = BlogUtil.objToInt(param.get("pageNum"));
        Integer pageSize = BlogUtil.objToInt(param.get("pageSize"));
        PageInfo<TypeEntity> info = typeService.selectListPage(pageNum, pageSize);
        return R.success("分类获取成功").setAttribute("info", info);
    }
    @AESDecrypt
    @PostMapping("/type")
    public R insertType(@RequestBody HashMap<String, Object> param) throws TypeExistsException {
        String name = param.get("name").toString();
        typeService.insertType(name);
        return R.success("【" + name + "】分类添加成功！");
    }

    @AESDecrypt
    @PutMapping("/type")
    public R updateType(@RequestBody TypeEntity entity) throws TypeExistsException {
        typeService.updateType(entity);
        return R.success("分类名称修改成功！");
    }

    @DeleteMapping("/type/{id}")
    public R deleteType(@PathVariable String id) throws TypeBlogNotZeroException {
        typeService.deleteType(id);
        return R.success("分类删除成功！");
    }
}