package cn.edu.xsyu.campus.project.entity.chat;


import cn.edu.xsyu.campus.project.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Groups extends BaseEntity implements Serializable {
    private String id;//群组ID
    private String groupname;//群组名
    private String avatar;//群组头像
    private String userid;//用户id
    private Date intime;//加入时间
    private String grpowner; //群主
}
