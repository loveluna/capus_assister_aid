package project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import project.entity.Comment;
import project.service.ArticleService;
import project.service.CommentService;
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
}
