package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.Article;
import cn.edu.xsyu.campus.project.entity.Report;
import cn.edu.xsyu.campus.project.mapper.ArticleMapper;
import cn.edu.xsyu.campus.project.mapper.ReportMapper;
import cn.edu.xsyu.campus.project.service.ArticleService;
import cn.edu.xsyu.campus.project.service.ReportService;
import cn.edu.xsyu.campus.project.vo.ArticleRespVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Resource
    private ReportMapper reportMapper;

    @Resource
    private ArticleMapper articleMapper;

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

    @Override
    public List<Article> queryAllCommodity(Integer status, String userId, int page, int count) {
        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.eq(status != null, "status", status) // 根据文章状态查询
                .eq(userId != null, "reporter_id", userId) // 根据用户ID查询
                .last("LIMIT " + (page - 1) * count + ", " + count);

        List<Report> reports = this.list(wrapper);
        reports = Optional.ofNullable(reports).orElse(Collections.emptyList());
        List<Article> articles = reports.stream().map(report -> {
            Article article = articleMapper.selectOne(Wrappers.<Article>lambdaQuery().eq(Article::getArticleId, report.getReportedId()));
            article.setReport(report);
            return article;
        }).collect(Collectors.toList());
        return articles;
    }

    @Override
    public Integer queryCommodityCount(Integer status, String userId) {
            QueryWrapper<Report> wrapper= new QueryWrapper<>();
            wrapper.eq(status != null, "status", status) // 根据文章状态查询
                    .eq(userId != null, "reporter_id", userId); // 根据用户ID查询
            return this.count(wrapper); // 统计符合条件的文章总数
    }
}
