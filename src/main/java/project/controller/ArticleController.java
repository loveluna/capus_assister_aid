package project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import project.entity.Article;
import project.entity.Comment;
import project.entity.Report;
import project.service.ArticleService;
import project.service.CommentService;
import project.service.ReportService;
import project.util.DataResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/**
 * 文章
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
@RestController
@RequestMapping("/article")
@Api(tags = "文章数据")
public class ArticleController {
    @Resource
    ArticleService articleService;
    @Resource
    CommentService commentService;

    @Resource
    private ReportService reportService;

    @GetMapping("/collaboration/{articleId}")
    @ApiOperation(value = "获取文章数据接口")
    public DataResult getCollaborationInfo(@PathVariable("articleId") String articleId, HttpSession session) {
        String userId = (String) session.getAttribute("userid");
        DataResult result = DataResult.success();
        result.setData(articleService.selectCommentsByArticleId(articleId, userId));
        articleService.updateViewsById(articleId);
        return result;
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加评论")
    public DataResult addComments(@RequestBody Comment comment, HttpSession session) {
        String userId = (String) session.getAttribute("userid");
        commentService.addComments(userId, comment);
        return DataResult.success();
    }

    @GetMapping("/assistance")
    @ApiOperation(value = "获取生活互助数据接口")
    public DataResult getAssistanceInfo(HttpSession session) {
        String userId = (String) session.getAttribute("userid");
        DataResult result = DataResult.success();
        result.setData(articleService.getArticlesWithCagetory(userId));
        return result;
    }


    @PostMapping("/report")
    @ApiOperation(value = "举报文章")
    public DataResult report(HttpSession session, @RequestBody Report report) {
        String userId = (String) session.getAttribute("userid");
        report.setType("article");
        if (reportService.saveReport(report)) {
            return DataResult.success("举报成功!");
        }
        return DataResult.success("举报的信息已在处理中...");
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建文章")
    public DataResult report(HttpSession session, @RequestBody Article article) {
        String userId = (String) session.getAttribute("userid");
        return articleService.saveArticles(userId, article) ? DataResult.success("保存成功") : DataResult.fail("保存失败");
    }

}
