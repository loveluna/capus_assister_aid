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
public class Soldrecord extends BaseEntity implements Serializable {
    /**
     * 售出记录id
     */
    @TableId
    private String id;
    /**
     * 商品id
     */
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
     * 售价
     */
    private BigDecimal thinkmoney;
    /**
     * 售出时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date soldtime;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 1正常 2删除
     */
    private Integer soldstatus;
}
