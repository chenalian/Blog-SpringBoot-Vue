package site.alian.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 友链表(BlLink)实体类
 *
 * @author makejava
 * @since 2021-01-23 10:03:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bl_link")
public class LinkEntity {

    // 主键ID
    private Integer id;
    // 网站名称
    private String name;
    // 网站地址URL
    private String url;

}