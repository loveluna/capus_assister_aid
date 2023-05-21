package project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import project.entity.Article;
import project.entity.UserInfo;

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
