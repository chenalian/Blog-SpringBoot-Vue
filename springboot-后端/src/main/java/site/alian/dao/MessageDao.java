package site.alian.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import site.alian.entity.MessageEntity;

@Repository
public interface MessageDao extends BaseMapper<MessageEntity> {
}
