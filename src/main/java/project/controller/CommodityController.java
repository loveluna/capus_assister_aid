package project.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.config.FileUploadProperties;
import project.entity.*;
import project.exception.BusinessException;
import project.service.*;
import project.util.DataResult;
import project.util.DateUtils;
import project.util.KeyUtil;
import project.util.StatusCode;
import project.vo.LayuiPageVo;
import project.vo.PageVo;
import project.vo.ResultVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@Controller
@Api(value = "CommdityController", tags = "商品相关")
public class CommodityController {
    @Resource
    private CommodityService commodityService;
    @Resource
    private CommimagesService commimagesService;
    @Resource
    private LoginService loginService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private SoldrecordService soldrecordService;
    @Resource
    private CollectService collectService;
    @Resource
    private NoticesService noticesService;
    @Resource
    private FileUploadProperties fileUploadProperties;

    /**
     * 跳转到发布商品
     */
    @ApiOperation(value = "跳转发布商品", httpMethod = "GET")
    @GetMapping("/user/relgoods")
    public String torelgoods(HttpSession session) {
        /*String userid = (String)session.getAttribute("userid");
        if(userid==null){
            return "redirect:/:";
        }*/
        return "/user/product/relgoods";
    }

    /**
     * 跳转到修改商品
     * --不能修改已删除、已完成的商品
     * 1、查询商品详情
     * 2、查询商品得其他图
     */
    @GetMapping("/user/editgoods/{commid}")
    @ApiOperation(value = "跳转至修改商品", httpMethod = "GET")
    public String toeditgoods(@PathVariable("commid") String commid, HttpSession session, ModelMap modelMap) {
        /*String userid = (String)session.getAttribute("userid");
        if(userid==null){
            return "redirect:/:";
        }*/
        Commodity commodity = commodityService.queryCommodityById(commid);
        if (commodity.getCommstatus().equals(2) || commodity.getCommstatus().equals(4)) {
            return "/error/404";//商品已被删除或已完成交易
        }
        String[] commons = commodity.getCommon().split("、");
        commodity.setCommon(commons[0]).setCommon2(commons[1]);
        modelMap.put("goods", commodity);
        modelMap.put("otherimg", commimagesService.findImagesByCommId(commid));
        return "/user/product/changegoods";
    }

    /**
     * 修改商品
     * 1、修改商品信息
     * 2、修改商品的其他图的状态
     * 3、插入商品的其他图
     */
    @PostMapping("/changegoods/rel")
    @ResponseBody
    @ApiOperation(value = "修改商品", httpMethod = "POST", response = String.class)
    public String changegoods(@RequestBody Commodity commodity, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        commodity.setUpdatetime(new Date()).setCommstatus(3);
        commodity.setCommon(commodity.getCommon() + "、" + commodity.getCommon2());//常用选项拼接
        commodityService.updateCommodity(commodity);
        commimagesService.delGoodImages(commodity.getCommid());
        List<Commimages> commimagesList = new ArrayList<>();
        for (String list : commodity.getOtherimg()) {
            commimagesList.add(new Commimages().setId(KeyUtil.genUniqueKey()).setCommid(commodity.getCommid()).setImage(list));
        }
        commimagesService.insertGoodImages(commimagesList);
        /**发出待审核系统通知*/
        Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("商品审核")
                .setWhys("您的商品 <a href=/product-detail/" + commodity.getCommid() + " style=\"color:#08bf91\" target=\"_blank\" >" + commodity.getCommname() + "</a> 进入待审核队列，请您耐心等待。");
        noticesService.insertNotices(notices);
        return "0";
    }

