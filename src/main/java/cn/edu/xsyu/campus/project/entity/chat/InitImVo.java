package cn.edu.xsyu.campus.project.entity.chat;

import cn.edu.xsyu.campus.project.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class InitImVo<T> extends BaseEntity implements Serializable {
    private String msg;
    private Integer code;
    private T data;
}
