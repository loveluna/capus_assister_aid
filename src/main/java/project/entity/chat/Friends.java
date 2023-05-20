package project.entity.chat;

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

    private static final long serialVersionUID = 1L;

    /**
     * 好友表id
     */
	private String id;
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
	private Date addtime;


}
