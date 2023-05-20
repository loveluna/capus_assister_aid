package project.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import project.entity.SysJobLogEntity;
import project.mapper.SysJobLogMapper;
import project.service.SysJobLogService;

/**
 * 定时任务 服务类
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
@Service("sysJobLogService")
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLogEntity> implements SysJobLogService {


}