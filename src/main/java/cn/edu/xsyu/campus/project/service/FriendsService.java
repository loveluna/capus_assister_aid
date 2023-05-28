package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.entity.chat.Friends;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;



public interface FriendsService  extends IService<Friends> {
    boolean insertFriend(Friends friends);

    boolean justTwoUserIsFriend(Friends friends);

    List<UserInfo> lookUserFriend(String userId);

    UserInfo lookUserMine(String userId);
}
