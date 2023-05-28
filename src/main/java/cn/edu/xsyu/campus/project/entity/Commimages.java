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
public class Commimages extends BaseEntity implements Serializable {
    /**
     * 图片id
     */
    @TableId
    private String id;
    /**
     * 商品id
     */
	private String commid;
    /**
     * 图片
     */
	private String image;
    /**
     * 发布时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createtime;

    /**
     *  图片状态
     */
    private Integer imagestatus;
}
