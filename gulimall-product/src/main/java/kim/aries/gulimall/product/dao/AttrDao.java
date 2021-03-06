package kim.aries.gulimall.product.dao;

import kim.aries.gulimall.product.entity.AttrAttrgroupRelationEntity;
import kim.aries.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 17:02:01
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    /**
     * 筛选出用于检索的属性
     *
     * @param attrIds
     * @return
     */
    List<Long> getSearchAttrIds(@Param("attrIds") List<Long> attrIds);
}
