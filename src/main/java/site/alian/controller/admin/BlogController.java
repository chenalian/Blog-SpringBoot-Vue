package site.alian.controller.admin;

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
 * 博客管理接口
 *
 * Spring默认使用类名作为bean的名称，这里防止类名重复添加admin前缀
 */
@RestController("AdminBlogController")
@RequestMapping("/api/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;

    @AESDecrypt
    @GetMapping("/blog")
    public R selectList(@RequestParam HashMap<String, Object> param) {
        // 获取参数
        Integer pageNum = BlogUtil.objToInt(param.get("pageNum"));
        Integer pageSize = BlogUtil.objToInt(param.get("pageSize"));
        Integer commend = BlogUtil.objToInt(param.get("commend"));
        String typeId = param.get("typeId").toString();
        String title = param.get("title").toString();
        commend = commend == 0 ? null : commend;
        typeId = typeId.equals("0") ? null : typeId;
        // 查询所有博客列表
        PageInfo<BlogEntity> info = blogService.selectListByWrapperPage(pageNum, pageSize, title, typeId, commend);
        // 查询所有分类列表
        List<TypeEntity> types = typeService.selectList();
        return R.success("博客列表获取成功")
                .setAttribute("info", info)
                .setAttribute("types", types);
    }

    @GetMapping("/blog/{id}")
    public R selectOneBlog(@PathVariable String id) {
        BlogEntity entity = blogService.adminSelectOneById(id);
        List<TypeEntity> types = typeService.selectList();
        return R.success("博客获取成功！")
                .setAttribute("blog", entity)
                .setAttribute("types", types);
    }

    @AESDecrypt
    @DeleteMapping("/blog")
    public R deleteById(@RequestParam HashMap<String, Object> param) {
        String id = param.get("id").toString();
        blogService.deleteById(id);
        return R.success("删除成功！");
    }

    @AESDecrypt
    @PostMapping("/blog")
    public R releaseBlog(@RequestBody BlogEntity entity) {
        blogService.releaseBlog(entity);
        return R.success("博客发布成功！");
    }

    @PutMapping("/blog/{id}/release/{release}")
    public R updateRelease(
            @PathVariable String id,
            @PathVariable Integer release) {
        BlogEntity entity = new BlogEntity();
        entity.setId(id);
        entity.setRelease(release);
        blogService.updateRelease(entity);
        return R.success("发布状态更新成功！");
    }

    @PutMapping("/blog/{id}/commend/{commend}")
    public R updateCommend(
            @PathVariable String id,
            @PathVariable Integer commend) {
        BlogEntity entity = new BlogEntity();
        entity.setId(id);
        entity.setCommend(commend);
        blogService.updateCommend(entity);
        return R.success("推荐状态更新成功！");
    }

    @AESDecrypt
    @PutMapping("/blog")
    public R updateBlog(@RequestBody BlogEntity entity) {
        blogService.updateBlog(entity);
        return R.success("博客更新成功！");
    }

}