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
 * 首页接口
 */
@RestController
@RequestMapping("/api")
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;

    @AESDecrypt
    @GetMapping("/index")
    public R index(@RequestParam HashMap<String, Object> param) {
        int pageNum = BlogUtil.objToInt(param.get("pageNum"));
        int pageSize = BlogUtil.objToInt(param.get("pageSize"));
        PageInfo<BlogEntity> info = blogService.selectListPage(pageNum, pageSize);
        List<BlogEntity> commend = blogService.selectCommendList();
        List<TypeEntity> types = typeService.selectListAndCount();
        return R.success("首页数据请求成功！")
                .setAttribute("info", info)
                .setAttribute("commend", commend)
                .setAttribute("types", types);
    }

}