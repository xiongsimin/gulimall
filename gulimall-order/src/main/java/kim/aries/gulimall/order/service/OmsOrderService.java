package kim.aries.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.order.entity.OmsOrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:17:39
 */
public interface OmsOrderService extends IService<OmsOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

