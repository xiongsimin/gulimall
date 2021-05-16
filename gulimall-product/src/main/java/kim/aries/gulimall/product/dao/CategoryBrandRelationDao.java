package kim.aries.gulimall.product.dao;

import kim.aries.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-05-15 12:36:44
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updateCategoryNameById(@Param("catId") Long catId, @Param("name") String name);
}
