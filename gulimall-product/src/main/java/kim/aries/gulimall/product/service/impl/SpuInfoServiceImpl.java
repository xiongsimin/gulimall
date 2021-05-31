package kim.aries.gulimall.product.service.impl;

import kim.aries.gulimall.product.vo.spusave.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.product.dao.SpuInfoDao;
import kim.aries.gulimall.product.entity.SpuInfoEntity;
import kim.aries.gulimall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        //1、保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        //2、保存spu的描述图片 pms_spu_info_desc

        //3、保存spu的图片集 pms_spu_images
        //4、保存spu的规格参数 pms_product_attr_value
        //5、保存spu的积分信息 sms_spu_bounds（跨库）
        //6、保存当前spu对应的所有sku信息（跨库）
        //6.1、sku的基本信息 pms_sku_info
        //6.2、sku的图片信息 pms_sku_images
        //6.3、sku的销售属性信息 pms_sku_sale_attr_value
        //6.4、sku的优惠满减等信息 gulimall_sms库->sms_sku_ladder、sms_sku_full_reduction、
    }

    private void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }
}