    /**
     * 发布商品
     * 1、插入商品信息
     * 2、插入商品其他图
     */
    @PostMapping("/relgoods/rel")
    @ResponseBody
    @ApiOperation(value = "发布商品", httpMethod = "POST", response = String.class)
    public String relgoods(@RequestBody Commodity commodity, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        UserInfo userInfo = userInfoService.lookUserinfo(userid);
        String commid = KeyUtil.genUniqueKey();
        commodity.setCommid(commid).setUserid(userid).setSchool(userInfo.getSchool());//商品id
        commodity.setCommon(commodity.getCommon() + "、" + commodity.getCommon2());//常用选项拼接
        commodityService.saveCommodity(commodity);
        List<Commimages> commimagesList = new ArrayList<>();
        for (String list : commodity.getOtherimg()) {
            commimagesList.add(new Commimages().setId(KeyUtil.genUniqueKey()).setCommid(commid).setImage(list));
        }
        commimagesService.insertGoodImages(commimagesList);
        /**发出待审核系统通知*/
        Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("商品审核")
                .setWhys("您的商品 <a href=/product-detail/" + commid + " style=\"color:#08bf91\" target=\"_blank\" >" + commodity.getCommname() + "</a> 进入待审核队列，请您耐心等待。");
        noticesService.insertNotices(notices);
        return "0";
    }

