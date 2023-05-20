package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.UserInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */

public interface UserInfoService extends IService<UserInfo> {
    UserInfo lookUserinfo(String userId);

    List<UserInfo> queryAllUserInfo(Integer roleid, Integer userstatus, Integer page, Integer count);

    Integer queryAllUserCount(Integer roleid);

    boolean saveUser(UserInfo userInfo);

    boolean updateUserInfo(UserInfo userInfo);

    UserInfo queryPartInfo(String userId);
}
