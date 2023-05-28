package cn.edu.xsyu.campus.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
public class UserPerm extends BaseEntity implements Serializable {
    /**
     * 1普通用户 2管理员 3超级管理员
     */
	private Integer roleid;
    /**
     * 对应权限
     */
	private String perms;
    /**
     * 权限解释
     */
	private String mean;


}
