package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Reply;
import project.mapper.ReplyMapper;
import project.service.ReplyService;

import java.util.List;

@Service
@Transactional
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    /**
     * 插入回复
     *
     * @param reply 回复信息
     * @return 是否插入成功
     */
    @Override
    public boolean insetReply(Reply reply) {
        return baseMapper.insert(reply) == 1;
    }

    /**
     * 查询回复
     *
     * @param cid 评论ID
     * @return 回复列表
     */
    @Override
    public List<Reply> queryReplys(String cid) {
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Reply::getRid, Reply::getCid, Reply::getCommid, Reply::getCuserid, Reply::getSpuserid, Reply::getRecontent, Reply::getRuserid, Reply::getReplytime)
                .eq(Reply::getRepstatus, 1)
                .eq(Reply::getCid, cid)
                .orderByDesc(Reply::getReplytime);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询回复中用户信息
     *
     * @param rid 回复ID
     * @return 回复信息
     */
    @Override
    public Reply queryById(String rid) {
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Reply::getRuserid, Reply::getSpuserid)
                .eq(Reply::getRepstatus, 1)
                .eq(Reply::getRid, rid);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 删除回复
     *
     * @param reply 回复信息
     * @return 是否删除成功
     */
    @Override
    public boolean deleteReply(Reply reply) {
        UpdateWrapper<Reply> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("repstatus", 2)
                .eq(reply.getRid() != null, "rid", reply.getRid())
                .eq(reply.getCid() != null, "cid", reply.getCid());
        return baseMapper.update(null, updateWrapper) == 1;
    }
}