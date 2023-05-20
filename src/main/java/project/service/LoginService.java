package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Value;
import project.entity.Commodity;
import project.entity.Login;
import project.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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