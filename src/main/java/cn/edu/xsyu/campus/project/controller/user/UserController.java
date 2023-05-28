package cn.edu.xsyu.campus.project.controller.user;

import cn.edu.xsyu.campus.project.config.FileUploadProperties;
import cn.edu.xsyu.campus.project.entity.Login;
import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.service.LoginService;
import cn.edu.xsyu.campus.project.service.UserInfoService;
import cn.edu.xsyu.campus.project.util.DataResult;
import cn.edu.xsyu.campus.project.util.JsonReader;
import cn.edu.xsyu.campus.project.util.StatusCode;
import cn.edu.xsyu.campus.project.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @Resource
    private FileUploadProperties fileUploadProperties;
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
    @ApiOperation(value = "修改头像", httpMethod = "POST",response = DataResult.class)
    public DataResult updateImg(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) throws IOException {
        //存储文件夹
        String newPath = fileUploadProperties.getPath();
        String fileName = file.getOriginalFilename();
        //id与filename保持一直，删除文件
        String fileNameNew = UUID.randomUUID().toString().replace("-", "") + getFileType(fileName);
        String newFilePathName = newPath + fileNameNew;
        String url = fileUploadProperties.getUrl() + "/" + fileNameNew;
        // 创建输出文件对象
        File outFile = new File(newFilePathName);
        // 拷贝文件到输出文件对象
        FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);

        // 将文件复制到编译后的目录中
        Path sourcePath = Paths.get(newFilePathName);
        Path targetPath = Paths.get("target/classes/static/images/pic/" + fileNameNew);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("src", url);
        String userid=(String) session.getAttribute("userid");
        UserInfo userInfo = new UserInfo().setUserid(userid).setUimage(url);
        userInfoService.updateUserInfo(userInfo);
        return DataResult.success(resultMap);
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

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    private String getFileType(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}

