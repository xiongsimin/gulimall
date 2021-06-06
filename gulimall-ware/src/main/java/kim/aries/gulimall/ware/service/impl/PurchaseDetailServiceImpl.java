package kim.aries.gulimall.ware.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.ware.dao.PurchaseDetailDao;
import kim.aries.gulimall.ware.entity.PurchaseDetailEntity;
import kim.aries.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(w -> {
                w.eq("purchase_id", key)
                        .or().eq("sku_id", key);
            });
        }
        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", status);
        }
        String wareId = (String) params.get("wareId");
        if (StringUtils.isNotBlank(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailsByPurchaseId(Long id) {
        return this.baseMapper.selectList(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
    }

}