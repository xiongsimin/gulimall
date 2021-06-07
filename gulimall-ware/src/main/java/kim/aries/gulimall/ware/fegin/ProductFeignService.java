package kim.aries.gulimall.ware.fegin;

import kim.aries.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author aries
 * @Data 2021-06-07
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    //注：实际这也可以给网关发，但是需要配置成@FeignClient("gulimall-gateway")
    @RequestMapping("/product/skuinfo/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId);
}
