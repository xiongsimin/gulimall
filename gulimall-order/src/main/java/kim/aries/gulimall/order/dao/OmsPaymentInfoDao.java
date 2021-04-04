package kim.aries.gulimall.order.dao;

import kim.aries.gulimall.order.entity.OmsPaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付信息表
 * 
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:17:39
 */
@Mapper
public interface OmsPaymentInfoDao extends BaseMapper<OmsPaymentInfoEntity> {
	
}
