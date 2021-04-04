package kim.aries.gulimall.order.dao;

import kim.aries.gulimall.order.entity.OmsOrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:17:39
 */
@Mapper
public interface OmsOrderItemDao extends BaseMapper<OmsOrderItemEntity> {
	
}
