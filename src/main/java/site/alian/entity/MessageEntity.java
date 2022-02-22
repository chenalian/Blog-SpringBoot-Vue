package site.alian.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 留言表(BlMessage)实体类
 *
 * @author makejava
 * @since 2021-01-23 10:03:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bl_message")
public class MessageEntity {

    // 主键ID
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    // 父级ID
    private String parent;
    // 用户名称
    private String name;
    // 用户邮箱
    private String email;
    // 留言内容
    private String content;
    // 留言时间
    private Long createTime;

    // 当前留言下的子留言列表
    @TableField(exist = false)
    private List<MessageEntity> children = new ArrayList<>();

}