    /**
     * 上传视频和主图
     */
    @PostMapping("/relgoods/video")
    @ResponseBody
    public DataResult relgoodsvideo(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        //存储文件夹
        String createTime = DateUtils.format(new Date(), DateUtils.DATEPATTERN);
        String newPath = fileUploadProperties.getPath() + createTime + File.separator;
        File uploadDirectory = new File(newPath);
        if (uploadDirectory.exists()) {
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.delete();
            }
        } else {
            uploadDirectory.mkdir();
        }
        try {
            String fileName = file.getOriginalFilename();
            //id与filename保持一直，删除文件
            String fileNameNew = UUID.randomUUID().toString().replace("-", "") + getFileType(fileName);
            String newFilePathName = newPath + fileNameNew;
            String url = fileUploadProperties.getUrl() + "/" + createTime + "/" + fileNameNew;
            //创建输出文件对象
            File outFile = new File(newFilePathName);
            //拷贝文件到输出文件对象
            FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("src", url);
            return DataResult.success(resultMap);
        } catch (Exception e) {
            throw new BusinessException("上传文件失败");
        }
    }

    /**
     * 上传其他图片
     */
    @PostMapping(value = "/relgoods/images")
    @ResponseBody
    @ApiOperation(value = "上传其他照片", httpMethod = "POST", response = DataResult.class)
    public DataResult relgoodsimages(@RequestParam(value = "file", required = false) MultipartFile[] files) throws IOException {
        // 存储文件夹
        String createTime = DateUtils.format(new Date(), DateUtils.DATEPATTERN);
        String uploadDirectoryPath = fileUploadProperties.getPath() + createTime + File.separator;
        File uploadDirectory = new File(uploadDirectoryPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileName = file.getOriginalFilename();
                // 生成新的文件名
                String fileExtension = getFileType(fileName);
                String newFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;
                String newFilePath = uploadDirectoryPath + newFileName;
                String imageUrl = fileUploadProperties.getUrl() + "/" + createTime + "/" + newFileName;
                // 创建输出文件对象
                File outFile = new File(newFilePath);
                // 拷贝文件到输出文件对象
                FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
                imageUrls.add(imageUrl);
            } catch (Exception e) {
                throw new BusinessException("上传文件失败");
            }
        }

        Map<String, List<String>> resultMap = new HashMap<>();
        resultMap.put("src", imageUrls);
        return DataResult.success(resultMap);
    }

    /**
     * 商品详情
     * 根据商品id（commid）查询商品信息、用户昵称及头像
     * 用户可以查看正常的商品
     * 商品发布者和管理员可以查看
     */
    @GetMapping("/product-detail/{commid}")
    @ApiOperation(value = "商品详情", httpMethod = "GET", response = String.class)
    public String product_detail(@PathVariable("commid") String commid, ModelMap modelMap, HttpSession session) {
        String couserid = (String) session.getAttribute("userid");

        Commodity commodity = commodityService.queryCommodityById(commid);
        int i = 0;
        if (commodity.getCommstatus().equals(1)) {//如果商品正常
            i = 1;
        } else if (!StringUtils.isEmpty(couserid)) {//如果用户已登录
            Login login = loginService.userLogin(new Login().setUserid(couserid));
            /**商品为违规状态时：本人和管理员可查看*/
            if (commodity.getCommstatus().equals(0) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2) || login.getRoleid().equals(3)))) {
                i = 1;
                /**商品为待审核状态时：本人和管理员可查看*/
            } else if (commodity.getCommstatus().equals(3) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2) || login.getRoleid().equals(3)))) {
                i = 1;
                /**商品为已售出状态时：本人和管理员可查看*/
            } else if (commodity.getCommstatus().equals(4) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2) || login.getRoleid().equals(3)))) {
                i = 1;
            }
        }
        if (i == 1) {
            commodity.setOtherimg(commimagesService.findImagesByCommId(commid));
            /**商品浏览量+1*/
            commodityService.updateCommodity(commodity.setRednumber(commodity.getRednumber() + 1));
            modelMap.put("userinfo", userInfoService.queryPartInfo(commodity.getUserid()));
            modelMap.put("gddetail", commodity);
            //如果没有用户登录
            if (StringUtils.isEmpty(couserid)) {
                modelMap.put("collectstatus", 2);
            } else {
                Collect collect = collectService.queryCollectStatus(commid, couserid);
                if (collect != null) {
                    if (collect.getCollstatus() == 2) {
                        modelMap.put("collectstatus", 2);
                    } else {
                        modelMap.put("collectstatus", 1);
                    }
                } else {
                    modelMap.put("collectstatus", 2);
                }
            }
            return "/common/product-detail";
        } else {
            return "/error/404";
        }
    }

    /**
     * 搜索+
     * 商
     * 8/963.品分页数\据
     * 前端传入搜索的商品名（commname）
     */
    @GetMapping("/product/search/number/{commname}")
    @ResponseBody
    @ApiOperation(value = "搜索商品的分页数据", httpMethod = "GET", response = PageVo.class)
    public PageVo searchCommodityNumber(@PathVariable("commname") String commname) {
        Integer dataNumber = commodityService.queryCommodityByNameCount(commname);
        return new PageVo(StatusCode.OK, "查询成功", dataNumber);
    }

    /**
     * 搜索商品
     * 前端传入当前页数（nowPaging）、搜索的商品名（commname）
     */
    @GetMapping("/product/search/{nowPaging}/{commname}")
    @ResponseBody
    @ApiOperation(value = "搜索商品", httpMethod = "GET", response = ResultVo.class)
    public ResultVo searchCommodity(@PathVariable("nowPaging") Integer page, @PathVariable("commname") String commname) {
        IPage<Commodity> commodityIPage = commodityService.queryCommodityByName(commname, (page - 1) * 20, 20);
        List<Commodity> commodityList = commodityIPage.getRecords();
        if (!StringUtils.isEmpty(commodityList)) {//如果有对应商品
            for (Commodity commodity : commodityList) {
                /**查询商品对应的其它图片*/
                List<String> imagesList = commimagesService.findImagesByCommId(commodity.getCommid());
                commodity.setOtherimg(imagesList);
            }
            return new ResultVo(true, StatusCode.OK, "查询成功", commodityList);
        } else {
            return new ResultVo(true, StatusCode.ERROR, "没有相关商品");
        }
    }

    /**
     * 首页分类展示商品 --> 按照分类查询商品
     * 前端传入商品类别（category）
     */
    @ResponseBody
    @GetMapping("/index/product/{category}")
    @ApiOperation(value = "首页分类查询商品", httpMethod = "POST", response = ResultVo.class)
    public ResultVo indexCommodity(@PathVariable("category") String category) {
        List<Commodity> commodityList = commodityService.queryCommodityByCategory(category);
        for (Commodity commodity : commodityList) {
            /**查询商品对应的其它图片*/
            List<String> imagesList = commimagesService.findImagesByCommId(commodity.getCommid());
            commodity.setOtherimg(imagesList);
        }
        return new ResultVo(true, StatusCode.OK, "查询成功", commodityList);
    }

    /**
     * 查询最新发布的8条商品
     */
    @ResponseBody
    @GetMapping("/product/latest")
    @ApiOperation(value = "返回最新8条商品", httpMethod = "GET", response = ResultVo.class)
    public ResultVo latestCommodity() {
        String category = "全部";
        List<Commodity> commodityList = commodityService.queryCommodityByCategory(category);
        for (Commodity commodity : commodityList) {
            /**查询商品对应的其它图片*/
            List<String> imagesList = commimagesService.findImagesByCommId(commodity.getCommid());
            commodity.setOtherimg(imagesList);
        }
        return new ResultVo(true, StatusCode.OK, "查询成功", commodityList);
    }

    /**
     * 产品清单分页数据
     * 前端传入商品类别（category）、区域（area）
     * 最低价（minmoney）、最高价（maxmoney）
     * 后端根据session查出个人本校信息（school）
     */
    @GetMapping("/list-number/{category}/{area}/{minmoney}/{maxmoney}")
    @ResponseBody
    @ApiOperation(value = "产品清单分页数据", httpMethod = "GET", response = PageVo.class)
    public PageVo productListNumber(@PathVariable("category") String category, @PathVariable("area") String area,
                                    @PathVariable("minmoney") BigDecimal minmoney, @PathVariable("maxmoney") BigDecimal maxmoney,
                                    HttpSession session) {
        String school = null;
        if (!area.equals("全部")) {
            String userid = (String) session.getAttribute("userid");
            UserInfo userInfo = userInfoService.lookUserinfo(userid);
            school = userInfo.getSchool();
        }
        Integer dataNumber = commodityService.queryAllCommodityByCategoryCount(category, minmoney, maxmoney, area, school);
        return new PageVo(StatusCode.OK, "查询成功", dataNumber);
    }

    /**
     * 产品清单界面
     * 前端传入商品类别（category）、当前页码（nowPaging）、区域（area）
     * 最低价（minmoney）、最高价（maxmoney）、价格升序降序（price：0.不排序 1.升序 2.降序）
     * 后端根据session查出个人本校信息（school）
     */
    @GetMapping("/product-listing/{category}/{nowPaging}/{area}/{minmoney}/{maxmoney}/{price}")
    @ResponseBody
    @ApiOperation(value = "产品清单页面", httpMethod = "GET", response = ResultVo.class)
    public ResultVo productlisting(@PathVariable("category") @ApiParam(value = "商品分类目录") String category, @PathVariable("nowPaging") Integer page,
                                   @PathVariable("area") String area, @PathVariable("minmoney") BigDecimal minmoney, @PathVariable("maxmoney") BigDecimal maxmoney,
                                   @PathVariable("price") Integer price, HttpSession session) {
        String school = null;
        if (!area.equals("全部")) {
            String userid = (String) session.getAttribute("userid");
            UserInfo userInfo = userInfoService.lookUserinfo(userid);
            school = userInfo.getSchool();
        }
        IPage<Commodity> commodityIPage = commodityService.queryAllCommodityByCategory(category, minmoney, maxmoney, area, school, (page - 1) * 16, 16);
        List<Commodity> commodityList = commodityIPage.getRecords();
        for (Commodity commodity : commodityList) {
            /**查询商品对应的其它图片*/
            List<String> imagesList = commimagesService.findImagesByCommId(commodity.getCommid());
            commodity.setOtherimg(imagesList);
        }

        /**自定义排序*/
        if (price != 0) {
            if (price == 1) {
                /**升序*/
                Collections.sort(commodityList, new Comparator<Commodity>() {//此处创建了一个匿名内部类
                    int i;

                    @Override
                    public int compare(Commodity o1, Commodity o2) {
                        if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) > -1) {
                            System.out.println("===o1大于等于o2===");
                            i = 1;
                        } else if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) < 1) {
                            i = -1;
                            System.out.println("===o1小于等于o2===");
                        }
                        return i;
                    }
                });
            } else if (price == 2) {
                /**降序*/
                Collections.sort(commodityList, new Comparator<Commodity>() {//此处创建了一个匿名内部类
                    int i;

                    @Override
                    public int compare(Commodity o1, Commodity o2) {
                        if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) > -1) {
                            System.out.println("===o1大于等于o2===");
                            i = -1;
                        } else if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) < 1) {
                            System.out.println("===o1小于等于o2===");
                            i = 1;
                        }
                        return i;
                    }
                });
            }
        }
        return new ResultVo(true, StatusCode.OK, "查询成功", commodityList);
    }

    /**
     * 分页展示个人各类商品信息
     * 前端传入页码、分页数量
     * 前端传入商品信息状态码（commstatus）-->全部:100，已审核:1，待审核:3，违规:0，已完成:4
     */
    @GetMapping("/user/commodity/{commstatus}")
    @ResponseBody
    @ApiOperation(value = "分页展示个人各类商品信息", httpMethod = "GET", response = LayuiPageVo.class)
    public LayuiPageVo userCommodity(@PathVariable("commstatus") Integer commstatus, int limit, int page, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        //如果未登录，给一个假id
        if (StringUtils.isEmpty(userid)) {
            userid = "123456";
        }
        List<Commodity> commodityList = null;
        Integer dataNumber;
        if (commstatus == 100) {
            IPage<Commodity> commodityIPage = commodityService.queryAllCommodity(null, userid, (page - 1) * limit, limit);
            commodityList = commodityIPage.getRecords();
            dataNumber = commodityService.queryCommodityCount(null, userid);
        } else {
            IPage<Commodity> commodityIPage = commodityService.queryAllCommodity(commstatus, userid, (page - 1) * limit, limit);
            commodityList = commodityIPage.getRecords();
            dataNumber = commodityService.queryCommodityCount(commstatus, userid);
        }
        return new LayuiPageVo("", 0, dataNumber, commodityList);
    }

    /**
     * 个人对商品的操作
     * 前端传入商品id（commid）
     * 前端传入操作的商品状态（commstatus）-->删除:2  已完成:4
     */
    @ResponseBody
    @GetMapping("/user/changecommstatus/{commid}/{commstatus}")
    @ApiOperation(value = "个人岁商品操作返回状态", httpMethod = "GET", response = ResultVo.class)
    public ResultVo ChangeCommstatus(@PathVariable("commid") String commid, @PathVariable("commstatus") Integer commstatus, HttpSession session) {
        if (commodityService.updateCommstatus(commid, commstatus)) {
            /**如果商品已售出*/
            if (commstatus == 4) {
                String userid = (String) session.getAttribute("userid");
                /**查询售出商品的信息*/
                Commodity commodity = commodityService.queryCommodityById(commid);
                Soldrecord soldrecord = new Soldrecord();
                /**将商品信息添加到售出记录中*/
                soldrecord.setId(KeyUtil.genUniqueKey()).setCommid(commid).setCommname(commodity.getCommname()).setCommdesc(commodity.getCommdesc())
                        .setThinkmoney(commodity.getThinkmoney()).setUserid(userid);
                /**添加售出记录*/
                soldrecordService.insertSold(soldrecord);
            }
            return new ResultVo(true, StatusCode.OK, "操作成功");
        }
        return new ResultVo(false, StatusCode.ERROR, "操作失败");
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

