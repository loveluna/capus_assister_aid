package cn.edu.xsyu.campus.project.service;

import cn.edu.xsyu.campus.project.entity.Commodity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */

public interface CommodityService extends IService<Commodity> {

    IPage<Commodity> queryCommodityByName(String commname, int page, int count);

    int queryCommodityByNameCount(String commname);

    IPage<Commodity> queryAllCommodity(Integer commstatus, String userid, int page, int count);

    int queryCommodityCount(Integer commstatus, String userid);

    List<Commodity> queryCommodityByCategory(String category);

    IPage<Commodity> queryAllCommodityByCategory(String category, BigDecimal minmoney, BigDecimal maxmoney, String area, String school, int page, int count);

    int queryAllCommodityByCategoryCount(String category, BigDecimal minmoney, BigDecimal maxmoney, String area, String school);

    boolean saveCommodity(Commodity commodity);

    Commodity queryCommodityById(String commid);

    boolean updateCommodity(Commodity commodity);

    boolean updateCommstatus(String commid, Integer commstatus);
}
