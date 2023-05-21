package project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import project.util.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)//链式写法
public class Comment extends BaseEntity implements Serializable {
    /**
     * 评论id
     */
    @TableId
    private String cid;
    /**
     * 商品id
     */
	private String commid;
    /**
     * 评论者id
     */
	private String cuserid;
    /**
     * 评论者昵称
     */
    @TableField(exist = false)
    private String cusername;
    /**
     * 评论者用户头像
     */
    @TableField(exist = false)
    private String cuimage;
    /**
     * 商品发布者id
     */
	private String spuserid;
    /**
     * 评论内容
     */
	private String content;
    /**
     * 评论时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 0异常 1正常 2删除
     */
	private Integer commstatus;
    /**
     * 评论对应的回复集合
     */
    @TableField(exist = false)
    private List<Reply> replyLsit;

    @TableField(exist = false)
    private String articleTimeDesc;
    public String getArticleTimeDesc() {
        if (this.createTime != null) {
            return DateUtils.formatTime(this.createTime.getTime());
        }
        return "";
    }
}
