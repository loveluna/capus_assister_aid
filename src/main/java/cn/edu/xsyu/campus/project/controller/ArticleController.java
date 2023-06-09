package cn.edu.xsyu.campus.project.controller;

import cn.edu.xsyu.campus.project.entity.Article;
import cn.edu.xsyu.campus.project.entity.Comment;
import cn.edu.xsyu.campus.project.entity.Commodity;
import cn.edu.xsyu.campus.project.entity.Report;
import cn.edu.xsyu.campus.project.service.ArticleService;
import cn.edu.xsyu.campus.project.service.CommentService;
import cn.edu.xsyu.campus.project.service.ReportService;
import cn.edu.xsyu.campus.project.util.DataResult;
import cn.edu.xsyu.campus.project.util.StatusCode;
import cn.edu.xsyu.campus.project.vo.LayuiPageVo;
import cn.edu.xsyu.campus.project.vo.ResultVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;


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

    /**
     * 分页展示个人各类文章信息
     * 前端传入页码、分页数量
     * 前端传入文章信息状态码（articleStatus）-->全部:100，已审核:1，待审核:3，违规:0，已完成:4
     */
    @GetMapping("/list/{articleStatus}")
    @ResponseBody
    @ApiOperation(value = "分页展示个人各类文章信息", httpMethod = "GET", response = LayuiPageVo.class)
    public LayuiPageVo userArticle(@PathVariable("articleStatus") Integer articleStatus, int limit, int page, HttpSession session) {
        String userId = (String) session.getAttribute("userid");
        //如果未登录，给一个假id
        if (StringUtils.isEmpty(userId)) {
            userId = "123456";
        }
        List<Article> articles = null;
        Integer dataNumber;
        if (articleStatus == 100) {
            IPage<Article> articleIPage = articleService.queryAllArticles(null, userId, (page - 1) * limit, limit);
            articles = articleIPage.getRecords();
            dataNumber = articleService.queryArticleCount(null, userId);
        } else if (articleStatus == 200) {
            IPage<Article> articleIPage = articleService.queryAllArticles(null, null, (page - 1) * limit, limit);
            articles = articleIPage.getRecords();
            dataNumber = articleService.queryArticleCount(null, null);
        } else {
            IPage<Article> articleIPage = articleService.queryAllArticles(articleStatus, userId, (page - 1) * limit, limit);
            articles = articleIPage.getRecords();
            dataNumber = articleService.queryArticleCount(articleStatus, userId);
        }
        return new LayuiPageVo("", 0, dataNumber, articles);
    }

    /**
     * 个人对文章的操作
     * 前端传入文章id（commid）
     * 前端传入操作的文章状态（articleStatus）-->删除:2  已完成:4
     */
    @ResponseBody
    @GetMapping("/updateStatus/{articleId}/{status}")
    @ApiOperation(value = "个人随文章操作返回状态", httpMethod = "GET", response = ResultVo.class)
    public ResultVo ChangearticleStatus(@PathVariable("articleId") String articleId, @PathVariable("status") Integer status, HttpSession session) {
        if (articleService.updateArticleStatus(articleId, status)) {
            return new ResultVo(true, StatusCode.OK, "操作成功");
        }
        return new ResultVo(false, StatusCode.ERROR, "操作失败");
    }

    @GetMapping("/reports/{status}")
    @ResponseBody
    @ApiOperation(value = "分页举报信息", httpMethod = "GET", response = LayuiPageVo.class)
    public LayuiPageVo userReport(@PathVariable("status") Integer status, int limit, int page) {
        if (status == 100) {
            List<Article> articles = reportService.queryAllCommodity(null, null, page, limit);
            Integer dataNumber = reportService.queryCommodityCount(null, null);
            return new LayuiPageVo("", 0, dataNumber, articles);
        } else {
            List<Article> articles = reportService.queryAllCommodity(status, null, page, limit);
            Integer dataNumber = reportService.queryCommodityCount(status, null);
            return new LayuiPageVo("", 0, dataNumber, articles);
        }
    }

}
