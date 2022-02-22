package site.alian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.alian.dao.LinkDao;
import site.alian.entity.LinkEntity;

import java.util.List;

@Service
public class LinkService {

    @Autowired
    private LinkDao linkDao;

    // 查询所有有情链接
    public List<LinkEntity> selectLink() {
        return linkDao.selectList(null);
    }

    // 更新有情链接
    public void updateLink(LinkEntity entity) {
        linkDao.updateById(entity);
    }

}
