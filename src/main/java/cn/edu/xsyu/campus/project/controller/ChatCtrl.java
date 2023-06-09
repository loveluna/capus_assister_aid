package cn.edu.xsyu.campus.project.controller;

import cn.edu.xsyu.campus.project.entity.UserInfo;
import cn.edu.xsyu.campus.project.entity.chat.*;
import cn.edu.xsyu.campus.project.service.ChatmsgService;
import cn.edu.xsyu.campus.project.service.FriendsService;
import cn.edu.xsyu.campus.project.service.UserInfoService;
import cn.edu.xsyu.campus.project.util.StatusCode;
import cn.edu.xsyu.campus.project.vo.ResultVo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Api(value = "ChatCtrl", tags = "聊天功能实现")
public class ChatCtrl {
    @Resource
    FriendsService friendsService;
    @Resource
    UserInfoService userInfoService;
    @Resource
    ChatmsgService chatmsgService;

    /**
     * 上传聊天图片
     **/
    @PostMapping(value = "/chat/upimg")
    @ResponseBody
    @ApiOperation(value = "上传聊天图片", httpMethod = "POST", response = JsonObject.class)
    public JSONObject upimg(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());//获得文件扩展名
        String filenames = filename + "." + ext;
        file.transferTo(new File("D:\\campusshops\\file\\" + filenames));
        resUrl.put("src", "/static/pic/" + filenames);
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }

    /**
     * 上传聊天文件
     **/
    @PostMapping(value = "/chat/upfile")
    @ResponseBody
    @ApiOperation(value = "上传聊天文件", httpMethod = "POST", response = JsonObject.class)
    public JSONObject upfile(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());//获得文件扩展名
        String filenames = filename + "." + ext;
        file.transferTo(new File("D:\\campusshops\\file\\" + filenames));
        resUrl.put("src", "/static/pic/" + filenames);
        resUrl.put("name", file.getOriginalFilename());
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }

    /**
     * 添加好友跳转到个人中心聊天
     */
    @PutMapping("/addfrend/{fuserid}")
    @ResponseBody
    @ApiOperation(value = "添加好友跳转个人中心聊天", httpMethod = "PUT", response = ResultVo.class)
    public ResultVo addfrend(@PathVariable("fuserid") String fuserid, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        if (userid.equals(fuserid)) {
            //不能对自己的商品感兴趣
            return new ResultVo(false, StatusCode.ERROR, "不能对自己的商品感兴趣");
        }
        Friends friends = new Friends().setUserid(userid).setFuserid(fuserid);
        if (!friendsService.justTwoUserIsFriend(friends)) {
            //如果不存在好友关系插入好友关系
            friendsService.insertFriend(friends);
            friendsService.insertFriend(new Friends().setFuserid(userid).setUserid(fuserid));
        }
        return new ResultVo(false, StatusCode.OK, "正在跳转到聊天界面");
    }

    /**
     * TODO 跳转到聊天记录界面
     */
    @GetMapping("/tochatlog")
    @ApiOperation(value = "跳转聊天记录页面", httpMethod = "GET")
    public String tochatlog() {
        return "/user/chat/chatlog";
    }

    /**
     * TODO 查询聊天记录
     */
    @GetMapping("/chatlog/{uid}")
    @ResponseBody
    @ApiOperation(value = "查询聊天记录", httpMethod = "GET", response = List.class)
    public List<UserInfo> chatlog(@PathVariable("uid") String uid, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        List<UserInfo> mines = chatmsgService.getChatHistory(userid, uid);
        return mines;
    }

    /**
     * TODO 初始化聊天
     */
    @GetMapping("/initim")
    @ResponseBody
    @ApiOperation(value = "初始化聊天返回状态", httpMethod = "GET", response = InitImVo.class)
    public InitImVo initim(HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        InitImVo initImVo = new InitImVo();
        //个人信息
        UserInfo mine = friendsService.lookUserMine(userid);
        //好友列表
        List<UserInfo> list = friendsService.lookUserFriend(userid);
        Friend friend = new Friend().setId("2").setGroupname("分组").setList(list);
        List<Friend> friendList = new ArrayList<>();
        friendList.add(friend);
        //群组信息
        List<Groups> groupList = new ArrayList<>();
        //Data数据
        ImData imData = new ImData().setMine(mine).setFriend(friendList).setGroup(groupList);
        initImVo.setCode(0).setMsg("").setData(imData);
        return initImVo;
    }
}
