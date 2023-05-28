package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.Report;
import cn.edu.xsyu.campus.project.mapper.ReportMapper;
import cn.edu.xsyu.campus.project.service.ReportService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Resource
    private ReportMapper reportMapper;

    @Override
    public boolean saveReport(Report report) {
        Report existingReport = reportMapper.selectOne(Wrappers.<Report>lambdaQuery()
                .eq(Report::getReportedId, report.getReporterId())
                .eq(Report::getReporterId, report.getReporterId()).eq(Report::getStatus, 0));
        if (existingReport == null) {
            return reportMapper.insert(report) > 0;
        }else{
            return false;
        }
    }
}