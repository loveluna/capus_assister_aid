package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Reply;
import project.entity.UserInfo;
import project.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
