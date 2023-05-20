package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Login;
import project.mapper.LoginMapper;
import project.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements LoginService {

    /**
     * 注册新用户
     *
     * @param login 登录信息
     * @return 是否注册成功
     */
    @Override
    public boolean loginAdd(Login login) {
        return baseMapper.insert(login) > 0;
    }

    /**
     * 用户登录及检查用户是否存在
     *
     * @param login 登录信息
     * @return 登录结果
     */
    @Override
    public Login userLogin(Login login) {
        LambdaQueryWrapper<Login> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(login.getUserid() != null, Login::getUserid, login.getUserid())
                .eq(login.getUsername() != null, Login::getUsername, login.getUsername())
                .eq(login.getMobilephone() != null, Login::getMobilephone, login.getMobilephone())
                .eq(login.getPassword() != null, Login::getPassword, login.getPassword())
                .eq(Login::getUserstatus, 1);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 修改登录信息
     *
     * @param login 登录信息
     * @return 是否修改成功
     */
    @Override
    public boolean updateLogin(Login login) {
        UpdateWrapper<Login> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", login.getId())
                .or().eq("userid", login.getUserid())
                .set(login.getRoleid() != null, "roleid", login.getRoleid())
                .set(login.getUsername() != null, "username", login.getUsername())
                .set(login.getPassword() != null, "password", login.getPassword())
                .set(login.getMobilephone() != null, "mobilephone", login.getMobilephone())
                .set(login.getUserstatus() != null, "userstatus", login.getUserstatus());
        return baseMapper.update(null, wrapper) > 0;
    }
}