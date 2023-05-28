package cn.edu.xsyu.campus.project.controller;

import cn.edu.xsyu.campus.project.service.ArticleService;
import cn.edu.xsyu.campus.project.util.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 首页
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "首页数据")
public class HomeController {
    @Resource
    private ArticleService articleService;

    @GetMapping("/home")
    @ApiOperation(value = "获取首页数据接口")
    public DataResult getHomeInfo(HttpServletRequest request, HttpSession session) {
        String UserId = (String)session.getAttribute("userid");
        DataResult result = DataResult.success();
        result.setData(articleService.getStudyModuleStatistics());
        return result;
    }

    @GetMapping("/home/other")
    @ApiOperation(value = "获取首页Other数据接口")
    public DataResult getHomeOtherInfo(HttpServletRequest request, HttpSession session) {
        DataResult result = DataResult.success();
        result.setData(articleService.getStudyOtherModuleStatistics());
        return result;
    }

    @GetMapping("/forum_post/{type}")
    @ApiOperation(value = "查询其他主题文章")
    public DataResult detailInfo(@PathVariable("type") Integer type) {
        return DataResult.success(articleService.getStudySectionByTopic(type));
    }

}
