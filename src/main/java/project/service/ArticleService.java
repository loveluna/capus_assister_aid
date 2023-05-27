package project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Article;
import project.vo.ArticleRespVO;

import java.util.List;
import java.util.Map;

/**
 * 文章 Mapper
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
public interface ArticleService extends IService<Article> {
    Map<String, Object> getStudyModuleStatistics();

    List<Article> getStudyOtherModuleStatistics();

    List<Article> getStudySectionByTopic(Integer type);

    ArticleRespVO selectCommentsByArticleId(String articleId, String userId);

    boolean updateViewsById(String articleId);

    ArticleRespVO getArticlesWithCagetory(String userId);

    boolean saveArticles(String userId, Article article);


    IPage<Article> queryAllArticles(Integer status, String userId, int page, int count);

    int queryArticleCount(Integer status, String userId);

    boolean updateArticleStatus(String articleId, Integer status);
}
