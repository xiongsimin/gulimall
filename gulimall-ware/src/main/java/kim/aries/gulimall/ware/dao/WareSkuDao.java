package kim.aries.gulimall.ware.dao;

import kim.aries.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kim.aries.common.to.SkuHasStockVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:23:39
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    /***
     * 查询库存
     * @param skuIds
     * @return
     */
    List<SkuHasStockVo> selectStock(@Param("skuIds") List<Long> skuIds);
}
