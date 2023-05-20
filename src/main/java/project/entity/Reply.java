package project.entity;

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

 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
public class Reply extends BaseEntity implements Serializable {
    /**
     * 回复id
     */
    @TableId
    private String rid;
    /**
     * 评论id
     */
	private String cid;
    /**
     * 商品id
     */
	private String commid;
    /**
     * 被回复用户id
     */
	private String cuserid;
    /**
     * 被回复者昵称
     */
    private String cusername;
    /**
     * 商品发布者id
     */
	private String spuserid;
    /**
     * 回复内容
     */
	private String recontent;
    /**
     * 回复者id
     */
	private String ruserid;
    /**
     * 回复者昵称
     */
    private String rusername;
    /**
     * 回复者用户头像
     */
    private String ruimage;
    /**
     * 回复时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date replytime;
    /**
     * 0异常 1正常 2删除
     */
	private Integer repstatus;


}
