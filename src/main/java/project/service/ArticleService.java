package project.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import project.entity.Article;
import project.entity.Collect;
import project.entity.UserInfo;
import project.entity.chat.ChatMsg;
import project.mapper.ChatmsgMapper;
import project.vo.ArticleRespVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 文章 Mapper
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
public interface ArticleService  extends IService<Article> {
    Map<String, Object> getStudyModuleStatistics();

    List<Article> getStudyOtherModuleStatistics();

    List<Article> getStudySectionByTopic(Integer type);

    ArticleRespVO selectCommentsByArticleId(String articleId, String userId);

    boolean updateViewsById(String articleId);

    ArticleRespVO  getArticlesWithCagetory(String userId);

    boolean saveArticles(String userId, Article article);
}
