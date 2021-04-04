package kim.aries.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.order.entity.OmsOrderOperateHistoryEntity;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:17:39
 */
public interface OmsOrderOperateHistoryService extends IService<OmsOrderOperateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

