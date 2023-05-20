package project.entity.chat;

import lombok.EqualsAndHashCode;
import project.entity.BaseEntity;
import project.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Friend extends BaseEntity implements Serializable {
    private String groupname;//好友分组名
    private String id;//分组ID
    private List<UserInfo> list;//分组下的好友列表
}
