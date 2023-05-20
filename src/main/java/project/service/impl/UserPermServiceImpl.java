package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.UserPerm;
import project.mapper.UserPermMapper;
import project.service.UserPermService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserPermServiceImpl extends ServiceImpl<UserPermMapper, UserPerm> implements UserPermService {
    @Resource
    private UserPermMapper userPermMapper;
    /**
     * 查询用户权限
     *
     * @param roleId 角色ID
     * @return 用户权限列表
     */
    @Override
    public List<String> lookPermsByUserid(Integer roleId) {
        LambdaQueryWrapper<UserPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserPerm::getPerms)
                .eq(UserPerm::getRoleid, roleId);
        List<UserPerm> userPerms = userPermMapper.selectList(queryWrapper);
        List<String> permsList = new ArrayList<>();
        for (UserPerm userPerm : userPerms) {
            permsList.add(userPerm.getPerms());
        }
        return permsList;
    }
}