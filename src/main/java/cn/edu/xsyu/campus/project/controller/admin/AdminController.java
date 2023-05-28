package cn.edu.xsyu.campus.project.controller.admin;

import cn.edu.xsyu.campus.project.entity.*;
import cn.edu.xsyu.campus.project.service.*;
import cn.edu.xsyu.campus.project.util.JustPhone;
import cn.edu.xsyu.campus.project.util.KeyUtil;
import cn.edu.xsyu.campus.project.util.StatusCode;
import cn.edu.xsyu.campus.project.vo.LayuiPageVo;
import cn.edu.xsyu.campus.project.vo.ResultVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: chuan
 * @Date: 2020/3/10 16:54
 */

@Controller
@Api(value = "AdminController", tags = "管理员相关接口")
public class AdminController {
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private LoginService loginService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private NoticesService noticesService;

    /**
     * 管理员跳转登录
     */
    @GetMapping("/admin")
    public String admintologin() {
        return "admin/login/login";
    }

    /**
     * 管理员登录
     * 1.判断输入账号的类型
     * 2.判断是否为管理员或者超级管理员
     * 3.登录
     */
    @ResponseBody
    @PostMapping("/admin/login")
    @ApiOperation(value = "登录提示", httpMethod = "POST", response = ResultVo.class)
    public ResultVo adminlogin(@RequestBody Login login, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String account = login.getUsername();
        String password = login.getPassword();
        String vercode = login.getVercode();
        UsernamePasswordToken token;
        //判断验证码
        if (!CaptchaUtil.ver(vercode, request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            return new ResultVo(false, StatusCode.ERROR, "验证码错误！");
        }
        //判断输入的账号是否手机号
        if (!JustPhone.justPhone(account)) {
            //输入的是用户名
            String username = account;
            //盐加密
            token = new UsernamePasswordToken(username, new Md5Hash(password, "Campus-shops").toString());
        } else {
            //输入的是手机号
            String mobilephone = account;
            login.setMobilephone(mobilephone);
            //将封装的login中username变为null
            login.setUsername(null);
            //盐加密
            token = new UsernamePasswordToken(mobilephone, new Md5Hash(password, "Campus-shops").toString());
        }
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            //盐加密
            String passwords = new Md5Hash(password, "Campus-shops").toString();
            login.setPassword(passwords);
            Login login1 = loginService.userLogin(login);
            //查询登录者的权限
            List<UserRole> userRoles = userRoleService.getUserRoleIdByUserId(login1.getUserid());
            List<Integer> userRoleIds = userRoles.stream().map(UserRole::getRoleid).collect(Collectors.toList());
            if (userRoleIds.contains(2) ||  userRoleIds.contains(3)) {
                session.setAttribute("userid", login1.getUserid());
                session.setAttribute("admin", login1.getUsername());
                session.setAttribute("username", login1.getUsername());
                System.out.println("userid：" + login1.getId());
                System.out.println("admin：" + login1.getUsername());
                System.out.println("username：" + login1.getUsername());
                return new ResultVo(true, StatusCode.OK, "登录成功");
            }
            return new ResultVo(true, StatusCode.ACCESSERROR, "权限不足");
        } catch (UnknownAccountException e) {
            return new ResultVo(true, StatusCode.LOGINERROR, "用户名不存在");
        } catch (IncorrectCredentialsException e) {
            return new ResultVo(true, StatusCode.LOGINERROR, "密码错误");
        }
    }

    /**
     * 用户列表
     */
    @GetMapping("/admin/userlist")
    public String userlist() {
        return "/admin/user/userlist";
    }

    /**
     * 管理员列表
     */
    @RequiresPermissions("admin:set")
    @GetMapping("/admin/adminlist")
    public String adminlist() {
        return "/admin/user/adminlist";
    }

    /**
     * 分页查询不同角色用户信息
     * roleid：1普通成员 2管理员
     * userstatus：1正常 0封号
     */
    @GetMapping("/admin/userlist/{roleId}/{userStatus}")
    @ResponseBody
    @ApiOperation(value = "分页查询不同角色用户信息", httpMethod = "GET", response = LayuiPageVo.class)
    public LayuiPageVo userList(int limit, int page, @PathVariable("roleId") Integer roleId, @PathVariable("userStatus") Integer userStatus) {
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo(roleId, userStatus, page, limit);
        Integer dataNumber = userInfoService.queryAllUserCount(roleId);
        return new LayuiPageVo("", 0, dataNumber, userInfoList);
    }

    /**
     * 设置为管理员或普通成员（roleid）
     * 1：普通成员   2：管理员
     */
    @PutMapping("/admin/set/administrator/{userId}/{roleId}")
    @ResponseBody
    @ApiOperation(value = "设置为管理员或普通成员", httpMethod = "PUT", response = ResultVo.class)
    public ResultVo setAdmin(@PathVariable("userId") String userId, @PathVariable("roleId") Integer roleId) {
        if (roleId == 2) {
            if (loginService.update(null, Wrappers.<Login>lambdaUpdate().set(Login::getRoleid, roleId).eq(Login::getUserid, userId))) {
                userRoleService.updateUserRole(new UserRole().setUserid(userId).setRoleid(2).setIdentity("网站管理员"));
                /**发出设置为管理员的系统通知*/
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userId).setTpname("系统通知")
                        .setWhys("恭喜您已被设置为网站管理员，努力维护网站的良好氛围。");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "设置管理员成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "设置管理员失败");
        } else if (roleId == 1) {
            if (loginService.updateLogin(new Login().setUserid(userId).setRoleid(roleId))) {
                userRoleService.updateUserRole(new UserRole().setUserid(userId).setRoleid(1).setIdentity("网站用户"));
                /**发出设置为网站用户的系统通知*/
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userId).setTpname("系统通知")
                        .setWhys("您已被设置为网站用户，希望您再接再厉。");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "设置成员成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "设置成员失败");
        }
        return new ResultVo(false, StatusCode.ACCESSERROR, "违规操作");
    }

