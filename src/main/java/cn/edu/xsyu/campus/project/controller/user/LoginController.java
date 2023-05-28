package cn.edu.xsyu.campus.project.controller.user;


import cn.edu.xsyu.campus.project.entity.AliyunSmsProperties;
import cn.edu.xsyu.campus.project.entity.Login;
import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.entity.UserRole;
import cn.edu.xsyu.campus.project.service.LoginService;
import cn.edu.xsyu.campus.project.service.UserInfoService;
import cn.edu.xsyu.campus.project.service.UserRoleService;
import cn.edu.xsyu.campus.project.service.impl.AliyunSmsService;
import cn.edu.xsyu.campus.project.util.*;
import cn.edu.xsyu.campus.project.vo.ResultVo;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Controller
@Api(value = "LoginController",tags = "登陆相关")
public class LoginController {
    @Resource
    private LoginService loginService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserRoleService userRoleService;

    @Resource
    AliyunSmsService aliyunSmsService;

    @Autowired
    private AliyunSmsProperties properties;

    /**手机号和注册验证码map集合*/
    ExpiringMap<String, String> phoneCodeMap = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(5, TimeUnit.MINUTES)
            .build();
    /**
     *图片验证码
     * */
    @RequestMapping(value = "/images", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "图片验证码", httpMethod = "GET POST")
    public void images(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ValidateCode vCode = new ValidateCode(820, 200, 5, 80);
        vCode.write(response.getOutputStream());
    }


    /**
     * 获取验证码图片
     * Gets captcha code.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    @RequestMapping("/getVerify")
    public void getCaptchaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);
        CaptchaUtil.out(captcha, request, response);
    }
    /**注册时发送短信验证码
     * 1.判断是否为注册类型验证码
     * 2.判断手机号格式是否正确
     * 3.判断手机号是否已经注册过
     * 4.发送注册验证码并存入map集合
     * */
    @ResponseBody
    @PostMapping("/user/sendregcode")
    @ApiOperation(value = "注册时发送短信验证码", httpMethod = "POST",response = ResultVo.class)
    public ResultVo sendregcode(HttpServletRequest request) throws IOException{
        JSONObject jsonObject = JsonReader.receivePost(request);
        final String mobilephone = jsonObject.getString("mobilephone");
        Integer type = jsonObject.getInt("type");
        Login login = new Login();
        if(type!=0){
            return new ResultVo(false, StatusCode.ACCESSERROR,"违规操作");
        }
        if (!JustPhone.justPhone(mobilephone)) {//判断输入的手机号格式是否正确
            return new ResultVo(false,StatusCode.ERROR,"请输入正确格式的手机号");
        }
        //查询手机号是否已经注册
        login.setMobilephone(mobilephone);
        Login userIsExist = loginService.userLogin(login);
        if (!StringUtils.isEmpty(userIsExist)){//用户账号已经存在
            return new ResultVo(false, StatusCode.ERROR,"该手机号已经注册过了");
        }
        return sendSMS(mobilephone);
    }


