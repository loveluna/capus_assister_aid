package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Commodity;
import project.entity.UserInfo;
import project.entity.chat.Friends;
import project.mapper.FriendsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;



public interface FriendsService  extends IService<Friends> {
    boolean insertFriend(Friends friends);

    boolean justTwoUserIsFriend(Friends friends);

    List<UserInfo> lookUserFriend(String userId);

    UserInfo lookUserMine(String userId);
}
