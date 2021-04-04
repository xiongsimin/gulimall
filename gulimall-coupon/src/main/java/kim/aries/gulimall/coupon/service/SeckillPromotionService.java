package kim.aries.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.coupon.entity.SeckillPromotionEntity;

import java.util.Map;

/**
 * 秒杀活动
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:20:45
 */
public interface SeckillPromotionService extends IService<SeckillPromotionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

