package cn.edu.xsyu.campus.project.entity.chat;

import cn.edu.xsyu.campus.project.entity.BaseEntity;
import cn.edu.xsyu.campus.project.entity.UserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ImData extends BaseEntity implements Serializable {
    private UserInfo mine;
    private List<Friend> friend;
    private List<Groups> group;
}
