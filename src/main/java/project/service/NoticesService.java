package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.News;
import project.entity.Notices;
import project.mapper.NoticesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
