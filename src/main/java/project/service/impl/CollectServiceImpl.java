package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Collect;
import project.mapper.CollectMapper;
import project.service.CollectService;

import java.util.List;

@Service
@Transactional
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    @Override
    @CacheEvict(value = "collectCache", allEntries = true)
    public void insertCollect(Collect collect) {
        save(collect);
    }

    @Override
    @Cacheable(value = "collectCache", key = "'queryAllCollect-' + #couserid + '-' + #page + '-' + #count")
    public List<Collect> queryAllCollect(String couserid, int page, int count) {
        // 计算起始行号
        int start = (page - 1) * count;

        return lambdaQuery()
                .eq(Collect::getCollstatus, 1)
                .eq(Collect::getCouserid, couserid)
                .orderByDesc(Collect::getSoldtime)
                .last("limit " + start + ", " + count)
                .list();
    }

    @Override
    @CacheEvict(value = "collectCache", allEntries = true)
    public void updateCollect(Collect collect) {
        lambdaUpdate()
                .eq(Collect::getId, collect.getId())
                .set(Collect::getCollstatus, collect.getCollstatus())
                .set(Collect::getSoldtime, collect.getSoldtime())
                .update();
    }

    @Override
    @CacheEvict(value = "collectCache", allEntries = true)
    public void deleteCollect(String collid) {
        lambdaUpdate()
                .eq(Collect::getId, collid)
                .set(Collect::getCollstatus, 0)
                .update();
    }

    /**
     * 查询商品是否被用户收藏
     * @param couserid 用户ID
     * @param commid 商品ID
     * @return 如果用户收藏了该商品，则返回对应的收藏记录，否则返回null
     */
    @Override
    public Collect queryCollectStatus(String couserid, String commid) {
        // 构建查询条件
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("couserid", couserid)  // 用户ID符合条件
                .eq("commid", commid)  // 商品ID符合条件
                .select("couserid", "collstatus");  // 只查询用户ID和收藏状态字段

        // 执行查询
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 查询我的收藏的总数
     * @param couserid 用户ID
     * @return 我的收藏总数
     */
    @Override
    public Integer queryCollectCount(String couserid) {
        // 构建查询条件
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("collstatus", 1)  // 等于1表示收藏状态有效
                .eq("couserid", couserid);  // 用户ID符合条件

        // 执行查询并返回结果
        return baseMapper.selectCount(wrapper);
    }
}
