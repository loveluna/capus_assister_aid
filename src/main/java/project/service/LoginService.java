package project.service;

import org.springframework.beans.factory.annotation.Value;
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
@Service
@Transactional
public class LoginService {
    @Resource
    private LoginMapper loginMapper;

    private Boolean allowMultipleLogin = true;

    /**注册*/
    public Integer loginAdd(Login login){
        return loginMapper.loginAdd(login);
    }
    /**登录及判断用户是否存在*/
    public Login userLogin(Login login){
        return loginMapper.userLogin(login);
    }
    /**修改登录信息*/
    public Integer updateLogin(Login login){
        return loginMapper.updateLogin(login);
    }
}