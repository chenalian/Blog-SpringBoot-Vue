package site.alian.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字段表(Field)实体类
 *
 * @author makejava
 * @since 2021-01-23 10:03:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bl_field")
public class FieldEntity {

    // 主键ID
    private Integer id;
    // 字段名称
    private String field;
    // 字段名称
    private String name;
    // 字段值
    private String value;

}