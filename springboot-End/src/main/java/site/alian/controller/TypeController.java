package site.alian.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.alian.aspect.AESDecrypt;
import site.alian.entity.BlogEntity;
import site.alian.entity.TypeEntity;
import site.alian.service.BlogService;
import site.alian.service.TypeService;
import site.alian.utils.BlogUtil;
import site.alian.utils.R;

import java.util.HashMap;
import java.util.List;

/**
 * 分类接口
 */
@RestController
@RequestMapping("/api")
public class TypeController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/type")
    public R action() {
        List<TypeEntity> typeList = typeService.selectListAndCount();
        return R.success("分类信息获取成功！").setAttribute("types", typeList);
    }

    @AESDecrypt
    @GetMapping("/type/{typeId}")
    public R selectBlogByTypeId(
            @RequestParam HashMap<String, Object> param,
            @PathVariable("typeId") String typeId) {
        int pageNum = BlogUtil.objToInt(param.get("pageNum"));
        int pageSize = BlogUtil.objToInt(param.get("pageSize"));
        PageInfo<BlogEntity> info = blogService.selectListByTypeIdPage(pageNum, pageSize, typeId);
        return R.success("获取分类下博客成功！").setAttribute("info", info);
    }

}