package site.alian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.alian.entity.BlogEntity;
import site.alian.service.BlogService;
import site.alian.utils.R;

import javax.servlet.http.HttpServletRequest;

/**
 * 博客接口
 */
@RestController
@RequestMapping("/api")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blog/{id}")
    public R selectOneBlog(@PathVariable String id, HttpServletRequest request) throws Exception {
        blogService.updateBlogViewCount(id);
        BlogEntity blog = blogService.selectOneById(id);
        return R.success("博客内容获取成功！").setAttribute("blog", blog);
    }

}