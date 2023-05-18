package project.service;

import project.entity.UserInfo;
import project.entity.chat.ChatMsg;
import project.mapper.ChatmsgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChatmsgService {
    @Resource
    ChatmsgMapper chatmsgMapper;

    /**插入发送的消息记录*/
    @Async
    public void insertChatmsg(ChatMsg chatmsg){
        chatmsgMapper.insertChatmsg(chatmsg);
    }

    /**查询聊天记录*/
    public List<UserInfo> LookChatMsg(ChatMsg chatMsg){
        return chatmsgMapper.LookChatMsg(chatMsg);
    }
}
