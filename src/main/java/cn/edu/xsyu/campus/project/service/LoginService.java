package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Login;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 */

public interface LoginService extends IService<Login> {
    boolean loginAdd(Login login);

    Login userLogin(Login login);

    boolean updateLogin(Login login);
}