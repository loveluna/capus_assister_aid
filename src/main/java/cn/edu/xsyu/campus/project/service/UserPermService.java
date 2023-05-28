package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.UserPerm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */

public interface UserPermService extends IService<UserPerm> {
    List<String> lookPermsByUserid(Integer roleId);
}
