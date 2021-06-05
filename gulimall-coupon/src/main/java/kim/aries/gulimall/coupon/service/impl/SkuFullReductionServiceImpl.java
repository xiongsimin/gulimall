package kim.aries.gulimall.coupon.service.impl;

import kim.aries.common.to.MemberPrice;
import kim.aries.common.to.SkuReductionTo;
import kim.aries.gulimall.coupon.entity.MemberPriceEntity;
import kim.aries.gulimall.coupon.entity.SkuLadderEntity;
import kim.aries.gulimall.coupon.service.MemberPriceService;
import kim.aries.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.coupon.dao.SkuFullReductionDao;
import kim.aries.gulimall.coupon.entity.SkuFullReductionEntity;
import kim.aries.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;
    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //6.4、sku的优惠满减等信息 gulimall_sms库->sms_sku_ladder、sms_sku_full_reduction、sms_member_price
        //1、sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuReductionTo.getFullCount()>0){
            skuLadderService.save(skuLadderEntity);
        }
        //2、sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if(skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal(0))==1){
            this.save(skuFullReductionEntity);
        }
        //3、sms_member_price
        List<MemberPrice> memberPrices = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntities = memberPrices.stream().map(e -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(e.getId());
            memberPriceEntity.setMemberLevelName(e.getName());
            memberPriceEntity.setMemberPrice(e.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter(member->{
            return member.getMemberPrice().compareTo(new BigDecimal(0))==1;
        }).collect(Collectors.toList());

        memberPriceService.saveBatch(memberPriceEntities);

    }
}