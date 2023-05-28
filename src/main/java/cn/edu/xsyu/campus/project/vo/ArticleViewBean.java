package cn.edu.xsyu.campus.project.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ArticleViewBean {
    private Map<String, List<ArticleSection>> sections;
}