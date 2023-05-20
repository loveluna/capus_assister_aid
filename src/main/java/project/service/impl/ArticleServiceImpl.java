package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import project.entity.Article;
import project.entity.Comment;
import project.entity.UserInfo;
import project.exception.BusinessException;
import project.mapper.ArticleMapper;
import project.service.ArticleService;
import project.service.CommentService;
import project.service.UserInfoService;
import project.vo.ArticleRespVO;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static project.entity.Article.Category.*;
import static project.exception.code.BaseResponseCode.NOT_ACCESS;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private static final String LIFE_MODULE = "1";
    private static final String STUDY_MODULE = "0";
    @Resource
    ArticleMapper articleMapper;

    @Resource
    CommentService commentService;

    @Resource
    UserInfoService userInfoService;

    @Override
    public Map<String, Object> getStudyModuleStatistics() {
        // 构建查询条件
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_type", STUDY_MODULE)
                .eq("status", 0)
                .in("category", Arrays.asList(QUESTIONS.getValue(),
                        TECHNICAL_SHARING.getValue(),
                        EVENTS.getValue()));

        // 查询符合条件的文章
        List<Article> articleList = articleMapper.selectList(queryWrapper);

        // 统计数据
        Map<Integer, Integer> numberCountMap = articleList.stream()
                .filter(article -> Arrays.asList(QUESTIONS.getValue(),
                                TECHNICAL_SHARING.getValue(),
                                EVENTS.getValue())
                        .contains(article.getCategory()))
                .collect(Collectors.groupingBy(Article::getCategory, Collectors.summingInt(article -> 1)));

        Map<Integer, Integer> viewCountMap = articleList.stream()
                .filter(article -> Arrays.asList(QUESTIONS.getValue(),
                                TECHNICAL_SHARING.getValue(),
                                EVENTS.getValue())
                        .contains(article.getCategory()))
                .collect(Collectors.groupingBy(Article::getCategory, Collectors.summingInt(Article::getViewsCount)));

        // 返回统计结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("questionsNumberCount", numberCountMap.getOrDefault(QUESTIONS.getValue(), 0));
        resultMap.put("questionsViewCount", viewCountMap.getOrDefault(QUESTIONS.getValue(), 0));
        resultMap.put("technicalSharingNumberCount", numberCountMap.getOrDefault(TECHNICAL_SHARING.getValue(), 0));
        resultMap.put("technicalSharingViewCount", viewCountMap.getOrDefault(TECHNICAL_SHARING.getValue(), 0));
        resultMap.put("eventsNumberCount", numberCountMap.getOrDefault(EVENTS.getValue(), 0));
        resultMap.put("eventsViewCount", viewCountMap.getOrDefault(EVENTS.getValue(), 0));
        resultMap.put("mainSubjectTotals", articleList.size());
        return resultMap;
    }

    @Override
    public List<Article> getStudyOtherModuleStatistics() {
        // 构建查询条件
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_type", STUDY_MODULE)
                .eq("status", 0)
                .eq("category",OTHERS.getValue());

        // 查询符合条件的文章
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Article> getStudySectionByTopic(Integer type) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_type", STUDY_MODULE)
                .eq("status", 0)
                .eq("category", type);
        return articleMapper.selectList(queryWrapper);
    }

    /**
     * 根据文章ID查询该文章的所有评论
     * @param articleId 文章ID
     * @return 包含该文章的所有评论的Map，key为文章ID，value为评论列表
     */
    @Override
    public ArticleRespVO selectCommentsByArticleId(String articleId, String userId) {

        UserInfo userInfo = Optional.ofNullable(userInfoService.getById(userId)).orElseThrow(()->new BusinessException(NOT_ACCESS));
        ArticleRespVO articleRespVO = new ArticleRespVO();
        Article article = articleMapper.selectOne(Wrappers.<Article>lambdaQuery().eq(Article::getArticleId, articleId));
        List<Comment> comments = commentService.queryComments(articleId);

        comments = Optional.ofNullable(comments).orElse(Collections.emptyList());
        comments = comments.stream().peek(comment -> {
            UserInfo user = userInfoService.queryPartInfo(comment.getCuserid());
            comment.setCuimage(user.getUimage());
            comment.setCusername(user.getUsername());
        }).collect(Collectors.toList());
        article.setComments(comments);
        articleRespVO.setArticle(article);
        articleRespVO.setUserInfo(userInfo);
        return articleRespVO;
    }

    @Override
    public boolean updateViewsById(String articleId) {
        UpdateWrapper<Article> wrapper = new UpdateWrapper<>();
        wrapper.eq("article_id", articleId);
        wrapper.setSql("views_count = views_count + 1");
        return update(wrapper);
    }
}
