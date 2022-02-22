package site.alian.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import site.alian.entity.BlogEntity;

@Repository
public interface BlogDao extends BaseMapper<BlogEntity> {

    void updateBlogViewCount(String id);

}