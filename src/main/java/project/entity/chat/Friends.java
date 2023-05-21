package project.entity.chat;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import project.entity.BaseEntity;

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
