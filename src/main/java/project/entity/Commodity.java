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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class Commodity extends BaseEntity implements Serializable {
    /**
     * 商品id
     */
    @TableId
    private String commid;
    /**
     * 商品名
     */
    private String commname;
    /**
     * 商品描述
     */
    private String commdesc;
    /**
     * 视频
     */
    private String videourl;
    /**
     * 原价
     */
    private BigDecimal orimoney;
    /**
     * 售价
     */
    private BigDecimal thinkmoney;
    /**
     * 商品所在学校
     */
    private String school;
    /**
     * 发布时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createtime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatetime;
    /**
     * 结束时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date endtime;
    /**
     * 0违规 1正常 2删除  3待审核
     */
    private Integer commstatus;
    /**
     * 常用选项：自提，可小刀，不议价等选项
     */
    private String common;
    /**
     * 常用类别字段
     * */
    @TableField(exist = false)
    private String common2;
    /**
     * 商品其他图集合
     * */
    @TableField(exist = false)
    private List<String> otherimg;

    /**
     * 浏览量
     */
    private Integer rednumber;
    /**
     * 商品类别
     */
    private String category;
    /**
     * 简介图
     */
    private String image;
    /**
     * 用户id
     */
    private String userid;

}
