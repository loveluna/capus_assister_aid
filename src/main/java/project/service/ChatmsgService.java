package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Article;
import project.entity.UserInfo;
import project.entity.chat.ChatMsg;
import project.mapper.ChatmsgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

public interface ChatmsgService extends IService<ChatMsg> {
    List<UserInfo> getChatHistory(String senduserid, String reciveuserid);
}
