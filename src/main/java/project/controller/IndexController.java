package project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@Api(value = "IndexController",tags = "首页")
public class IndexController {
    /**
     * 网站首页
     * */
    @GetMapping("/")
    @ApiOperation(value = "跳转首页",httpMethod = "GET")
    public String index(){
        return "/index";
    }

    /**
     * 联系我们
     * */
    @GetMapping("/contacts")
    @ApiOperation(value = "跳转联系我们",httpMethod = "GET")
    public String contacts(){
        return "/common/contacts";
    }

    /**
     * 关于我们
     * */
    @GetMapping("/about")
    @ApiOperation(value = "跳转关于我们",httpMethod = "GET")
    public String about(){
        return "/common/about";
    }

    /**
     * 后台管理首页
     * */
    @GetMapping("/admin/index")
    @ApiOperation(value = "跳转管理首页",httpMethod = "GET")
    public String adminindex(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String admin = (String) session.getAttribute("admin");
        /**拦截器：如果不是管理员，则进行重定向*/
        if (StringUtils.isEmpty(admin)){
            response.sendRedirect(request.getContextPath() + "/");//重定向
        }
        return "/admin/index";
    }

    /**
     * 用户登录注册
     * */
    @GetMapping("/login")
    @ApiOperation(value = "跳转登录注册页面",httpMethod = "GET")
    public String login(){
        return "/user/logreg";
    }

    @GetMapping("/forum_post")
    @ApiOperation(value = "跳转文章详情",httpMethod = "GET")
    public String detail(){
        return "/collaboration";
    }

    @GetMapping("/article")
    public String collaborationArticle() {
        return "/article";
    }
    /**
     * 用户忘记密码
     * */
    @GetMapping("/forget")
    @ApiOperation(value = "跳转忘记密码页面",httpMethod = "GET")
    public String forget(){
        return "user/forget";
    }

    /**
     * 个人中心
     * */
    @GetMapping("/user/center")
    @ApiOperation(value = "跳转个人中心",httpMethod = "GET")
    public String usercenter(){
        return "/user/user-center";
    }

    /**
     * 个人主页
     * */
    @GetMapping("/user/home")
    @ApiOperation(value = "跳转个人主页",httpMethod = "GET")
    public String userhome(){
        return "/user/user-home";
    }

    /**
     * 用户修改密码
     * */
    @RequiresPermissions("user:userinfo")
    @ApiOperation(value = "用户修改密码",httpMethod = "GET")
    @GetMapping("/user/pass")
    public String userinfo(){
        return "/user/updatepass";
    }

    /**
     * 用户更换手机号
     * */
    @RequiresPermissions("user:userinfo")
    @GetMapping("/user/phone")
    @ApiOperation(value = "跳转更换手机号页面",httpMethod = "GET")
    public String userphone(){
        return "/user/updatephone";
    }

    /**
     * 用户商品列表
     * */
    @GetMapping("/user/product")
    @ApiOperation(value = "跳转商品列表页面",httpMethod = "GET")
    public String userproduct(){
        return "/user/product/productlist";
    }

    /**
     * 通知消息
     * */
    @GetMapping("/user/message")
    @ApiOperation(value = "跳转通知信息页面",httpMethod = "GET")
    public String commonmessage(){
        return "/user/message/message";
    }
    /**
     * 弹出式通知消息
     * */
    @GetMapping("/user/alertmessage")
    @ApiOperation(value = "跳转弹出式通知消息页面",httpMethod = "GET")
    public String alertmessage(){
        return "/user/message/alertmessage";
    }
    /**
     * 跳转到产品清单界面
     * */
    @GetMapping("/product-listing")
    @ApiOperation(value = "跳转产品清单页面",httpMethod = "GET")
    public String toproductlisting() {
        return "/common/product-listing";
    }

    /**
     * 跳转到产品清单搜索界面
     * */
    @GetMapping("/product-search")
    @ApiOperation(value = "跳转产品清单搜索页面",httpMethod = "GET")
    public String toProductSearchs(String keys, ModelMap modelMap) {
        if(keys==null){
            return "/error/404";
        }
        modelMap.put("keys",keys);
        return "/common/product-search";
    }

    /**用户个人中心默认展示图*/
    @GetMapping("/home/console")
    @ApiOperation(value = "跳转个人中心默认展示图页面",httpMethod = "GET")
    public String homeconsole(){
        return "/admin/home/console";
    }

    /**
     * 管理员首页默认展示图
     * */
    @GetMapping("/echars/console")
    @ApiOperation(value = "跳转管理员默认展示图页面",httpMethod = "GET")
    public String echars(){
        return "/admin/echars/console";
    }


    @GetMapping("/app/message/index")
    @ApiOperation(value = "跳转message/index页面",httpMethod = "GET")
    public String appmessageindex(){
        return "/admin/app/message/index";
    }

    /**
     * 用户收藏列表
     * */
    @GetMapping("/user/collect")
    @ApiOperation(value = "跳转收藏列表页面",httpMethod = "GET")
    public String usercollect(){
        return "/user/collect/collectlist";
    }

    /**
     * 用户售出记录
     * */
    @GetMapping("/user/sold")
    @ApiOperation(value = "跳转售出记录页面",httpMethod = "GET")
    public String sold(){
        return "/user/sold/soldrecord";
    }

    /**
     * 销量列表
     * */
    @GetMapping("/admin/sold")
    @ApiOperation(value = "跳转销售列表页面",httpMethod = "GET")
    public String adminSold(){
        return "/admin/sold/soldrecord";
    }

    /**
     * 首页公告清单
     * */
    @GetMapping("/user/newslist")
    @ApiOperation(value = "跳转首页公告清单页面",httpMethod = "GET")
    public String userNews(){
        return "/common/listnews";
    }

    /**
     * 管理员公告列表
     * */
    @GetMapping("/admin/newslist")
    @ApiOperation(value = "跳转管理员公告列表页面",httpMethod = "GET")
    public String adminNews(){
        return "/admin/news/newslist";
    }
}
