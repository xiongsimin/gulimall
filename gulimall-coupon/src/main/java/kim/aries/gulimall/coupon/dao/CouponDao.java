package kim.aries.gulimall.coupon.dao;

import kim.aries.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-05 16:49:01
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
