package site.alian.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客表(Blog)实体类
 *     教训：以后不要用关键子类似desc、release做字段名，每次使用都要``很烦的
 * @author makejava
 * @since 2021-01-23 10:03:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bl_blog")
public class BlogEntity {

    // 主键ID
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    // 博客分类ID
    private String typeId;
    // 博客标题
    private String title;
    // 博客略缩图URL地址
    private String imgUrl;
    // 博客内容
    private String content;
    // 博客描述@Column
    @TableField("`desc`")  // 防止识别关键字报错
    private String desc;
    // 原创标识，1为原创
    private Integer origin;
    // 推荐标识，1为推荐
    private Integer commend;
    // 发布标识，1为发布
    @TableField("`release`")  // 防止识别关键字报错
    private Integer release;
    // 赞赏标识，1为赞赏
    private Integer reward;
    // 博客浏览次数
    private Integer viewCount;
    // 博客发布时间
    private Long createTime;
    // 博客修改时间
    private Long updateTime;

    // 数据库中不存在该属性-分类名称
    @TableField(exist = false)
    private String typeName;

}