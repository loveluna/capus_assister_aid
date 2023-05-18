package project.service;

import project.mapper.UserPermMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
@Service
@Transactional
public class UserPermService {
    @Resource
    private UserPermMapper userPermMapper;

    //查询用户的权限
    public List<String> LookPermsByUserid(Integer id){
        return userPermMapper.LookPermsByUserid(id);
    }
}
