package cn.edu.xsyu.campus.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 举报实体类
 */
@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
@Data
@Accessors(chain = true)//链式写法
public class Report extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 举报 ID
     */
    @TableId
    private String id;
    
    /**
     * 举报类型，可以是 "article" 或 "product"
     */
    private String type;
    
    /**
     * 被举报的文章或货物的 ID
     */
    private String reportedId;
    
    /**
     * 举报人的 ID
     */
    private String reporterId;
    
    /**
     * 举报原因
     */
    private String reason;
    
    /**
     * 举报时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date reportTime;
    
    /**
     * 举报状态，0 表示未处理，1 表示已处理
     */
    private Integer status;

    private String appealContent;


    /**
     * 申诉状态，0 表示未申诉，1 表示已申诉
     */
    private Integer appealStatus;

    /**
     * 申诉结果，0 表示未处理，1 表示已处理，2 表示申诉无效
     */
    private Integer appealResult;
}