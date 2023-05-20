package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Collect;
import project.entity.Comment;
import project.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  评论 服务类
 * </p>
 *
 */
@Service
@Transactional
public interface CommentService extends IService<Comment> {
    List<Comment> queryComments(String commid);

    Comment queryById(String cid);

    boolean insertComment(Comment comment);

    boolean deleteComment(String cid);


    void addComments(String userId, Comment comment);
}
