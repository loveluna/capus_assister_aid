package project.service;

import project.entity.UserRole;
import project.mapper.UserRoleMapper;
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
public class UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    /**插入角色*/
    public Integer InsertUserRole(UserRole userRole){
        return userRoleMapper.InsertUserRole(userRole);
    }
    /**查询角色id*/
    public Integer LookUserRoleId(String userid){
        return userRoleMapper.LookUserRoleId(userid);
    }
    /**修改用户的角色*/
    public void UpdateUserRole(UserRole userRole){
        userRoleMapper.UpdateUserRole(userRole);
    }
}