    /**
     * 将用户封号或解封（userstatus）
     * 0：封号  1：解封
     */
    @PutMapping("/admin/user/forbid/{userid}/{userstatus}")
    @ResponseBody
    @ApiOperation(value = "用户封号解封", httpMethod = "PUT", response = ResultVo.class)
    public ResultVo adminUserlist(@PathVariable("userid") String userid, @PathVariable("userstatus") Integer userstatus) {
        if (userstatus == 0) {
            boolean updateUserInfo = userInfoService.updateUserInfo(new UserInfo().setUserid(userid).setUserstatus(userstatus));
            if (loginService.updateById(new Login().setUserid(userid).setUserstatus(userstatus)) && updateUserInfo) {
                /**发出封号的系统通知*/
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("系统通知")
                        .setWhys("因为您的不良行为，您在该网站的账号已被封号。");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "封号成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "封号失败");
        } else if (userstatus == 1) {
            boolean isUpdateSuccess = userInfoService.updateUserInfo(new UserInfo().setUserid(userid).setUserstatus(userstatus));
            if (loginService.updateById(new Login().setUserid(userid).setUserstatus(userstatus)) && isUpdateSuccess) {
                /**发出解封的系统通知*/
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("系统通知")
                        .setWhys("您在该网站的账号已被解封，希望您保持良好的行为。");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "解封成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "解封失败");
        }
        return new ResultVo(false, StatusCode.ACCESSERROR, "违规操作");
    }

    /**
     * 管理员商品列表
     */
    @GetMapping("/admin/product")
    public String adminProduct() {
        return "/admin/product/productlist";
    }


    /**
     * 管理员文章列表
     */
    @GetMapping("/admin/article")
    public String adminArticle() {
        return "/admin/product/article_list";
    }

    /**
     * 管理员商品列表
     */
    @GetMapping("/admin/report")
    public String report() {
        return "/admin/product/report_list";
    }

    /**
     * 分页管理员查看各类商品信息
     * 前端传入页码、分页数量
     * 前端传入商品信息状态码（commstatus）-->全部:100，违规:0，已审核:1，待审核:3 已完成:4
     * 因为是管理员查询，将userid设置为空
     */
    @GetMapping("/admin/commodity/{commstatus}")
    @ResponseBody
    @ApiOperation(value = "分页管理员擦好看各类商品信息", httpMethod = "GET", response = LayuiPageVo.class)
    public LayuiPageVo userCommodity(@PathVariable("commstatus") Integer commstatus, int limit, int page) {
        if (commstatus == 100) {
            IPage<Commodity> commodityIPage = commodityService.queryAllCommodity(null, null, (page - 1) * limit, limit);
            List<Commodity> commodityList = commodityIPage.getRecords();
            Integer dataNumber = commodityService.queryCommodityCount(null, null);
            return new LayuiPageVo("", 0, dataNumber, commodityList);
        } else {
            IPage<Commodity> commodityIPage = commodityService.queryAllCommodity(commstatus, null, (page - 1) * limit, limit);
            List<Commodity> commodityList = commodityIPage.getRecords();
            Integer dataNumber = commodityService.queryCommodityCount(commstatus, null);
            return new LayuiPageVo("", 0, dataNumber, commodityList);
        }
    }

    /**
     * 管理员对商品的操作
     * 前端传入商品id（commid）
     * 前端传入操作的商品状态（commstatus）-->违规:0  通过审核:1
     */
    @ResponseBody
    @PutMapping("/admin/changecommstatus/{commid}/{commstatus}")
    @ApiOperation(value = "管理员对商品操作", httpMethod = "PUT", response = ResultVo.class)
    public ResultVo ChangeCommstatus(@PathVariable("commid") String commid, @PathVariable("commstatus") Integer commstatus) {
        if (commodityService.updateCommstatus(commid, commstatus)) {
            /**发出商品审核结果的系统通知*/
            Commodity commodity = commodityService.queryCommodityById(commid);
            if (commstatus == 0) {
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(commodity.getUserid()).setTpname("商品审核")
                        .setWhys("您的商品 <a href=/product-detail/" + commodity.getCommid() + " style=\"color:#08bf91\" target=\"_blank\" >" + commodity.getCommname() + "</a> 未通过审核，目前不支持公开发布。");
                noticesService.insertNotices(notices);
            } else if (commstatus == 1) {
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(commodity.getUserid()).setTpname("商品审核")
                        .setWhys("您的商品 <a href=/product-detail/" + commodity.getCommid() + " style=\"color:#08bf91\" target=\"_blank\" >" + commodity.getCommname() + "</a> 已通过审核，快去看看吧。");
                noticesService.insertNotices(notices);
            }
            return new ResultVo(true, StatusCode.OK, "操作成功");
        }
        return new ResultVo(false, StatusCode.ERROR, "操作失败");
    }

}
