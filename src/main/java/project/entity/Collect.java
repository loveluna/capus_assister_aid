package project.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
@Data
@Accessors(chain = true)//链式写法
@ApiModel(value = "goods",description = "商品信息")
public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Integer colloperate;
}
