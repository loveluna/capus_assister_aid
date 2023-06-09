package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.UserRole;
import cn.edu.xsyu.campus.project.mapper.UserRoleMapper;
import cn.edu.xsyu.campus.project.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    /**
     * 插入角色
     *
     * @param userRole 用户角色信息
     * @return 是否插入成功
     */
    @Override
    public boolean insertUserRole(UserRole userRole) {
        return baseMapper.insert(userRole) == 1;
    }

    /**
     * 查询用户角色ID
     *
     * @param userId 用户ID
     * @return 用户角色ID
     */
    @Override
    public List<UserRole> getUserRoleIdByUserId(String userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserid, userId);
        List<UserRole> userRoles = baseMapper.selectList(queryWrapper);
        return userRoles;
    }

    /**
     * 修改用户角色
     *
     * @param userRole 用户角色信息
     * @return 是否修改成功
     */
    @Override
    public boolean updateUserRole(UserRole userRole) {
        UpdateWrapper<UserRole> updateWrapper =new UpdateWrapper<>();
        updateWrapper.set("roleid", userRole.getRoleid())
                .eq("userid", userRole.getUserid());
        return baseMapper.update(null, updateWrapper) == 1;
    }
}
