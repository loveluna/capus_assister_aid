package cn.edu.xsyu.campus.project.controller.user;

import cn.edu.xsyu.campus.project.entity.Commodity;
import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.service.CommodityService;
import cn.edu.xsyu.campus.project.service.UserInfoService;
import cn.edu.xsyu.campus.project.util.StatusCode;
import cn.edu.xsyu.campus.project.vo.LayuiPageVo;
import cn.edu.xsyu.campus.project.vo.ResultVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


@Controller
@Api(value = "UserHomeController", tags = "个人简介相关接口")
public class UserHomeController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CommodityService commodityService;

    /**
     * 个人简介
     * 前端传入用户id（userid）
     */
    @ResponseBody
    @GetMapping("/user/userinfo/{userid}")
    @ApiOperation(value = "个人简介查询状态", httpMethod = "GET", response = ResultVo.class)
    public ResultVo userinfo(@PathVariable("userid") String userid) {
        UserInfo userInfo = userInfoService.lookUserinfo(userid);
        if (!StringUtils.isEmpty(userInfo)) {
            return new ResultVo(true, StatusCode.OK, "查询成功", userInfo);
        }
        return new ResultVo(false, StatusCode.ERROR, "查询失败");
    }

    /**
     * 分页展示个人已审核的商品信息（状态码：1）
     * 前端传入用户id（userid）、当前页码（nowPaging）、
     */
    @ResponseBody
    @GetMapping("/user/usercommodity/{userid}")
    @ApiOperation(value = "分页展示个人已审核的商品信息", httpMethod = "GET", response = LayuiPageVo.class)
    public LayuiPageVo userHomeCommodity(@PathVariable("userid") String userid, int limit, int page) {
        IPage<Commodity> commodityIPage = commodityService.queryAllCommodity(1, userid, (page - 1) * limit, limit);
        List<Commodity> commodityList = commodityIPage.getRecords();
        Integer dataNumber = commodityService.queryCommodityCount(1, userid);
        return new LayuiPageVo("", 0, dataNumber, commodityList);
    }

}
