package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import project.entity.UserInfo;
import project.entity.chat.ChatMsg;
import project.mapper.ChatmsgMapper;
import project.service.ChatmsgService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatmsgServiceImpl extends ServiceImpl<ChatmsgMapper, ChatMsg> implements ChatmsgService {

    @Resource
    private ChatmsgMapper chatmsgMapper;

    @Override
    public boolean save(ChatMsg chatMsg) {
        return super.save(chatMsg);
    }

    @Override
    public List<UserInfo> getChatHistory(String senduserid, String reciveuserid) {
        QueryWrapper<ChatMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("senduserid", senduserid)
                        .eq("reciveuserid", reciveuserid))
                .or(wrapper -> wrapper.eq("senduserid", reciveuserid)
                        .eq("reciveuserid", senduserid))
                .orderByAsc("sendtime");
        List<ChatMsg> chatMsgList = chatmsgMapper.selectList(queryWrapper);
        List<UserInfo> userInfoList = new ArrayList<>();
        for (ChatMsg chatMsg : chatMsgList) {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(chatMsg, userInfo);
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }
}