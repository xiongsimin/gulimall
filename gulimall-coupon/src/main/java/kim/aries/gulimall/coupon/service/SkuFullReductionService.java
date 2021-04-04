package kim.aries.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:20:45
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

