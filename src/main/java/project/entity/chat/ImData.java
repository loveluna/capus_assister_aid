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
public class ImData extends BaseEntity implements Serializable {
    private UserInfo mine;
    private List<Friend> friend;
    private List<Groups> group;
}
