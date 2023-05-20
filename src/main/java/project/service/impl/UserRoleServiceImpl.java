package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.UserRole;
import project.mapper.UserRoleMapper;
import project.service.UserRoleService;

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
    public Integer lookUserRoleId(String userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserRole::getRoleid)
                .eq(UserRole::getUserid, userId);
        UserRole userRole = baseMapper.selectOne(queryWrapper);
        return userRole != null ? userRole.getRoleid() : null;
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
