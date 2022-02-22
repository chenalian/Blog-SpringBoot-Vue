package site.alian.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.alian.dao.FieldDao;
import site.alian.entity.FieldEntity;
import site.alian.utils.BlogUtil;

import java.util.List;

@Service
public class FieldService {

    @Autowired
    private FieldDao fieldDao;

    // 查询所有字段
    public List<FieldEntity> selectList() {
        return fieldDao.selectList(new QueryWrapper<FieldEntity>()
                .ne("field", "account")
                .ne("field", "password"));
    }

    // 查询字段
    public List<FieldEntity> selectField(String ...fields) {
        QueryWrapper<FieldEntity> wrapper = new QueryWrapper<>();
        for (String field : fields) {
            wrapper.eq("field", field);
        }
        return fieldDao.selectList(wrapper);
    }

    // 修改某个字段
    public void updateField(FieldEntity entity) {
        fieldDao.updateById(entity);
    }

    // 修改账号
    public void updateAccount(String account) {
        FieldEntity entity = new FieldEntity();
        entity.setValue(account);
        fieldDao.update(entity, new UpdateWrapper<FieldEntity>().eq("field", "account"));
    }

    // 修改密码
    public void updatePassword(String password) {
        FieldEntity entity = new FieldEntity();
        password = BlogUtil.toMD5(BlogUtil.toMD5(password));
        entity.setValue(password);
        fieldDao.update(entity, new UpdateWrapper<FieldEntity>().eq("field", "password"));
    }

    // 管理员登录
    public boolean login(String account, String password) {
        List<FieldEntity> list = fieldDao.selectList(new QueryWrapper<FieldEntity>()
                .in("field", "account", "password"));
        for (FieldEntity entity : list) {
            if (entity.getField().equals("account")) {
                if (!entity.getValue().equals(account))
                    return false;
            } else {
                if (!entity.getValue().equals(BlogUtil.toMD5(BlogUtil.toMD5(password))))
                    return false;
            }
        }
        return true;
    }

}