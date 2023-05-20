package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import project.entity.Collect;
import project.entity.chat.ChatMsg;
import project.mapper.CollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  收藏 服务类
 * </p>
 *
 */

public interface CollectService extends IService<Collect> {
    @CacheEvict(value = "collectCache", allEntries = true)
    void insertCollect(Collect collect);

    @Cacheable(value = "collectCache", key = "'queryAllCollect-' + #couserid + '-' + #page + '-' + #count")
    List<Collect> queryAllCollect(String couserid, int page, int count);

    @CacheEvict(value = "collectCache", allEntries = true)
    void updateCollect(Collect collect);

    @CacheEvict(value = "collectCache", allEntries = true)
    void deleteCollect(String collid);

    Collect queryCollectStatus(String couserid, String commid);

    Integer queryCollectCount(String couserid);
}
