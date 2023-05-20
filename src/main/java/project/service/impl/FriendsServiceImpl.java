package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import project.entity.UserInfo;
import project.entity.chat.Friends;
import project.mapper.FriendsMapper;
import project.mapper.UserInfoMapper;
import project.service.FriendsService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private FriendsMapper friendsMapper;
    /**
     * 添加好友
     *
     * @param friends 好友信息
     * @return 是否添加成功
     */
    @Override
    public boolean insertFriend(Friends friends) {
        return friendsMapper.insert(friends) > 0;
    }

    /**
     * 判断两个用户是否是好友
     *
     * @param friends 好友信息
     * @return 是否是好友
     */
    @Override
    public boolean justTwoUserIsFriend(Friends friends) {
        QueryWrapper<Friends> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", friends.getUserid())
                .eq("fuserid", friends.getFuserid());
        return friendsMapper.selectCount(wrapper) > 0;
    }

    /**
     * 查询用户的好友列表
     *
     * @param userId 用户ID
     * @return 好友列表
     */
    @Override
    public List<UserInfo> lookUserFriend(String userId) {
        LambdaQueryWrapper<Friends> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Friends::getUserid, userId)
                .select(Friends::getFuserid);
        List<String> friendIds = friendsMapper.selectList(wrapper).stream()
                .map(Friends::getFuserid)
                .collect(Collectors.toList());

        if (friendIds.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.in("userid", friendIds)
                .select("userid as id", "username", "uimage as avatar", "sign", "status");
        return userInfoMapper.selectList(userWrapper);
    }

    /**
     * 查询用户的信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfo lookUserMine(String userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId)
                .select("userid as id", "username", "uimage as avatar", "sign", "status");
        return userInfoMapper.selectOne(wrapper);
    }
}