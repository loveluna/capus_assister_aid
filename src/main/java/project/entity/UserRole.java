package project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
public class UserRole extends BaseEntity implements Serializable {
	private String userid;
    /**
     * 1普通用户 2管理员 3超级管理员
     */
	private Integer roleid;
    /**
     * 身份
     */
	private String identity;


}
