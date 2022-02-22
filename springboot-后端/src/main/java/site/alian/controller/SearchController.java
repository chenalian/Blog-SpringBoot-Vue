package site.alian.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.alian.aspect.AESDecrypt;
import site.alian.entity.BlogEntity;
import site.alian.service.BlogService;
import site.alian.utils.BlogUtil;
import site.alian.utils.R;

import java.util.HashMap;

/**
 * 搜索接口
 */
@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private BlogService blogService;

    @AESDecrypt
    @GetMapping("/search")
    public R getOneBlog(@RequestParam HashMap<String, Object> param) {
        int pageNum = BlogUtil.objToInt(param.get("pageNum"));
        int pageSize = BlogUtil.objToInt(param.get("pageSize"));
        String query = param.get("searchInput").toString();
        PageInfo<BlogEntity> info = blogService.selectListByTitle(pageNum, pageSize, query);
        return R.success("【" + query + "】搜索成功！").setAttribute("info", info);
    }

}