package site.alian.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.alian.dao.MessageDao;
import site.alian.entity.MessageEntity;

import java.util.*;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    // 查询留言
    public PageInfo<MessageEntity> selectList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MessageEntity> parentList = messageDao.selectList(new QueryWrapper<MessageEntity>()
                .isNull("parent")
                .orderByDesc("create_time"));
        String[] parentIdArray = new String[parentList.size()];
        if (parentList.size() < 1)
            return new PageInfo<>(parentList);
        for (int i = 0; i < parentIdArray.length; i++){
            parentIdArray[i] = parentList.get(i).getId();
        }
        List<MessageEntity> childrenList = messageDao.selectList(new QueryWrapper<MessageEntity>()
                .in("parent", Arrays.asList(parentIdArray))
                .orderByAsc("create_time"));
        parentList.forEach(level1->{
            childrenList.forEach(level2->{
                List<MessageEntity> children = level1.getChildren();
                if (level1.getId().equals(level2.getParent())){
                    children.add(level2);
                }
            });
        });
        return new PageInfo<>(parentList);
    }

    // 游客留言
    public void guestMessage(String name, String email, String content) {
        MessageEntity entity = new MessageEntity();
        entity.setName(name);
        entity.setEmail(email);
        entity.setContent(content);
        entity.setCreateTime(new Date().getTime());
        messageDao.insert(entity);
    }

    // 管理员回复留言
    public void replyMessage(String id, String content) {
        MessageEntity entity = new MessageEntity();
        entity.setParent(id);
        entity.setContent(content);
        entity.setName("站长回复");
        entity.setCreateTime(System.currentTimeMillis());
        messageDao.insert(entity);
    }

    // 删除留言
    public void deleteMessage(String ids) {
        String[] split = ids.split("-");
        messageDao.delete(new QueryWrapper<MessageEntity>().in("id", split));
    }

}
