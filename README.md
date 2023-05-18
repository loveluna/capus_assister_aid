# 咸鱼二手交易平台



#### 软件架构
Springboot+shiro+vue+Mybatis+MySQL

### 一、功能：
  #### 登陆、注册、重置密码模块
  1. 登录：登录可以使用手机号或用户名进行登录
  2. 注册的时候会判断账号是否以及存在，用户名是否已存在。
     
     会判断手机号及邮箱的格式，但不会判断邮箱是否已使用过。
     
     发送短信验证时也会对手机号进行查重，验证码五分钟内有效。
  3. 重置密码时会验证手机号格式，判断是否存在该用户，  短信验证码五分钟有效。 

  #### 商品模块
  1. 发布商品
  2. 修改商品
  3. 商品详情界面
  4. 首页界面
  5. 商品列表界面
  6. 商品搜索界面

  #### 评论模块
  1. 查看商品下所有评论及回复
  2. 评论回复以及对应删除操作

  #### 个人中心
  1. 修改个人信息
  2. 修改密码
  3. 分页展示个人各类商品信息 已审核:1，待审核:3，违规:0，已完成:4
  4. 个人对商品的操作 删除:2  已完成:4
  5. 更换手机号

  #### 收藏模块
  1. 商品详情界面：收藏商品or取消收藏
  2. 收藏列表界面取消收藏
  3. 分页查看用户所有收藏内容
  4. 查看商品收藏状态

  #### 售出记录模块
  1. 用户设置商品为售出状态时存入售出记录
  2. 用户分页查看所有的售出记录
  3. 用户删除售出记录

  #### 聊天模块
  1.可以进行在线聊天
  采用的是websocket的方式，实现网络通信。

  #### 公告模块
  1. 发布公告
  2. 默认展示最新发布的前三条公告
  3. 公告详情的展示
  4. 修改公告及删除公告
  5. 删除公告

  #### 后台操作功能
  1. 管理员登录
  2. 查看用户列表
  3. 给予用户封号
  4. 管理员审核商品
  5. 管理员分页展示各类商品信息 全部，已审核:1，待审核:3，违规:0 完成:4
  6. 管理员对商品的操作 违规:0 通过审核:1

  #### 网站公告
  1. 管理员发布网站公告
  2. 管理员本人修改或删除公告
  3. 查看网站公告

  #### 通知模块
  1. 评论回复、商品审核、私信发送通知
  2. 设置为管理员或用户发送通知

  #### 支付模块
  未实现。

  #### 安全
  1.使用shiro做授权和认证。

  #### 日志
  1.使用了阿里巴巴的druid数据库连接池，对sql执行做了日志监控。

  #### 错误页面
  1.自定义页面处理错误。

  #### 异常处理
   1.统一异常处理

#### 统一vo

统一了返回的json数据对象。

分为三个VO对象，

ResultVo  一般的json返回数据

 PageVo  首页的vo 

LayuiVo   laiui分页数据vo

  #### swagger api接口文档
   使用swagger生成api接口文档。
   启动项目，访问地址： 127.0.0.1/swagger-ui.html#

   ![](https://sign-1259371307.cos.ap-chongqing.myqcloud.com/1615620190698.png)

### 二、数据库设计

登陆表login，收藏表collect，评论表comment，回应表reply，商品图片信息表commimages，商品信息表commdity，好友表friends，公告表news，商品购买通知表notice，售卖记录表soldrecord，用户信息，用户角色。
![](https://sign-1259371307.cos.ap-chongqing.myqcloud.com/1615550791054.png)

### 三、后台架构

采用MVC架构,在dao层使用了Mybatis框架，实现对数据库的curd，service层调用dao层数据接口，拿到数据库的数据，但没有做太多业务逻辑，主要业务逻辑都放在了controller层，控制视图跳转。

![](https://sign-1259371307.cos.ap-chongqing.myqcloud.com/1615551807984.png)

#### 1.后台项目结构设计思路

 1).页面设计包括：index.html,comment.html,list.html,tags.html,replys.html,category.html,specs.html,detail.html,console.html,login.html,newslist.html,relnews.html,updatenews.html,soldrecord.html,about.html,get.html,more.html,theme.html.
    
 2.控制类设计分类：

controller控制器文件分adminController、UserController、聊天控制器ChatController、商品收藏CollectController、商品控制类CommodityController、评论CommentController、公告通知Noticesontroller,卖货记录SoldController
 以及IndexConroller(放所有的页面跳转)。
    
 3.统一返回的json格式

将返回json的数据分为四大类，LayuiPageVo(需要分页的数据)，PageVo（返回给首页的json数据），ResultVo(操作状态返回类json数据)，还有一类是普通自定义JsonObject类json数据。这样返回的数据格式做了有效统一。
   **LayuiPage**类型json数据格式： 

```java
 @ApiModelProperty(value = "",name = "msg",notes ="描述返回状态" )
 private String msg;
 @ApiModelProperty(value = "",name = "code",notes ="状态码" )
 private Integer code;	                    
 @ApiModelProperty(value = "",name = "count",notes ="数量" )                          
 private Integer count;                       
 @ApiModelProperty(value = "",name = "data",notes ="返回数据" )                        
 private T data;                                                    
```

   **PageVo**类型的json数据格式：

```java
@ApiModelProperty(value = "",name = "status",notes ="返回状态码" )
    private Integer status; //状态码
    @ApiModelProperty(value = "",name = "message",notes ="返回信息" )
    private String message; //返回信息
    @ApiModelProperty(value = "",name = "pages",notes ="描述返回页数" )
    private Integer pages;  //返回页数
    @ApiModelProperty(value = "",name = "dataNumber",notes ="描述总记录数" )
    private Integer dataNumber;//总记录数
    @ApiModelProperty(value = "",name = "data",notes ="返回数据" )
    private T data;    //返回数据
```

**ResultVo**类型json数据格式：

```java
@ApiModelProperty(value = "",name = "flag",notes ="描述是否成功" )
    private boolean flag;   //是否成功
    @ApiModelProperty(value = "",name = "status",notes ="描述返回状态码" )
    private Integer status; //状态码
    @ApiModelProperty(value = "",name = "message",notes ="描述返回信息" )
    private String message; //返回信息
    @ApiModelProperty(value = "",name = "data",notes ="返回数据" )
    private Object data;    //返回数据
```

4.命名规范

类名各单词首字母大写。

方法名 英文都小写。













###   四、项目启动部署

1.使用idea配置maven

2.修改application-prod.yml配置文件数据库username 和password（阿里云短音验证配置按照自身情况确定是否修改）

3.将项目中shop.sql文件导入数据库，数据库名为shop。（sql文件可能出现版本原因导入出错，建议MySQL5.7）

4.启动运行

5.所有项目登录账户密码都为123456

6.管理员登陆地址 127.0.0.1/admin







### 五、网站预览



  1.商品首页
![](https://sign-1259371307.cos.ap-chongqing.myqcloud.com/1615547794361.png)

  

2.商品详情
![](https://sign-1259371307.cos.ap-chongqing.myqcloud.com/1615547868554.png)

  3.个人中心

![](https://sign-1259371307.cos.ap-chongqing.myqcloud.com/1615547905809.png)

  

4.在线聊天

![](https://sign-1259371307.cos.ap-chongqing.myqcloud.com/1615547939625.png)






