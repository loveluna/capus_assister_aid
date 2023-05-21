package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Report;

public interface ReportService extends IService<Report> {

    boolean saveReport(Report report);
}
