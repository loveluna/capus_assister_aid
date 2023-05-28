package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Article;
import cn.edu.xsyu.campus.project.entity.Report;
import cn.edu.xsyu.campus.project.vo.ArticleRespVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ReportService extends IService<Report> {

    boolean saveReport(Report report);

    List<Article> queryAllCommodity(Integer status , String userId, int i, int limit);

    Integer queryCommodityCount(Integer status , String userId);
}
