package cn.edu.xsyu.campus.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "goods",description = "商品信息")
public class Collect extends BaseEntity implements Serializable {
    /**
     * 收藏id
     */
    @ApiModelProperty(value = "收藏id",name = "id")
    private String id;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id",name = "commid")
    private String commid;
    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名",name = "commname")
    private String commname;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述",name = "commdesc")
    private String commdesc;
    /**
     * 收藏时间
     */
    @ApiModelProperty(value = "收藏时间",name = "soldtime")
    @TableField(fill = FieldFill.INSERT)
    private Date soldtime;
    /**
     * 0失效 1正常 2删除
     */
    @ApiModelProperty(value = "收藏状态 0失败 1正常 2删除",name = "collstatus")
    private Integer collstatus;
    /**
     * 商品用户id
     */
    @ApiModelProperty(value = "商品用户id",name = "cmuserid")
    private String cmuserid;
    /**
     * 商品用户名
     */
    @ApiModelProperty(value = "商品用户名",name = "username")
    private String username;
    /**
     * 商品所在学校
     */
    private String school;
    /**
     * 收藏用户id
     */
    private String couserid;
    /**
     * 收藏操作：收藏or取消收藏
     */
    @TableField(exist = false)
    private Integer colloperate;
}
