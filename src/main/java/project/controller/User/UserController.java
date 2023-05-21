package project.controller.User;

import project.entity.Login;
import project.entity.UserInfo;
import project.service.LoginService;
import project.service.UserInfoService;
import project.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 个人中心 控制器
 * </p>
 *
 */
@Controller
@Api(value = "UserController",tags = "普通用户登录注册相关")
public class UserController {
    @Resource
    private LoginService loginService;
    @Resource
    private UserInfoService userInfoService;
    /**
     * 修改密码
     * 1.前端传入旧密码（oldpwd）、新密码（newpwd）
     * 2.判断输入旧密码和系统旧密码是否相等
     * 4.修改密码
     */
    @ResponseBody
    @PutMapping("/user/updatepwd")
    @ApiOperation(value = "修改密码", httpMethod = "POST",response = ResultVo.class)
    public ResultVo updatepwd(HttpSession session, HttpServletRequest request) throws IOException {
        JSONObject json = JsonReader.receivePost(request);
        String oldpwd = json.getString("oldpwd");
        String newpwd = json.getString("newpwd");
        String userid = (String) session.getAttribute("userid");
        Login login = new Login();
        UserInfo userInfo = new UserInfo();
        login.setUserid(userid);
        Login login1 = loginService.userLogin(login);
        String oldpwds = new Md5Hash(oldpwd, "Campus-shops").toString();
        //如果旧密码相等
        if (oldpwds.equals(login1.getPassword())){
            //盐加密
            String passwords = new Md5Hash(newpwd, "Campus-shops").toString();
            login.setPassword(passwords);
            userInfo.setPassword(passwords).setUserid(login1.getUserid());
            if (loginService.updateLogin(login) && userInfoService.updateUserInfo(userInfo)) {
                return new ResultVo(true, StatusCode.OK, "修改密码成功");
            }
            return new ResultVo(false, StatusCode.ERROR, "修改密码失败");
        }
        return new ResultVo(false, StatusCode.LOGINERROR, "当前密码错误");
    }

    /**
     * 展示用户头像昵称
     */
    @ResponseBody
    @PostMapping("/user/avatar")
    @ApiOperation(value = "展示用户头像昵称", httpMethod = "POST",response = ResultVo.class)
    public ResultVo userAvatar( HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        UserInfo userInfo = userInfoService.queryPartInfo(userid);
        return new ResultVo(true, StatusCode.OK, "查询头像成功",userInfo);
    }

    /**
     * 修改头像
     * */
    @PostMapping(value = "/user/updateuimg")
    @ResponseBody
    @ApiOperation(value = "修改头像", httpMethod = "POST",response = JSONObject.class)
    public JSONObject updateuimg(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());//获得文件扩展名
        String filenames = filename + "." + ext;//文件全名
        String pathname = "D:\\campusshops\\file\\" + filenames;
        file.transferTo(new File(pathname));
        resUrl.put("src", "/static/pic/" +filenames);
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        String uimgUrl = "/static/pic/" + filenames;

        String userid=(String) session.getAttribute("userid");
        UserInfo userInfo = new UserInfo().setUserid(userid).setUimage(uimgUrl);
        userInfoService.updateUserInfo(userInfo);
        return res;
    }

    /**
     * 展示个人信息
     */
    @RequiresPermissions("user:userinfo")
    @GetMapping("/user/lookinfo")
    @ApiOperation(value = "跳转展示个人信息", httpMethod = "GET")
    public String lookinfo(HttpSession session, ModelMap modelMap) {
        String userid = (String) session.getAttribute("userid");
        UserInfo userInfo = userInfoService.lookUserinfo(userid);
        modelMap.put("userInfo",userInfo);
        return "/user/userinfo";
    }

    /**
     * 跳转到完善个人信息
     */
    @GetMapping("/user/perfectinfo")
    @ApiOperation(value = "跳转到完善个人信息", httpMethod = "POST")
    public String perfectInfo(HttpSession session, ModelMap modelMap) {
        String userid = (String) session.getAttribute("userid");
        UserInfo userInfo = userInfoService.lookUserinfo(userid);
        modelMap.put("perfectInfo",userInfo);
        return "/user/perfectInfo";
    }

    /**
     * 修改个人信息
     * 1.前端传入用户昵称（username）、用户邮箱（email）、性别（sex）、学校（school）、院系（faculty）、入学时间（startime）
     * 2.前端传入变更后的字段，未变更的不传入后台
     * 3.判断更改的用户名是否已存在
     * 4.修改个人信息
     */
    @ResponseBody
    @PostMapping("/user/updateinfo")
    @ApiOperation(value = "修改个人信息", httpMethod = "POST",response = ResultVo.class)
    public ResultVo updateInfo(@RequestBody UserInfo userInfo, HttpSession session) {
        String username = userInfo.getUsername();
        String userid = (String) session.getAttribute("userid");
        Login login = new Login();
        //如果传入用户名
        if (!StringUtils.isEmpty(username)){
            login.setUsername(username);
            Login login1 = loginService.userLogin(login);
            //如果该用户名对应有用户
            if (!StringUtils.isEmpty(login1)){
                return new ResultVo(false, StatusCode.ERROR, "该用户名已存在");
            }
            login.setUserid(userid);
            //修改登录表中用户名
            loginService.updateById(login);
        }
        userInfo.setUserid(userid);
        if (userInfoService.updateUserInfo(userInfo)) {
            return new ResultVo(true, StatusCode.OK, "修改成功");
        }
        return new ResultVo(false, StatusCode.ERROR, "修改失败");
    }

    /**
     * 修改绑定手机号
     * 1.获取session中userid
     * 2.修改login和userInfo中对应的手机号
     */
    @ResponseBody
    @PutMapping("/user/updatephone/{mobilephone}/{vercode}")
    @ApiOperation(value = "修改绑定手机号", httpMethod = "PUT",response = ResultVo.class)
    public ResultVo updatephone(@PathVariable("mobilephone")String mobilephone,@PathVariable("vercode")String vercode,HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        Login login = new Login().setUserid(userid).setMobilephone(mobilephone);
        UserInfo userInfo = new UserInfo().setUserid(userid).setMobilephone(mobilephone);
        if (loginService.updateById(login) && userInfoService.updateUserInfo(userInfo)) {
            return new ResultVo(true, StatusCode.OK, "更换手机号成功");
        }
        return new ResultVo(false,StatusCode.ERROR,"更换手机号成功失败");
    }

}

