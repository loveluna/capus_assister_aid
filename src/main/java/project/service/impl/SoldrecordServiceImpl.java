package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Soldrecord;
import project.mapper.SoldrecordMapper;
import project.service.SoldrecordService;

import java.util.List;

@Service
@Transactional
public class SoldrecordServiceImpl extends ServiceImpl<SoldrecordMapper, Soldrecord> implements SoldrecordService {

    /**
     * 插入售出记录
     *
     * @param soldrecord 售出记录信息
     * @return 是否插入成功
     */
    @Override
    public boolean insertSold(Soldrecord soldrecord) {
        return baseMapper.insert(soldrecord) == 1;
    }

    /**
     * 删除售出记录
     *
     * @param id 售出记录ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSold(String id) {
        UpdateWrapper<Soldrecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("soldstatus", 2)
                .eq("id", id);
        return baseMapper.update(null, updateWrapper) == 1;
    }

    /**
     * 分页展示售出记录
     *
     * @param userId 用户ID
     * @param page   分页页码
     * @param count  分页大小
     * @return 售出记录列表
     */
    @Override
    public List<Soldrecord> queryAllSoldrecord(String userId, Integer page, Integer count) {
        LambdaQueryWrapper<Soldrecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Soldrecord::getId, Soldrecord::getCommid, Soldrecord::getCommname, Soldrecord::getCommdesc, Soldrecord::getThinkmoney, Soldrecord::getSoldtime)
                .eq(Soldrecord::getSoldstatus, 1)
                .eq(userId != null, Soldrecord::getUserid, userId)
                .orderByDesc(Soldrecord::getSoldtime)
                .last("LIMIT " + (page - 1) * count + ", " + count);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查看售出记录总数
     *
     * @param userId 用户ID
     * @return 售出记录总数
     */
    @Override
    public Integer querySoldCount(String userId) {
        QueryWrapper<Soldrecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("soldstatus", 1)
                .eq(userId != null, "userid", userId);
        return baseMapper.selectCount(queryWrapper);
    }
}