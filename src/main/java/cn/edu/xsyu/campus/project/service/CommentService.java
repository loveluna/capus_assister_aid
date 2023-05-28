package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
