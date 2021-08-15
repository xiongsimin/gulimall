package kim.aries.gulimall.product.feign;

import kim.aries.common.to.es.SkuEsModel;
import kim.aries.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-07-04
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {
    @RequestMapping("/search/save/product")
    R productPutOnShelf(@RequestBody List<SkuEsModel> skuEsModels);
}
