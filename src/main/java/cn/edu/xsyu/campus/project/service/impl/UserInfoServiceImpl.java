package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.entity.UserRole;
import cn.edu.xsyu.campus.project.mapper.LoginMapper;
import cn.edu.xsyu.campus.project.mapper.UserInfoMapper;
import cn.edu.xsyu.campus.project.service.UserInfoService;
import cn.edu.xsyu.campus.project.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private LoginMapper loginMapper;

    @Resource
    private UserRoleService userRoleService;
    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfo lookUserinfo(String userId) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserid, userId);

        return getOne(queryWrapper);
    }

    /**
     * 查询所有用户信息
     *
     * @param roleId     角色ID
     * @param userStatus 用户状态
     * @param page       当前页码
     * @param count      每页显示数量
     * @return 用户信息列表
     */
    @Override
    public List<UserInfo> queryAllUserInfo(Integer roleId, Integer userStatus, Integer page, Integer count) {
        List<UserInfo> userInfoList = userInfoMapper.selectList(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserstatus, userStatus)
                .orderByDesc(UserInfo::getCreatetime)
                .last("limit " + (page - 1) * count + "," + count));
        userInfoList = userInfoList.stream().peek(userInfo -> {
            List<UserRole> userRoles = userRoleService.getUserRoleIdByUserId(userInfo.getUserid());
            userInfo.setRoleid(userRoles.stream().map(UserRole::getRoleid).collect(Collectors.toList()));
        }).collect(Collectors.toList());

        if (roleId != null) {
            userInfoList = userInfoList.stream().filter(userInfo -> userInfo.getRoleid().contains(roleId)).collect(Collectors.toList());
        }
        return userInfoList;
    }

    /**
     * 查询所有用户总数
     *
     * @param roleId 角色ID
     * @return 所有用户总数
     */
    @Override
    public Integer queryAllUserCount(Integer roleId) {
        List<UserInfo> userInfoList = userInfoMapper.selectList(Wrappers.<UserInfo>lambdaQuery()
                .orderByDesc(UserInfo::getCreatetime));
        userInfoList = Optional.ofNullable(userInfoList).orElse(Collections.emptyList());
        List<Integer> userRoles = userInfoList.stream()
                .flatMap(userInfo -> userRoleService.getUserRoleIdByUserId(userInfo.getUserid()).stream().map(UserRole::getRoleid)).collect(Collectors.toList());

        userInfoList = userInfoList.stream().filter(userInfo -> userRoles.contains(userInfo.getRoleid())).collect(Collectors.toList());

        return userInfoList.size();
    }

    /**
     * 添加用户信息
     *
     * @param userInfo 用户信息
     * @return 是否添加成功
     */
    @Override
    public boolean saveUser(UserInfo userInfo) {
        return save(userInfo);
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 是否修改成功
     */
    @Override
    public boolean updateUserInfo(UserInfo userInfo) {
        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInfo::getUserid, userInfo.getUserid())
                .set(userInfo.getUsername() != null, UserInfo::getUsername, userInfo.getUsername())
                .set(userInfo.getPassword() != null, UserInfo::getPassword, userInfo.getPassword())
                .set(userInfo.getMobilephone() != null, UserInfo::getMobilephone, userInfo.getMobilephone())
                .set(userInfo.getEmail() != null, UserInfo::getEmail, userInfo.getEmail())
                .set(userInfo.getUimage() != null, UserInfo::getUimage, userInfo.getUimage())
                .set(userInfo.getSex() != null, UserInfo::getSex, userInfo.getSex())
                .set(userInfo.getSchool() != null, UserInfo::getSchool, userInfo.getSchool())
                .set(userInfo.getFaculty() != null, UserInfo::getFaculty, userInfo.getFaculty())
                .set(userInfo.getStartime() != null, UserInfo::getStartime, userInfo.getStartime())
                .set(userInfo.getUserstatus() != null, UserInfo::getUserstatus, userInfo.getUserstatus())
                .set(userInfo.getStatus() != null, UserInfo::getStatus, userInfo.getStatus());

        return update(updateWrapper);
    }

    /**
     * 查询用户的昵称和头像
     *
     * @param userId 用户ID
     * @return 用户的昵称和头像
     */
    @Override
    public UserInfo queryPartInfo(String userId) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserInfo::getUsername, UserInfo::getUimage)
                .eq(UserInfo::getUserstatus, 1)
                .eq(UserInfo::getUserid, userId);

        return getOne(queryWrapper);
    }
}