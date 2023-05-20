package project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.entity.Commimages;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */

public interface CommimagesService extends IService<Commimages> {
    void insertGoodImages(List<Commimages> commimagesList);

    List<String> findImagesByCommId(String commid);

    void delGoodImages(String commid);
}
