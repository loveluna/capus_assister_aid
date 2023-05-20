package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Commimages;
import project.mapper.CommimagesMapper;
import project.service.CommimagesService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommimagesServiceImpl extends ServiceImpl<CommimagesMapper, Commimages> implements CommimagesService {
    @Resource
    private CommimagesMapper commimagesMapper;
    /**
     * 插入商品的其他图
     * @param commimagesList 商品其他图列表
     */
    @Override
    public void insertGoodImages(List<Commimages> commimagesList) {
        // 使用 LambdaQueryWrapper 构建插入查询
        LambdaQueryWrapper<Commimages> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Commimages::getCommid, commimagesList.stream().map(Commimages::getCommid).distinct().toArray())
                .eq(Commimages::getImagestatus, 1)
                .select(Commimages::getCommid);
        // 查询插入前已有的商品其他图的商品 ID，并将其移除
        List<Object> resultList = commimagesMapper.selectObjs(queryWrapper);
        List<String> commidList = resultList.stream().map(Object::toString).collect(Collectors.toList());
        commimagesList.removeIf(commimages -> commidList.contains(commimages.getCommid()));

        // 批量插入商品其他图
        saveBatch(commimagesList);
    }

    /**
     * 查询某个商品的其他图
     * @param commid 商品 ID
     * @return 商品其他图列表
     */
    @Override
    public List<String> findImagesByCommId(String commid) {
        // 使用 LambdaQueryWrapper 构建查询
        LambdaQueryWrapper<Commimages> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Commimages::getCommid, commid)
                .eq(Commimages::getImagestatus, 1)
                .select(Commimages::getImage);
        List<Object> resultList = commimagesMapper.selectObjs(queryWrapper);
        return resultList.stream().map(Object::toString).collect(Collectors.toList());
    }

    /**
     * 删除某个商品的其他图
     * @param commid 商品 ID
     */
    @Override
    public void delGoodImages(String commid) {
        // 使用 LambdaQueryWrapper 构建更新查询
        LambdaQueryWrapper<Commimages> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Commimages::getCommid, commid)
                .eq(Commimages::getImagestatus, 1);
        // 使用 LambdaUpdateWrapper 构建更新操作
        LambdaUpdateWrapper<Commimages> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Commimages::getImagestatus,2)
                .apply(queryWrapper.getSqlSegment());
        update(updateWrapper);
    }

}
