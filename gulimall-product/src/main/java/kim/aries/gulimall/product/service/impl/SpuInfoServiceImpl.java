package kim.aries.gulimall.product.service.impl;

import kim.aries.common.constant.ProductConstant;
import kim.aries.common.to.SkuHasStockVo;
import kim.aries.common.to.SkuReductionTo;
import kim.aries.common.to.SpuBoundsTo;
import kim.aries.common.to.es.SkuEsModel;
import kim.aries.common.utils.R;
import kim.aries.gulimall.product.dao.SpuInfoDescDao;
import kim.aries.gulimall.product.entity.*;
import kim.aries.gulimall.product.feign.CouponFeignService;
import kim.aries.gulimall.product.feign.SearchFeignService;
import kim.aries.gulimall.product.feign.WareFeignService;
import kim.aries.gulimall.product.service.*;
import kim.aries.gulimall.product.vo.spusave.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    AttrService attrService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    WareFeignService wareFeignService;
    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //TODO 高级篇中需要考虑分布式事务
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        //1、保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        spuInfoEntity.setCatalogId(spuSaveVo.getCatelogId());
        this.saveBaseSpuInfo(spuInfoEntity);
        //2、保存spu的描述图片 pms_spu_info_desc
        List<String> descs = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", descs));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        //3、保存spu的图片集 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        //4、保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setAttrId(attr.getAttrId());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttrValue(productAttrValueEntities);
        //5、保存spu的积分信息 sms_spu_bounds（跨库）--远程调用
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R r1 = couponFeignService.saveSpuBounds(spuBoundsTo);
        if (r1.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }
        //6、保存当前spu对应的所有sku信息
        //6.1、sku的基本信息 pms_sku_info
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(sku -> {
                String defaultImg = "";
                for (Images image : sku.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> skuImages = sku.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> {
                    // 图片链接为空的不保存
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //6.2、sku的图片信息 pms_sku_images
                skuImagesService.saveBatch(skuImages);
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = sku.getAttr().stream().map(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                //6.3、sku的销售属性信息 pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.4、sku的优惠满减等信息 gulimall_sms库->sms_sku_ladder、sms_sku_full_reduction
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                //满减或打折信息存在才需要调用优惠券服务
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1) {
                    R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r2.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }


    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<SpuInfoEntity>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(e -> {
                e.eq("id", key).or().like("spu_name", key);
            });

        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            queryWrapper.eq("catalog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void up(Long spuId) {
        List<SkuEsModel> upProducts = new ArrayList<>();

        //组装需要的数据

        //查出当前spu对应的所有sku信息（包括品牌名等）
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);

        //TODO 4、在循环外查出spu的属性信息
        List<ProductAttrValueEntity> listForSpu = productAttrValueService.listforspu(spuId);
        List<Long> attrIds = listForSpu.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        //筛选出用于检索的属性
        List<Long> searchAttrIds = (attrIds != null && attrIds.size() > 0) ? attrService.getSearchAttrIds(attrIds) : new ArrayList<>();
        Set<Long> attrIdsSet = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> searchAttrs = listForSpu.stream().filter(item -> {
            return attrIdsSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());

        List<Long> skuIds = skuInfoEntities.stream().map(item -> {
            return item.getSkuId();
        }).collect(Collectors.toList());

        List<SkuHasStockVo> skuHasStockVos = new ArrayList<>();

        Map<Long, Boolean> hasStockMap = new HashMap<>();
        //TODO 1、发送远程调用，库存系统查询是否有库存
        if (skuIds != null && skuIds.size() > 0) {
            try {
                R rHasStock = wareFeignService.getSkuHasStock(skuIds);
                skuHasStockVos = (List<SkuHasStockVo>) rHasStock.get("data");
                hasStockMap = skuHasStockVos.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));

            } catch (Exception e) {
                log.error("库存服务查询异常，原因：{}", e);
            }

        }

        //封装每个sku的信息
        Map<Long, Boolean> finalHasStockMap = hasStockMap;
        List<SkuEsModel> skuEsModels = skuInfoEntities.stream().map(item -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(item, esModel);
            //有些属性名不一致，需要单独处理
            //skuPrice、skuImg
            esModel.setSkuPrice(item.getPrice());
            esModel.setSkuImg(item.getSkuDefaultImg());
            //hasStock、hotScore
            if (finalHasStockMap == null) {
                esModel.setHasStock(finalHasStockMap.get(item.getSkuId()));
            }
            //默认有库存
            else {
                esModel.setHasStock(true);
            }
            //TODO 2、热度评分 0 （后台可控）
            esModel.setHotScore(0L);
            //3、查询品牌和分类的名字信息
            //brandName、brandImg、catalogName、attrs
            BrandEntity brandEntity = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandImg(brandEntity.getLogo());

            CategoryEntity categoryEntity = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(categoryEntity.getName());
            //设置检索属性
            esModel.setAttrs(searchAttrs);
            return esModel;
        }).collect(Collectors.toList());
        //TODO 5、将数据发送给es进行保存

        R r = searchFeignService.productPutOnShelf(skuEsModels);
        if (r.getCode() == 0) {
            //修改spu状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            //调用失败
        }
    }

    private void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }
}