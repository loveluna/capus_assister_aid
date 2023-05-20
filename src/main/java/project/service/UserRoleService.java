package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.UserInfo;
import project.entity.UserRole;
import project.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface  UserRoleService extends IService<UserRole> {
    boolean insertUserRole(UserRole userRole);

    Integer lookUserRoleId(String userId);

    boolean updateUserRole(UserRole userRole);
}
