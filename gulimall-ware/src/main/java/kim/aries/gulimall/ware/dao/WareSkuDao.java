package kim.aries.gulimall.ware.dao;

import kim.aries.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:23:39
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
