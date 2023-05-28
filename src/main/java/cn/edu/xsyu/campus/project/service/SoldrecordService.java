package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Soldrecord;
import com.baomidou.mybatisplus.extension.service.IService;

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
