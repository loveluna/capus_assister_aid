package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */

public interface UserInfoService extends IService<UserInfo> {
    UserInfo lookUserinfo(String userId);

    List<UserInfo> queryAllUserInfo(Integer roleId, Integer userStatus, Integer page, Integer count);

    Integer queryAllUserCount(Integer roleid);

    boolean saveUser(UserInfo userInfo);

    boolean updateUserInfo(UserInfo userInfo);

    UserInfo queryPartInfo(String userId);
}
