package project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
public class Login extends BaseEntity implements Serializable {
    /**
     * 登录id
     */
    @TableId
    private String id;
    /**
     * 用户id
     */
	private String userid;
    /**
     * 角色id 1普通用户 2管理员 3超级管理员
     */
	private Integer roleid;
    /**
     * 用户名
     */
	private String username;
    /**
     * 用户密码
     */
	private String password;
    /**
     * 手机号
     */
    private String mobilephone;
    /**
     * 1正常 0封号
     */
	private Integer userstatus;
    /**
     * 验证码
     * */
    @TableField(exist = false)
    private String vercode;


}
