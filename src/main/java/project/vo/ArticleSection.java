package project.vo;

import lombok.Data;
import project.entity.Article;

import java.util.List;
import java.util.Map;

@Data
public class ArticleSection {
    private Map<String, List<ArticleSection>> sections;
    private Map<String, Integer> sectionsViewCount;
    private Map<String, Integer> sectionsNumberCount;

    public Map<String, Integer> getSectionsViewCount() {
        return sectionsViewCount;
    }

    public void setSectionsViewCount(Map<String, Integer> sectionsViewCount) {
        this.sectionsViewCount = sectionsViewCount;
    }

    public Map<String, Integer> getSectionsNumberCount() {
        return sectionsNumberCount;
    }

    public void setSectionsNumberCount(Map<String, Integer> sectionsNumberCount) {
        this.sectionsNumberCount = sectionsNumberCount;
    }
}
