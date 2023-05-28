package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  评论回复 服务类
 * </p>
 *
 */

public interface ReplyService  extends IService<Reply> {

    boolean insetReply(Reply reply);

    List<Reply> queryReplys(String cid);

    Reply queryById(String rid);

    boolean deleteReply(Reply reply);
}
