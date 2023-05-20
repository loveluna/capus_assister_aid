package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Soldrecord;
import project.entity.UserInfo;
import project.mapper.SoldrecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */

public interface SoldrecordService extends IService<Soldrecord> {
    boolean insertSold(Soldrecord soldrecord);

    boolean deleteSold(String id);

    List<Soldrecord> queryAllSoldrecord(String userId, Integer page, Integer count);

    Integer querySoldCount(String userId);
}
