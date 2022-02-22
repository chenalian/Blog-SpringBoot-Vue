package site.alian.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.alian.dao.BlogDao;
import site.alian.dao.TypeDao;
import site.alian.entity.BlogEntity;
import site.alian.entity.TypeEntity;
import site.alian.exception.BlogNotFoundException;
import site.alian.utils.BlogUtil;
import site.alian.utils.Constant;

import java.util.List;

/**
 * 处理博客业务逻辑
 *   boolean all = true：查询所有博客
 *   boolean all = false：只查询已发布的博客
 */
@Service
public class BlogService {

    // 这里定义字段数组
    private final String[] columns = {"id", "type_id", "title", "img_url", "create_time", "view_count", "`desc`", "`release`", "commend"};

    @Autowired
    private BlogDao blogDao;
    @Autowired
    private TypeDao typeDao;

    // 查询所有博客列表
    public PageInfo<BlogEntity> selectListPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<BlogEntity>()
                .select(columns)
                .eq("`release`", 1)
                .orderByDesc("create_time");
        List<BlogEntity> list = blogDao.selectList(wrapper);
        this.blogListAddTypeName(list);
        return new PageInfo<>(list, Constant.PAGING_BAR_SIZE);
    }

    // 查询所有推荐博客
    public List<BlogEntity> selectCommendList() {
        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<BlogEntity>()
                .select("id", "title")
                .eq("`release`", 1)
                .eq("commend", 1);
        return blogDao.selectList(wrapper);
    }

    // 通过分类查找博客
    public PageInfo<BlogEntity> selectListByTypeIdPage(int pageNum, int pageSize, String typeId) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<BlogEntity>()
                .eq("type_id", typeId)
                .eq("`release`", 1);
        List<BlogEntity> list = blogDao.selectList(wrapper);
        this.blogListAddTypeName(list);
        return new PageInfo<>(list, Constant.PAGING_BAR_SIZE);
    }

    // 通过ID查找博客
    public BlogEntity selectOneById(String id) throws BlogNotFoundException {
        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<BlogEntity>()
                .eq("id", id)
                .eq("`release`", 1);
        BlogEntity blog = blogDao.selectOne(wrapper);
        if (blog == null) {
            // 如果博客不存在要抛出一唱歌
            throw new BlogNotFoundException("该博客不存在或已被删除！");
        }
        // 追加博客的分类名称
        TypeEntity type = typeDao.selectOne(new QueryWrapper<TypeEntity>()
                .select("name")
                .eq("id", blog.getTypeId()));
        blog.setTypeName(type.getName());
        // 将Markdown转换为HTML返回
        String html = BlogUtil.mdToHtml(blog.getContent());
        blog.setContent(html);
        return blog;
    }

    // 博客访问量+1
    public void updateBlogViewCount(String id) throws BlogNotFoundException {
        blogDao.updateBlogViewCount(id);
    }

    // 通过标题模糊查询博客
    public PageInfo<BlogEntity> selectListByTitle(int pageNum, int pageSize, String query) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<BlogEntity>()
                .select(columns)
                .like("title", query)
                .eq("`release`", 1);
        List<BlogEntity> list = blogDao.selectList(wrapper);
        blogListAddTypeName(list);
        return new PageInfo<>(list, Constant.PAGING_BAR_SIZE);
    }

    // Admin-查询所有博客列表
    public PageInfo<BlogEntity> selectListByWrapperPage(int pageNum, int pageSize, String title, String typeId, Integer commend) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<BlogEntity>()
                .select(columns)
                .orderByDesc("create_time");
        if (commend != null)
            wrapper.eq("commend", 1);
        if (typeId != null)
            wrapper.eq("type_id", typeId);
        if (title != null && title.equals(""))
            wrapper.like("title", title);
        List<BlogEntity> list = blogDao.selectList(wrapper);
        this.blogListAddTypeName(list);
        return new PageInfo<>(list, Constant.PAGING_BAR_SIZE);
    }

    // Admin-通过ID删除博客
    public void deleteById(String id) {
        blogDao.deleteById(id);
    }

    // Admin-更新博客隐藏状态 这里不对修改时间进行更新
    public void updateRelease(BlogEntity entity) {
        // 如果博客下架，那么也没有推荐的必要了
        if (entity.getRelease() == 0) {
            entity.setCommend(0);
        }
        blogDao.updateById(entity);
    }

    // Admin-更新博客隐藏状态 这里不对修改时间进行更新
    public void updateCommend(BlogEntity entity) {
        // 如果博客下架，那么也没有推荐的必要了
        blogDao.updateById(entity);
    }

    // TODO Admin-发布博客 这里后续可以考虑使用MyBatisPlus的自动填充
    public void releaseBlog(BlogEntity entity) {
        long time = System.currentTimeMillis();
        entity.setCreateTime(time);
        entity.setUpdateTime(time);
        entity.setViewCount(0);
        String content = entity.getContent();
        String desc = getDesc(content);
        entity.setDesc(desc);
        blogDao.insert(entity);
    }

    // Admin-通过ID查询单挑博客
    public BlogEntity adminSelectOneById(String id) {
        return blogDao.selectById(id);
    }

    // Admin-更新博客 这里后续可以考虑使用MyBatisPlus的自动填充
    public void updateBlog(BlogEntity entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        String content = entity.getContent();
        String desc = getDesc(content);
        entity.setDesc(desc);
        blogDao.updateById(entity);
    }









    /* ------------------------------------------------------------------------- */

    // 为博客列表集合批量添加分类名称
    private void blogListAddTypeName(List<BlogEntity> blogList) {
        if (blogList.size() > 0) {
            List<TypeEntity> typeList = typeDao.selectList(null);
            blogList.forEach(blog -> {
                typeList.forEach(type -> {
                    if (blog.getTypeId().equals(type.getId())) {
                        blog.setTypeName(type.getName());
                    }
                });
            });
        }
    }

    // 截取博客内容前100字符作为描述信息
    public String getDesc(String content) {
        if(content.length() > 100) {
            String str = content.substring(0, 100);
            str = str.replaceAll("#", "");
            str = str.replaceAll("-", "");
            str = str.replaceAll(">", "");
            str = str.replaceAll("`", "");
            str = str.replaceAll("~`", "");
            str = str.replaceAll("\\*", "");
            return str + "...";
        } else return content + "...";
    }

}
