package site.alian.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类表(BlType)实体类
 *
 * @author makejava
 * @since 2021-01-23 10:03:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bl_type")
public class TypeEntity {

    // 主键ID
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    // 分类名称
    private String name;
    // 分类创建时间
    private Long createTime;
    // 分类修改时间
    private Long updateTime;

    // 当前分类下的博客数量
    @TableField(exist = false)
    private int count;

}