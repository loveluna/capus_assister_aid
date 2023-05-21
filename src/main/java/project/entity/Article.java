package project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hankcs.hanlp.HanLP;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import project.util.DateUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 用户文章表
 *
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
@TableName("article")
public class Article extends BaseEntity implements Serializable {
    /**
     * 文章的内容
     * /**
     * 用户文章表主键，自增长的文章编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章的唯一标识符
     */
    @TableField("article_id")
    private String articleId;

    /**
     * 发布文章的用户 ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 发布文章的用户昵称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 发布文章的用户头像链接
     */
    @TableField("user_image")
    private String userImage;

    /**
     * 文章类型
     */
    @TableField("article_type")
    private String articleType;

    /**
     * 文章的内容
     */
    @TableField("article_content")
    private String articleContent;

    /**
     * 文章中包含的图片链接
     */
    @TableField("article_image")
    private String articleImage;

    /**
     * 文章中包含的视频链接
     */
    @TableField("article_video")
    private String articleVideo;

    /**
     * 文章的浏览数
     */
    @TableField("views_count")
    private Integer viewsCount;

    @TableField(exist = false)
    private List<String> imgSrcs;

    public List<String> getImgSrcs() {
        return StringUtils.isEmpty(articleImage) ? Collections.emptyList() : Arrays.asList(articleImage.split("#"));
    }

    /**
     * 文章的评论数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 文章的点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 文章的创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 文章的来源，例如：Android 客户端、iPhone 客户端、网页版等
     */
    @TableField("source")
    private String source;

    /**
     * 发布文章的地理位置
     */
    @TableField("location")
    private String location;

    /**
     * 文章的话题标签
     */
    @TableField("topic")
    private String topic;

    /**
     * 文章类型
     */
    @TableField("category")
    private int category;

    /**
     * 文章是否为长微博，1 表示是，0 表示否
     */
    @TableField("is_long")
    private Boolean isLong;

    /**
     * 用户对文章的态度，例如：赞、踩、中立等
     */
    @TableField("attitude")
    private String attitude;

    private int status;


    @TableField(exist = false)
    private List<String> keywords;

    public List<String> getKeywords() {
        return HanLP.extractKeyword(this.articleContent, 3);
    }

    @TableField(exist = false)
    private List<Comment> comments;

    @TableField(exist = false)
    private String articleTimeDesc;
    public String getArticleTimeDesc() {
        return DateUtils.formatTime(this.createTime.getTime());
    }

    public enum Category {
        QUESTIONS(1),
        TECHNICAL_SHARING(2),
        EVENTS(3),
        OTHERS(4),
        TEXT_MODULE(5),
        IMAGE_MODULE(6);

        private int value;

        Category(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Category fromValue(int value) {
            for (Category category : Category.values()) {
                if (category.getValue() == value) {
                    return category;
                }
            }
            throw new IllegalArgumentException("No enum constant project.entity.Article.Category." + value);
        }
    }

}