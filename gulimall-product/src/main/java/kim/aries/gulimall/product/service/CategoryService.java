package kim.aries.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 17:02:01
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    /**
     * @param catIds
     */
    void removeMenuByIds(List<Long> catIds);

    /**
     * 查询分类路径，如[2,22,225]
     *
     * @param catelogId
     * @return
     */
    Long[] getCatelogPath(Long catelogId);
}

