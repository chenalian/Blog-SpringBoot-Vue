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
import site.alian.exception.TypeBlogNotZeroException;
import site.alian.exception.TypeExistsException;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeDao typeDao;
    @Autowired
    private BlogDao blogDao;

    // 获取所有分类
    public List<TypeEntity> selectList() {
        return typeDao.selectList(null);
    }

    // 获取所有分类
    public PageInfo<TypeEntity> selectListPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TypeEntity> list = typeDao.selectList(new QueryWrapper<TypeEntity>().orderByDesc("create_time"));
        return new PageInfo<>(list);
    }

    // 获取所有分类以及分类下的博客ID
    public List<TypeEntity> selectListAndCount() {
        List<TypeEntity> typeList = this.selectList();
        List<BlogEntity> blogList = this.selectBlogIdAndTypeIdList(true);
        blogList.forEach(blog -> {
            typeList.forEach(type -> {
                if (blog.getTypeId().equals(type.getId())){
                    type.setCount(type.getCount()+1);
                }
            });
        });
        return typeList;
    }

    // 添加新的分类
    public void insertType(String name) throws TypeExistsException {
        if (typeExists(name)) {
            throw new TypeExistsException("该分类名称已存在！");
        }
        TypeEntity entity = new TypeEntity();
        long time = System.currentTimeMillis();
        entity.setName(name);
        entity.setCreateTime(time);
        entity.setUpdateTime(time);
        typeDao.insert(entity);
    }

    // 修改分类名称
    public void updateType(TypeEntity entity) throws TypeExistsException {
        if (typeExists(entity.getName())) {
            throw new TypeExistsException("该分类名称已存在！");
        }
        entity.setUpdateTime(System.currentTimeMillis());
        typeDao.updateById(entity);
    }

    // 删除分类
    public void deleteType(String id) throws TypeBlogNotZeroException {
        Integer count = blogDao.selectCount(new QueryWrapper<BlogEntity>().eq("type_id", id));
        if (count == 0) {
            typeDao.deleteById(id);
        } else throw new TypeBlogNotZeroException("请先处理该分类下的" + count + "篇博客");
    }









    // 查询分类是否存在，true：存在，false：不存在
    public boolean typeExists(String name) {
        return typeDao.selectOne(new QueryWrapper<TypeEntity>().eq("name", name)) != null;
    }

    // 查询所有博客ID、分类ID
    public List<BlogEntity> selectBlogIdAndTypeIdList(boolean all) {
        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<BlogEntity>().select("id", "type_id");
        if (!all) {
            wrapper.eq("`release`", 1);
        }
        return blogDao.selectList(wrapper);
    }

}