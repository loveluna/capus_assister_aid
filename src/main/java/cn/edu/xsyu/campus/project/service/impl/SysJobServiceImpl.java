package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.common.job.utils.ScheduleUtils;
import cn.edu.xsyu.campus.project.entity.SysJobEntity;
import cn.edu.xsyu.campus.project.exception.BusinessException;
import cn.edu.xsyu.campus.project.mapper.SysJobMapper;
import cn.edu.xsyu.campus.project.service.SysJobService;
import cn.edu.xsyu.campus.project.util.Constant;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务 服务类
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
@Service("sysJobService")
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJobEntity> implements SysJobService {
    @Resource
    private Scheduler scheduler;
    @Resource
    private SysJobMapper sysJobMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<SysJobEntity> scheduleJobList = this.list();
        for (SysJobEntity scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
            //如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    @Override
    public void saveJob(SysJobEntity sysJob) {
        sysJob.setStatus(Constant.SCHEDULER_STATUS_NORMAL);
        this.save(sysJob);

        ScheduleUtils.createScheduleJob(scheduler, sysJob);
    }

    @Override
    public void updateJobById(SysJobEntity sysJob) {
        SysJobEntity sysJobEntity = this.getById(sysJob.getId());
        if (sysJobEntity == null) {
            throw new BusinessException("获取定时任务异常");
        }
        sysJob.setStatus(sysJobEntity.getStatus());
        ScheduleUtils.updateScheduleJob(scheduler, sysJob);

        this.updateById(sysJob);
    }

    @Override
    public void delete(List<String> ids) {
        for (String jobId : ids) {
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        }
        sysJobMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(List<String> ids) {
        for (String jobId : ids) {
            ScheduleUtils.run(scheduler, this.getById(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(List<String> ids) {
        for (String jobId : ids) {
            ScheduleUtils.pauseJob(scheduler, jobId);
        }

        updateBatch(ids, Constant.SCHEDULER_STATUS_PAUSE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(List<String> ids) {
        for (String jobId : ids) {
            ScheduleUtils.resumeJob(scheduler, jobId);
        }

        updateBatch(ids, Constant.SCHEDULER_STATUS_NORMAL);
    }

    @Override
    public void updateBatch(List<String> ids, int status) {
        ids.parallelStream().forEach(id -> {
            SysJobEntity sysJobEntity = new SysJobEntity();
            sysJobEntity.setId(id);
            sysJobEntity.setStatus(status);
            baseMapper.updateById(sysJobEntity);
        });
    }
}