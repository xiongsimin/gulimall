package kim.aries.gulimall.order.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.order.dao.OmsOrderReturnApplyDao;
import kim.aries.gulimall.order.entity.OmsOrderReturnApplyEntity;
import kim.aries.gulimall.order.service.OmsOrderReturnApplyService;


@Service("omsOrderReturnApplyService")
public class OmsOrderReturnApplyServiceImpl extends ServiceImpl<OmsOrderReturnApplyDao, OmsOrderReturnApplyEntity> implements OmsOrderReturnApplyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmsOrderReturnApplyEntity> page = this.page(
                new Query<OmsOrderReturnApplyEntity>().getPage(params),
                new QueryWrapper<OmsOrderReturnApplyEntity>()
        );

        return new PageUtils(page);
    }

}