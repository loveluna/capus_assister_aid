package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.Notices;
import cn.edu.xsyu.campus.project.mapper.NoticesMapper;
import cn.edu.xsyu.campus.project.service.NoticesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoticesServiceImpl extends ServiceImpl<NoticesMapper, Notices> implements NoticesService {

    /**
     * 插入通知
     *
     * @param notices 通知信息
     * @return 是否插入成功
     */
    @Override
    public boolean insertNotices(Notices notices) {
        return baseMapper.insert(notices) == 1;
    }

    /**
     * 用户已读通知消息
     *
     * @param id 通知ID
     * @return 是否修改成功
     */
    @Override
    public boolean updateNoticesById(String id) {
        UpdateWrapper<Notices> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("isread", 1)
                .eq("id", id);
        return baseMapper.update(null, updateWrapper) == 1;
    }

    /**
     * 查询前10条通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    @Override
    public List<Notices> queryNotices(String userId) {
        LambdaQueryWrapper<Notices> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Notices::getId, Notices::getTpname, Notices::getWhys, Notices::getIsread, Notices::getNttime, Notices::getLatest)
                .eq(Notices::getUserid, userId)
                .orderByDesc(Notices::getNttime)
                .last("LIMIT 0, 10");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 取消新通知标志
     *
     * @param userId 用户ID
     * @return 是否取消成功
     */
    @Override
    public boolean cancelLatest(String userId) {
        UpdateWrapper<Notices> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("latest", 2)
                .eq("userid", userId);
        return baseMapper.update(null, updateWrapper) == 1;
    }

    /**
     * 分页查询用户所有通知消息
     *
     * @param userId 用户ID
     * @param page   分页页码
     * @param count  分页大小
     * @return 通知列表
     */
    @Override
    public List<Notices> queryAllNotices(String userId, Integer page, Integer count) {
        LambdaQueryWrapper<Notices> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Notices::getId, Notices::getTpname, Notices::getWhys, Notices::getIsread, Notices::getNttime)
                .eq(Notices::getUserid, userId)
                .orderByDesc(Notices::getNttime)
                .last("LIMIT " + (page - 1) * count + ", " + count);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询用户所有通知消息的数量
     *
     * @param userId 用户ID
     * @return 通知数量
     */
    @Override
    public Integer queryNoticesCount(String userId) {
        QueryWrapper<Notices> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(*)")
                .eq("userid", userId);
        return baseMapper.selectCount(queryWrapper);
    }
}