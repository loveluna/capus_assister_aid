package project.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import project.entity.BaseEntity;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class InitImVo<T> extends BaseEntity implements Serializable {
    private String msg;
    private Integer code;
    private T data;
}
