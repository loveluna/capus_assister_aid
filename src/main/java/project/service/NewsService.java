package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.News;
import project.entity.UserInfo;
import project.mapper.NewsMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */

public interface NewsService extends IService<News> {

    boolean insertNews(News news);

    boolean deleteNews(String id);

    boolean updateNews(News news);

    News queryNewsById(String id);

    boolean addNewsRednumber(String id);

    List<News> queryAllNews(Integer page, Integer count);

    Integer LookNewsCount();

    List<News> queryNews();
}
