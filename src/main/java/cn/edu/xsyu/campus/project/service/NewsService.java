package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;

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
