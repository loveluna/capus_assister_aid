package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface  UserRoleService extends IService<UserRole> {
    boolean insertUserRole(UserRole userRole);

    List<UserRole> getUserRoleIdByUserId(String userId);

    boolean updateUserRole(UserRole userRole);
}
