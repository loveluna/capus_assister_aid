package cn.edu.xsyu.campus.project.controller;

import cn.edu.xsyu.campus.project.entity.Soldrecord;
import cn.edu.xsyu.campus.project.service.SoldrecordService;
import cn.edu.xsyu.campus.project.util.StatusCode;
import cn.edu.xsyu.campus.project.vo.LayuiPageVo;
import cn.edu.xsyu.campus.project.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 销售记录控制器
 * </p>
 */
@Controller
@Api(value = "soldrecordController", tags = "售卖记录")
public class SoldrecordController {
    @Resource
    private SoldrecordService soldrecordService;

    /**
     * 删除售出记录
     * 1.前端传入需删除记录的id（id）
     * 2.判断是否是本人
     */
    @ResponseBody
    @PutMapping("/soldrecord/delect/{id}")
    @ApiOperation(value = "删除售出记录", httpMethod = "PUT", response = ResultVo.class)
    public ResultVo delectSold(@PathVariable("id") String id) {
        if (soldrecordService.deleteSold(id)) {
            return new ResultVo(true, StatusCode.OK, "删除记录成功");
        }
        return new ResultVo(false, StatusCode.ERROR, "删除记录失败");
    }

    /**
     * 分页查看用户所有售出记录
     * 1.前端传入页码、分页数量
     * 2.查询分页数据
     */
    @ResponseBody
    @GetMapping("/soldrecord/lookuser")
    @ApiOperation(value = "返回用户所有售出记录", httpMethod = "GET", response = LayuiPageVo.class)
    public LayuiPageVo LookUserSold(int limit, int page, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        List<Soldrecord> soldrecordList = soldrecordService.queryAllSoldrecord(userid, page, limit);
        Integer dataNumber = soldrecordService.querySoldCount(userid);
        return new LayuiPageVo("", 0, dataNumber, soldrecordList);
    }

    /**
     * 分页查看全部的售出记录
     * 1.前端传入页码、分页数量
     * 2.查询分页数据
     */
    @ResponseBody
    @GetMapping("/soldrecord/queryall")
    @ApiOperation(value = "返回全部的售出记录", httpMethod = "PUT", response = LayuiPageVo.class)
    public LayuiPageVo queryAllSold(int limit, int page) {
        List<Soldrecord> soldrecordList = soldrecordService.queryAllSoldrecord(null, page, limit);
        Integer dataNumber = soldrecordService.querySoldCount(null);
        return new LayuiPageVo("", 0, dataNumber, soldrecordList);
    }

}

