package project.controller;


import project.entity.Collect;
import project.service.CollectService;
import project.util.GetDate;
import project.util.KeyUtil;
import project.util.StatusCode;
import project.vo.LayuiPageVo;
import project.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  收藏控制器
 * </p>
 *
 */
@Controller
@Api(value = "CollectController",tags = "商品收藏")
public class CollectController {
    @Resource
    private CollectService collectService;

    /**
     * 商品详情界面：收藏商品or取消收藏
     * 前端传入收藏操作（colloperate：1收藏，2取消收藏）,获取session中用户id信息，判断是否登录
     * (1). 收藏商品
     * 1.前端传入商品id（commid）、商品名（commname）、商品描述（commdesc）、商品用户id（cmuserid）
     *   商品用户名（username）、商品所在学校（school）
     * 2.session中获取收藏用户id（couserid）
     * 3.进行收藏操作
     * (2). 取消收藏
     * 1.前端传入商品id（commid）
     * 2.判断是否本人取消收藏
     * 3.进行取消收藏操作
     */
    @ResponseBody
    @PostMapping("/collect/operate")
    @ApiOperation(value = "收藏状态",httpMethod = "POST",response = ResultVo.class)
    public ResultVo insertcollect(@RequestBody Collect collect, HttpSession session){
        String couserid = (String) session.getAttribute("userid");
        Integer colloperate = collect.getColloperate();
        collect.setCouserid(couserid);

        if (StringUtils.isEmpty(couserid)){
            return new ResultVo(false, StatusCode.ACCESSERROR,"请先登录");
        }

        if (colloperate == 1){
            Collect collect1 = collectService.queryCollectStatus(collect.getCouserid(), collect.getCommid());
            if(!StringUtils.isEmpty(collect1)){
                /**更改原来的收藏信息和状态*/
                collect1.setCommname(collect.getCommname()).setCommdesc(collect.getCommdesc()).setSchool(collect.getSchool())
                        .setSoldtime(GetDate.strToDate());
                collectService.updateCollect(collect);
                    return new ResultVo(true, StatusCode.OK,"收藏成功");
            }else{
                collect.setId(KeyUtil.genUniqueKey());
                collectService.insertCollect(collect);
                return new ResultVo(true, StatusCode.OK,"收藏成功");
            }

        }else {
            Collect collect1 = collectService.queryCollectStatus(collect.getCouserid(), collect.getCommid());
            /**判断是否为本人操作*/
            if (collect1.getCouserid().equals(couserid)){
                collectService.updateCollect(collect);
                return new ResultVo(true, StatusCode.OK,"取消成功");
            }
            return new ResultVo(false,StatusCode.ACCESSERROR,"禁止操作");
        }
    }

    /**
     * 收藏列表界面取消收藏
     * 1.前端传入收藏id（id）
     * 2.判断是否本人取消收藏
     * 3.进行取消收藏操作
     */
    @ResponseBody
    @PutMapping("/collect/delete/{id}")
    @ApiOperation (value = "取消收藏状态返回",httpMethod = "PUT",response = ResultVo.class)
    public ResultVo deletecollect(@PathVariable("id") String id,HttpSession session){
        String couserid = (String) session.getAttribute("userid");
        Collect collect = collectService.getById(id);
        /**判断是否为本人操作*/
        if (collect.getCouserid().equals(couserid)){
            collect.setColloperate(2);
            collectService.updateCollect(collect);
            return new ResultVo(true, StatusCode.OK,"取消成功");
        }
        return new ResultVo(false,StatusCode.ACCESSERROR,"禁止操作");
    }

    /**
     * 分页查看用户所有收藏内容
     * 前端传入页码、分页数量
     * 查询分页数据
     */
    @ResponseBody
    @GetMapping("/user/collect/queryall")
    @ApiOperation (value = "收藏内容",httpMethod = "POST",response = LayuiPageVo.class)
    public LayuiPageVo usercollect(int limit, int page, HttpSession session) {
        String couserid = (String) session.getAttribute("userid");
        List<Collect> collectList = collectService.queryAllCollect(couserid, page, limit);
        Integer dataNumber = collectService.queryCollectCount(couserid);
        return new LayuiPageVo("",0,dataNumber,collectList);
    }
}

