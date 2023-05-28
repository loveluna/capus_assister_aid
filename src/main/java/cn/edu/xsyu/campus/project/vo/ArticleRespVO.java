package cn.edu.xsyu.campus.project.vo;

import cn.edu.xsyu.campus.project.entity.Article;
import cn.edu.xsyu.campus.project.entity.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ArticleRespVO {
    @ApiModelProperty(value = "用户信息")
    private UserInfo userInfo;

    @ApiModelProperty(value = "文章")
    private Article article;

    @ApiModelProperty(value = "文章")
    private List<Article> articles;
}
