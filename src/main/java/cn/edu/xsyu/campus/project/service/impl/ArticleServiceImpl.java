package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.config.FileUploadProperties;
import cn.edu.xsyu.campus.project.entity.Article;
import cn.edu.xsyu.campus.project.entity.Comment;
import cn.edu.xsyu.campus.project.entity.Report;
import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.exception.BusinessException;
import cn.edu.xsyu.campus.project.mapper.ArticleMapper;
import cn.edu.xsyu.campus.project.service.ArticleService;
import cn.edu.xsyu.campus.project.service.CommentService;
import cn.edu.xsyu.campus.project.service.ReportService;
import cn.edu.xsyu.campus.project.service.UserInfoService;
import cn.edu.xsyu.campus.project.util.KeyUtil;
import cn.edu.xsyu.campus.project.vo.ArticleRespVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.xsyu.campus.project.entity.Article.Category.*;
import static cn.edu.xsyu.campus.project.exception.code.BaseResponseCode.*;

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

    @Resource
    FileUploadProperties fileUploadProperties;

    @Resource
    private ReportService reportService;

    @Override
    public Map<String, Object> getStudyModuleStatistics() {
        // 构建查询条件
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_type", STUDY_MODULE)
                .eq("status", 1)
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
                .eq("status", 1)
                .eq("category",OTHERS.getValue());

        // 查询符合条件的文章
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Article> getStudySectionByTopic(Integer type) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_type", STUDY_MODULE)
                .eq("status", 1)
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
        Article article = articleMapper.selectOne(Wrappers.<Article>lambdaQuery().eq(Article::getArticleId, articleId).ne(Article::getStatus, 2));
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

    @Override
    public ArticleRespVO  getArticlesWithCagetory(String userId) {
        ArticleRespVO articleRespVO = new ArticleRespVO();
        UserInfo userInfo = userInfoService.getById(userId);
        // 构建查询条件
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_type", LIFE_MODULE)
                .eq("status", 1)
                .in("category", Arrays.asList(TEXT_MODULE.getValue(),
                        IMAGE_MODULE.getValue()));

        // 查询符合条件的文章
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        for (Article article:articleList) {
            List<Comment> comments = commentService.queryComments(article.getArticleId());

            comments = Optional.ofNullable(comments).orElse(Collections.emptyList());
            comments = comments.stream().peek(comment -> {
                UserInfo user = userInfoService.queryPartInfo(comment.getCuserid());
                comment.setCuimage(user.getUimage());
                comment.setCusername(user.getUsername());
            }).collect(Collectors.toList());
            article.setComments(comments);
        }
        articleRespVO.setArticles(articleList);
        articleRespVO.setUserInfo(userInfo);
        return articleRespVO;
    }

    @Override
    public boolean saveArticles(String userId, Article article) {
        UserInfo userInfo = userInfoService.getById(userId);
        if (userInfo == null) {
            return false;
        }
        Gson gson = new Gson();
        List<String> urlList = Arrays.asList(gson.fromJson(article.getArticleImage(), String[].class));
        String imgPath = fileUploadProperties.convertURLPath(urlList);
        article.setArticleImage(imgPath);
        article.setCreateTime(new Date());
        article.setArticleId(KeyUtil.genUniqueKey());
        article.setUserId(userInfo.getUserid());
        article.setStatus(3);
        article.setUserName(userInfo.getUsername());
        article.setUserImage(userInfo.getUimage());
        return articleMapper.insert(article) > 0;
    }

    @Override
    public IPage<Article> queryAllArticles(Integer status, String userId, int page, int count) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq(status != null, "status", status) // 根据文章状态查询
                .ne(status == null, "status", new Integer(2))
                .eq(userId != null, "user_Id", userId); // 根据用户ID查询
        return this.page(new Page<>(page, count), wrapper); // 分页查询
    }


    @Override
    public int queryArticleCount(Integer status, String userId) {
        QueryWrapper<Article> wrapper= new QueryWrapper<>();
        wrapper.eq(status != null, "status", status) // 根据文章状态查询
                .ne(status == null, "status", new Integer(2))
                .eq(userId != null, "user_Id", userId); // 根据用户ID查询
        return this.count(wrapper); // 统计符合条件的文章总数
    }

    @Override
    public boolean updateArticleStatus(String articleId, Integer status) {
        boolean updateStatus = articleMapper.update(null, Wrappers.<Article>lambdaUpdate().set(Article::getStatus, status).eq(Article::getArticleId, articleId)) > 0;
        boolean updateReport = reportService.update(Wrappers.<Report>lambdaUpdate().set(Report::getStatus, status).eq(Report::getReportedId, articleId));
        return updateStatus && updateReport;
    }
}