    /**注册
     * 1.前端传入用户名（username）、密码（password）、邮箱（email）、手机号（mobilephone）、验证码（vercode）
     * 2.查询账号是否已经注册
     * 3.查询用户名是否已存在
     * 4.判断验证码是否有效或正确
     * 5.注册
     * */
    @ResponseBody
    @PostMapping("/user/register")
    @ApiOperation(value = "注册", httpMethod = "POST",response = ResultVo.class)
    public  ResultVo userReg(@RequestBody UserInfo userInfo, HttpSession session) {
        String username = userInfo.getUsername();
        String password = userInfo.getPassword();
        String mobilephone = userInfo.getMobilephone();
        String vercode = userInfo.getVercode();
        Login login = new Login().setMobilephone(mobilephone);
        //查询账号是否已经注册
        Login userIsExist = loginService.userLogin(login);
        if (!StringUtils.isEmpty(userIsExist)){//用户账号已经存在
            return new ResultVo(false, StatusCode.ERROR,"该用户已经注册过了");
        }
        login.setUsername(username).setMobilephone(null);
        Login userNameIsExist = loginService.userLogin(login);
        if (!StringUtils.isEmpty(userNameIsExist)){//用户名已经存在
            return new ResultVo(false, StatusCode.ERROR,"用户名已存在，请换一个吧");
        }
        String rel = phoneCodeMap.get(mobilephone);
        if (StringUtils.isEmpty(rel)) {//验证码到期 或者 没发送短信验证码
            return new ResultVo(false,StatusCode.ERROR,"请重新获取验证码");
        }
        if (rel.equalsIgnoreCase(vercode)) {//验证码正确
            //盐加密
            String passwords = new Md5Hash(password, "Campus-shops").toString();
            String userid = KeyUtil.genUniqueKey();
            login.setId(KeyUtil.genUniqueKey()).setUserid(userid).setMobilephone(mobilephone).setPassword(passwords);
            loginService.loginAdd(login);
            //新注册用户存入默认头像、存入默认签名
            userInfo.setUserid(userid).setPassword(passwords).setUimage("/static/pic/d1d66c3ea71044a9b938b00859ca94df.jpg").
                    setSign("如此清秋何吝酒，这般明月不须钱").setStatus("offline");
            if (userInfoService.saveUser(userInfo)){
                /**注册成功后存入session*/
                session.setAttribute("userid",userid);
                session.setAttribute("username",username);
                /**存入用户角色信息*/
                userRoleService.save(new UserRole().setUserid(userid).setRoleid(1).setIdentity("网站用户"));
                return new ResultVo(true,StatusCode.OK,"注册成功");
            }
            return new ResultVo(false,StatusCode.ERROR,"注册失败");
        }
        return new ResultVo(false,StatusCode.ERROR,"验证码错误");
    }

