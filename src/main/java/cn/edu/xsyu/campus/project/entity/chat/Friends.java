package cn.edu.xsyu.campus.project.entity.chat;

import cn.edu.xsyu.campus.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
public class Friends extends BaseEntity implements Serializable {
    /**
     * 好友表id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
	private String userid;
    /**
     * 好友id
     */
	private String fuserid;
    /**
     * 时间
     */
    @TableField(fill = FieldFill.INSERT)
	private Date createDate;


}
