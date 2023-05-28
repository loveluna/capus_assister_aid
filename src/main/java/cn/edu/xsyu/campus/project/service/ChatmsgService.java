package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.entity.chat.ChatMsg;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ChatmsgService extends IService<ChatMsg> {
    List<UserInfo> getChatHistory(String senduserid, String reciveuserid);
}
