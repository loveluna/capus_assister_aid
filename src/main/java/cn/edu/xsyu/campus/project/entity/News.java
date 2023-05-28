package cn.edu.xsyu.campus.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
public class News extends BaseEntity implements Serializable {
    /**
     * 新闻id
     */
    @TableId
    private String id;
    /**
     * 新闻标题
     */
	private String newstitle;
    /**
     * 新闻简介
     */
	private String newsdesc;
    /**
     * 新闻内容
     */
	private String newscontent;
    /**
     * 发布时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createtime;
    /**
     * 新闻发布者
     */
	private String username;
    /**
     * 图片
     */
    private String image;
    /**
     * 1正常  2删除
     */
	private Integer newsstatus;
    /**
     * 浏览量
     */
	private Integer rednumber;


}
