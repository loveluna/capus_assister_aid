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
public class Notices extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知id
     */
    @TableId
    private String id;
    /**
     * 用户id
     */
	private String userid;
    /**
     * 通知内容
     */
	private String whys;
    /**
     * 是否阅读 0未阅读 1已阅读
     */
	private Integer isread;
    /**
     * 通知类型
     */
	private String tpname;
    /**
     * 通知时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date nttime;
    /**
     * 是否为新通知 1是 2不是
     */
    private Integer latest;

}
