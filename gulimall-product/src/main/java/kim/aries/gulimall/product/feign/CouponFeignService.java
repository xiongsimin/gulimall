package kim.aries.gulimall.product.feign;

import kim.aries.common.to.SkuReductionTo;
import kim.aries.common.to.SpuBoundsTo;
import kim.aries.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author aries
 * @Data 2021-06-03
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    @RequestMapping("/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
