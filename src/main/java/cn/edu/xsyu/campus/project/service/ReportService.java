package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ReportService extends IService<Report> {

    boolean saveReport(Report report);
}
