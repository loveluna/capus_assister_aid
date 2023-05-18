package project.service;

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
public class CommentService {
    @Resource
    private CommentMapper commentMapper;

    /**插入评论*/
    public Integer insertComment(Comment comment){
        return commentMapper.insertComment(comment);
    }
    /**查询评论*/
    public List<Comment> queryComments(String commid){
        return commentMapper.queryComments(commid);
    }
    /**查询评论中用户信息*/
    public Comment queryById(String cid){
        return commentMapper.queryById(cid);
    }
    /**删除评论*/
    public Integer deleteComment(String cid){
        return commentMapper.deleteComment(cid);
    }
}
