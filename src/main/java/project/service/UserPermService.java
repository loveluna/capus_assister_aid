package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.UserInfo;
import project.entity.UserPerm;
import project.mapper.UserPermMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */

public interface UserPermService extends IService<UserPerm> {
    List<String> lookPermsByUserid(Integer roleId);
}
