package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import project.entity.Comment;
import project.entity.UserInfo;
import project.exception.BusinessException;
import project.mapper.CommentMapper;
import project.service.CommentService;
import project.service.UserInfoService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static project.exception.code.BaseResponseCode.NOT_ACCESS;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserInfoService userInfoService;
    /**
     * 查询评论
     *
     * @param commid 评论 ID
     * @return 评论列表
     */
    @Override
    public List<Comment> queryComments(String commid) {
        /*
         * 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
         * eq 方法相当于 WHERE 字段名 = #{参数名}
         * orderByDesc 方法相当于 ORDER BY 字段名 DESC
         */
        LambdaQueryWrapper<Comment> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Comment::getCommstatus, 1)
                .eq(Comment::getCommid, commid)
                .orderByDesc(Comment::getCreateTime);
        return commentMapper.selectList(queryWrapper); // 调用 commentMapper 的 selectList 方法执行查询
    }

    /**
     * 查询评论中用户信息 * @param cid 评论 ID
     *
     * @return Comment 对象，包含评论用户 ID 和被评论用户 ID
     */
    @Override
    public Comment queryById(String cid) {
        /*
         * 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
         * select 方法相当于 SELECT 字段名1, 字段名2 ...
         * eq 方法相当于 WHERE 字段名 = #{参数名}
         */
        LambdaQueryWrapper<Comment> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(Comment::getCuserid, Comment::getSpuserid)
                .eq(Comment::getCommstatus, 1)
                .eq(Comment::getCid, cid);
        return commentMapper.selectOne(queryWrapper); // 调用 commentMapper 的 selectOne 方法执行查询
    }

    /**
     * 插入评论
     *
     * @param comment Comment 对象，包含评论信息
     * @return 插入结果，true 表示成功，false 表示失败
     */
    @Override
    public boolean insertComment(Comment comment) {
        return commentMapper.insert(comment) > 0; // 调用 commentMapper 的 insert 方法执行插入操作
    }

    /**
     * 删除评论
     *
     * @param cid 评论 ID
     * @return 删除结果，true 表示成功，false 表示失败
     */
    @Override
    public boolean deleteComment(String cid) {
        Comment comment = new Comment();
        comment.setCommstatus(2); // 设置要更新的字段
        LambdaQueryWrapper<Comment> queryWrapper = Wrappers.lambdaQuery(); // 创建一个 LambdaQueryWrapper 对象
        queryWrapper.eq(Comment::getCid, cid); // 相当于 WHERE cid = #{cid}
        return commentMapper.update(comment, queryWrapper) > 0; // 调用 commentMapper 的 update 方法执行更新操作
    }

    @Override
    public void addComments(String userId, Comment comment) {
        UserInfo userInfo = Optional.ofNullable(userInfoService.getById(userId)).orElseThrow(()->new BusinessException(NOT_ACCESS));
        comment.setCuserid(userInfo.getUserid());
        comment.setCuimage(userInfo.getUimage());
        comment.setCusername(userInfo.getUsername());
        comment.setCommstatus(1);
        commentMapper.insert(comment);
    }
}
