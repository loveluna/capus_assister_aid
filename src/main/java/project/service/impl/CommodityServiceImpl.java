package project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.Commodity;
import project.mapper.CommodityMapper;
import project.service.CommodityService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
@Service
@Transactional
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {
    @Resource
    private CommodityMapper commodityMapper;
    /**
     * 通过商品名模糊查询商品并按更新时间倒序分页展示
     * @param commname 商品名（模糊查询）
     * @param page 当前页码
     * @param count 每页展示数量
     * @return 分页查询结果
     */
    @Override
    public IPage<Commodity> queryCommodityByName(String commname, int page, int count) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.like("commname", commname) // 模糊查询商品名
                .eq("commstatus", 1) // 查询已上架的商品
                .orderByDesc("updatetime"); // 按更新时间倒序排序
        return this.page(new Page<>(page, count), wrapper); // 分页查询
    }

    /**
     * 统计模糊查询商品的总数
     * @param commname 商品名（模糊查询）
     * @return 商品总数
     */
    @Override
    public int queryCommodityByNameCount(String commname) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.like("commname", commname) // 模糊查询商品名
                .eq("commstatus", 1); // 查询已上架的商品
        return this.count(wrapper); // 统计符合条件的商品总数
    }

    /**
     * 分页展示各类状态的商品信息
     * @param commstatus 商品状态（可选）
     * @param userid 商品所有者ID（可选）
     * @param page 当前页码
     * @param count 每页展示数量
     * @return 分页查询结果
     */
    @Override
    public IPage<Commodity> queryAllCommodity(Integer commstatus, String userid, int page, int count) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.eq(commstatus != null, "commstatus", commstatus) // 根据商品状态查询
                .ne(commstatus == null, "commstatus", 2) // 根据用户ID查询
                .eq(userid != null, "userid", userid) // 根据用户ID查询
                .orderByDesc("updatetime"); // 按更新时间倒序排序
        return this.page(new Page<>(page, count), wrapper); // 分页查询
    }

    /**
     * 统计各类状态的商品总数
     * @param commstatus 商品状态（可选）
     ** @param userid 商品所有者ID（可选）
     * @return 商品总数
     */
    @Override
    public int queryCommodityCount(Integer commstatus, String userid) {
        QueryWrapper<Commodity> wrapper= new QueryWrapper<>();
        wrapper.eq(commstatus != null, "commstatus", commstatus) // 根据商品状态查询
                .ne(commstatus == null, "commstatus", 2) // 根据用户ID查询
                .eq(userid != null, "userid", userid); // 根据用户ID查询
        return this.count(wrapper); // 统计符合条件的商品总数
    }

    /**
     * 首页分类展示8条商品
     * @param category 商品分类
     * @return 商品列表
     */
    @Override
    public List<Commodity> queryCommodityByCategory(String category) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.eq(!"全部".equals(category), "category", category) // 根据商品分类查询（如果分类为“全部”，则查询所有商品）
                .eq("commstatus", 1) // 查询已上架的商品
                .orderByDesc("updatetime") // 按更新时间倒序排序
                .last("limit 0,8"); // 只查询8条记录
        return this.list(wrapper); // 查询符合条件的商品列表
    }

    /**
     * 分页展示指定类别、价格范围、地区和学校的商品信息
     * @param category 商品分类
     * @param minmoney 最小价格（可选）
     * @param maxmoney 最大价格（可选）
     * @param area 地区（“附近”或“本校”）
     * @param school 学校名称
     * @param page 当前页码
     * @param count 每页展示数量
     * @return 分页查询结果
     */
    @Override
    public IPage<Commodity> queryAllCommodityByCategory(String category, BigDecimal minmoney, BigDecimal maxmoney, String area, String school, int page, int count) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.eq(!"全部".equals(category), "category", category) // 根据商品分类查询（如果分类为“全部”，则查询所有商品）
                .between(minmoney != null && maxmoney != null, "thinkmoney", minmoney, maxmoney) // 根据价格范围查询
                .eq("commstatus", 1) // 查询已上架的商品
                .and(area.equals("附近"), i -> i.ne("school", school)) // 根据地区和学校查询（如果地区为“附近”，则排除指定学校的商品）
                .and(area.equals("本校"), i -> i.eq("school", school)) // 根据地区和学校查询（如果地区为“本校”，则查询指定学校的商品）
                .orderByDesc("updatetime"); // 按更新时间倒序排序
        return this.page(new Page<>(page, count), wrapper); // 分页查询
    }

    /**
     * 统计指定类别、价格范围、地区和学校的商品总数
     * @param category 商品分类
     * @param minmoney 最小价格（可选）
     * @param maxmoney 最大价格（可选）
     * @param area 地区（“附近”或“本校”）
     * @param school 学校名称
     * @return 商品总数
     */
    @Override
    public int queryAllCommodityByCategoryCount(String category, BigDecimal minmoney, BigDecimal maxmoney, String area, String school) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.eq(!"全部".equals(category), "category", category) // 根据商品分类查询（如果分类为“全部”，则查询所有商品）
                .between(minmoney != null && maxmoney != null, "thinkmoney", minmoney, maxmoney) // 根据价格范围查询
                .eq("commstatus", 1) // 查询已上架的商品
                .and(area.equals("附近"), i -> i.ne("school", school)) // 根据地区和学校查询（如果地区为“附近”，则排除指定学校的商品）
                .and(area.equals("本校"), i -> i.eq("school", school)); // 根据地区和学校查询（如果地区为“本校”，则查询指定学校的商品）
        return this.count(wrapper); // 统计符合条件的商品总数
    }

    /**
     * 添加商品
     * @param commodity 商品信息
     * @return 是否添加成功
     */
    @Override
    public boolean saveCommodity(Commodity commodity) {
        return this.save(commodity); // 添加商品并返回添加结果
    }

    /**
     * 根据商品ID查询商品详情
     * @param commid 商品ID
     * @return 商品详情
     */
    @Override
    public Commodity queryCommodityById(String commid) {
        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Commodity::getCommid, commid); // 根据商品ID查询
        return this.getOne(wrapper); // 查询并返回商品详情
    }

    /**
     * 修改商品信息
     * @param commodity 商品信息
     * @return 是否修改成功
     */
    @Override
    public boolean updateCommodity(Commodity commodity) {
        return this.updateById(commodity); // 修改商品信息并返回修改结果
    }

    /**
     * 修改商品状态（上架、下架）
     * @param commid 商品ID
     * @param commstatus 商品状态
     * @return 是否修改成功
     */
    @Override
    public boolean updateCommstatus(String commid, Integer commstatus) {
        UpdateWrapper<Commodity> wrapper = new UpdateWrapper<>();
        wrapper.eq("commid", commid) // 根据商品ID查询
                .set("commstatus", commstatus); // 修改商品状态
        return this.update(wrapper); // 修改商品状态并返回修改结果
    }
}
