package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.News;
import cn.edu.xsyu.campus.project.mapper.NewsMapper;
import cn.edu.xsyu.campus.project.service.NewsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Resource
    private NewsMapper newsMapper;
    /**
     * 发布公告
     *
     * @param news 公告信息
     * @return 是否发布成功
     */
    @Override
    public boolean insertNews(News news) {
        return baseMapper.insert(news) == 1;
    }

    /**
     * 删除公告
     *
     * @param id 公告ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteNews(String id) {
        UpdateWrapper<News> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("newsstatus", 2)
                .eq("id", id);
        return baseMapper.update(null, updateWrapper) == 1;
    }

    /**
     * 修改公告
     *
     * @param news 公告信息
     * @return 是否修改成功
     */
    @Override
    public boolean updateNews(News news) {
        LambdaUpdateWrapper<News> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(news.getNewstitle() != null, News::getNewstitle, news.getNewstitle())
                .set(news.getNewsdesc() != null, News::getNewsdesc, news.getNewsdesc())
                .set(news.getImage() != null, News::getImage, news.getImage())
                .set(news.getNewscontent() != null, News::getNewscontent, news.getNewscontent())
                .eq(News::getId, news.getId());
        return baseMapper.update(null, updateWrapper) == 1;
    }

    /**
     * 查看公告详情
     *
     * @param id 公告ID
     * @return 公告详情
     */
    @Override
    public News queryNewsById(String id) {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(News::getId, id)
                .eq(News::getNewsstatus, 1);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 添加公告浏览量
     *
     * @param id 公告ID
     * @return 是否添加成功
     */
    @Override
    public boolean addNewsRednumber(String id) {
        UpdateWrapper<News> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("rednumber = rednumber + 1")
                .eq("id", id)
                .eq("newsstatus", 1);
        return baseMapper.update(null, updateWrapper) == 1;
    }

    /**
     *分页展示公告信息
     *
     * @param page  分页页码
     * @param count 分页大小
     * @return 公告信息列表
     */
    @Override
    public List<News> queryAllNews(Integer page, Integer count) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "newstitle", "image", "newsdesc", "username", "createtime", "rednumber")
                .eq("newsstatus", 1)
                .orderByDesc("createtime")
                .last("LIMIT " + (page - 1) * count + ", " + count);
        return newsMapper.selectList(queryWrapper);
    }

    /**
     * 查找所有公告的总数
     *
     * @return 公告总数
     */
    @Override
    public Integer LookNewsCount() {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("newsstatus", 1);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 查询前三条公告
     *
     * @return 前三条公告列表
     */
    @Override
    public List<News> queryNews() {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(News::getNewsstatus, 1)
                .orderByDesc(News::getCreatetime)
                .last("LIMIT 0, 3");
        return baseMapper.selectList(queryWrapper);
    }
}