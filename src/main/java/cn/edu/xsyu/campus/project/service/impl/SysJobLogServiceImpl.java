package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.SysJobLogEntity;
import cn.edu.xsyu.campus.project.mapper.SysJobLogMapper;
import cn.edu.xsyu.campus.project.service.SysJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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