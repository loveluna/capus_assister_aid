package cn.edu.xsyu.campus.project.entity.chat;

import cn.edu.xsyu.campus.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
@TableName("chatmsg")
public class ChatMsg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发送者id
     */
    @TableId
	private String senduserid;
    /**
     * 接收者id
     */
	private String reciveuserid;
    /**
     * 发送内容
     */
	private String content;
    /**
     * 发送时间
     */
	private Date sendtime;
    /**
     * 消息类型
     */
	private String msgtype;


}