    /**登录
     * 1.判断输入账号的类型
     * 2.登录
     * */
    @ResponseBody
    @PostMapping("/user/login")
    @ApiOperation(value = "登录", httpMethod = "POST",response = ResultVo.class)
    public ResultVo userLogin(@RequestBody @ApiParam(name = "login",required = true) Login login , HttpSession session){
        String account=login.getUsername();
        String password=login.getPassword();
        String vercode=login.getVercode();
        UsernamePasswordToken token;
//        if(!ValidateCode.code.equalsIgnoreCase(vercode)){
//            return new ResultVo(false,StatusCode.ERROR,"请输入正确的验证码");
//        }
        //判断输入的账号是否手机号
        if (!JustPhone.justPhone(account)) {
            //输入的是用户名
            String username = account;
            //盐加密
            token=new UsernamePasswordToken(username, new Md5Hash(password,"Campus-shops").toString());
        }else {
            //输入的是手机号
            String mobilephone = account;
            login.setMobilephone(mobilephone);
            //将封装的login中username变为null
            login.setUsername(null);
            //盐加密
            token=new UsernamePasswordToken(mobilephone, new Md5Hash(password,"Campus-shops").toString());
        }
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(token);
            //盐加密
            String passwords = new Md5Hash(password, "Campus-shops").toString();
            login.setPassword(passwords);
            Login login1 = loginService.userLogin(login);
            session.setAttribute("userid",login1.getUserid());
            session.setAttribute("username",login1.getUsername());
            return new ResultVo(true,StatusCode.OK,"登录成功");
        }catch (UnknownAccountException e){
            return new ResultVo(true,StatusCode.LOGINERROR,"用户名不存在");
        }catch (IncorrectCredentialsException e){
            return new ResultVo(true,StatusCode.LOGINERROR,"密码错误");
        }
    }

    /**重置密码时发送短信验证码
     * 1.判断是否为重置密码类型验证码
     * 2.判断手机号格式是否正确
     * 3.查询账号是否存在
     * 4.发送验证码
     * */
    @ResponseBody
    @PostMapping("/user/sendresetpwd")
    @ApiOperation(value = "重置密码发送短信验证码", httpMethod = "POST",response = ResultVo.class)
    public ResultVo sendresetpwd(HttpServletRequest request) throws IOException {
        JSONObject json = JsonReader.receivePost(request);
        final String mobilephone = json.getString("mobilephone");
        Integer type = json.getInt("type");
        Login login = new Login();
        if(type!=1){
            return new ResultVo(false,StatusCode.ACCESSERROR,"违规操作");
        }
        if (!JustPhone.justPhone(mobilephone)) {//判断输入的手机号格式是否正确
            return new ResultVo(false,StatusCode.ERROR,"请输入正确格式的手机号");
        }
        //查询手机号是否存在
        login.setMobilephone(mobilephone);
        Login userIsExist = loginService.userLogin(login);
        if (StringUtils.isEmpty(userIsExist)){//用户账号不存在
            return new ResultVo(false, StatusCode.LOGINERROR,"该用户不存在");
        }
        return sendSMS(mobilephone);

    }

    /**重置密码
     * 1.判断手机号格式是否正确
     * 2.查询手机号是否存在
     * 3.判断验证码是否有效或正确
     * 4.重置密码
     * */
    @ResponseBody
    @PostMapping("/user/resetpwd")
    @ApiOperation(value = "重置密码", httpMethod = "POST",response = ResultVo.class)
    public  ResultVo resetpwd(@RequestBody Login login) {
        String mobilephone=login.getMobilephone();
        String password=login.getPassword();
        String vercode=login.getVercode();
        Login login1 = new Login();
        UserInfo userInfo = new UserInfo();
        if (!JustPhone.justPhone(mobilephone)) {//判断输入的手机号格式是否正确
            return new ResultVo(false,StatusCode.ERROR,"请输入正确格式的手机号");
        }
        //查询手机号是否存在
        login1.setMobilephone(mobilephone);
        Login userIsExist = loginService.userLogin(login1);
        if (StringUtils.isEmpty(userIsExist)){//用户账号不存在
            return new ResultVo(false, StatusCode.LOGINERROR,"该账号不存在");
        }
        String rel = phoneCodeMap.get(mobilephone);
        if (StringUtils.isEmpty(rel)) {//验证码到期 或者 没发送短信验证码
            return new ResultVo(false,StatusCode.ERROR,"请重新获取验证码");
        }
        if (rel.equalsIgnoreCase(vercode)) {//验证码正确
            //盐加密
            String passwords = new Md5Hash(password, "Campus-shops").toString();
            login1.setPassword(passwords).setId(userIsExist.getId()).setMobilephone(null);
            userInfo.setMobilephone(mobilephone).setPassword(passwords).setUserid(userIsExist.getUserid());
            if (loginService.updateById(login1) && userInfoService.updateUserInfo(userInfo)){
                return new ResultVo(true,StatusCode.OK,"重置密码成功");
            }
            return new ResultVo(false,StatusCode.ERROR,"重置密码失败");
        }
        return new ResultVo(false,StatusCode.ERROR,"验证码错误");
    }

    /**退出登陆*/
    @GetMapping("/user/logout")
    @ApiOperation(value = "退出登录", httpMethod = "GET")
    public String logout(HttpServletRequest request,HttpSession session){
        String userid = (String)session.getAttribute("userid");
        String username = (String)session.getAttribute("username");
        if(StringUtils.isEmpty(userid) && StringUtils.isEmpty(username)){
            return "redirect:/";
        }
        request.getSession().removeAttribute("userid");
        request.getSession().removeAttribute("username");
        return "redirect:/";
    }

    private ResultVo sendSMS(String mobilephone) {
        String code = GetCode.phonecode();
        String templateParam = "{\"code\":\"" + code + "\"}";
        SendSmsResponse smsResponse = aliyunSmsService.sendSms(mobilephone, templateParam);//发送验证码
        if (smsResponse != null && "OK".equals(smsResponse.getCode())) {
            phoneCodeMap.put(mobilephone, code);
            return new ResultVo(true, StatusCode.SMS, "验证码发送成功");
        } else if ("isv.SMS_TEST_NUMBER_LIMIT".equals(smsResponse.getCode())) {
            // 向您的手机号发送申请加入白名单的短信
            smsResponse = aliyunSmsService.sendSms(properties.getMaintainUserPhone(), templateParam);//发送验证码
            if (smsResponse != null && "OK".equals(smsResponse.getCode())) {
                phoneCodeMap.put(mobilephone, code);
                String msg = String.format("请向将手机号 \"%s\"申请, 将\"%s\"加入短信服务白名单，以便正常接收验证码。验证已成功发送到 \"%s\"", properties.getMaintainUserPhone(), mobilephone, properties.getMaintainUserPhone());
                return new ResultVo(true, StatusCode.SMS, msg);
            } else {
                phoneCodeMap.put(mobilephone, code);
                String msg = String.format("请向将手机号 \"%s\"申请, 加入短信服务白名单，以便正常接收验证码。验证发送失败 测试验证是:\"%d\"", properties.getMaintainUserPhone(), code);
                return new ResultVo(true, StatusCode.SMS, msg);
            }
        } else {
            return new ResultVo(true, StatusCode.SMS, "验证码发送失败");
        }
    }

}