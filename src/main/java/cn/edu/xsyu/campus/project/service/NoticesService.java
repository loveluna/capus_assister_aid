package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Notices;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  消息通知服务类
 * </p>
 *
 */

public interface NoticesService extends IService<Notices> {
    boolean insertNotices(Notices notices);

    boolean updateNoticesById(String id);

    List<Notices> queryNotices(String userId);

    boolean cancelLatest(String userId);

    List<Notices> queryAllNotices(String userId, Integer page, Integer count);

    Integer queryNoticesCount(String userId);
}
