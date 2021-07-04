package kim.aries.gulimall.search.controller;

import kim.aries.common.enums.BizCodeEnum;
import kim.aries.common.to.es.SkuEsModel;
import kim.aries.common.utils.R;
import kim.aries.gulimall.search.service.ElasticSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-07-04
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {
    @Autowired
    ElasticSaveService elasticSaveService;

    @RequestMapping("/product")
    public R productPutOnShelf(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean result = false;
        try {
            result = elasticSaveService.productPutOnShelf(skuEsModels);
        } catch (Exception e) {
            log.error("ElasticSaveController商品上架失败：{}", e);
            return R.error(BizCodeEnum.PROUCT_UPEXCEPTION.getCode(), BizCodeEnum.PROUCT_UPEXCEPTION.getMsg());
        }
        if (!result) {
            return R.ok();
        }
        return R.error(BizCodeEnum.PROUCT_UPEXCEPTION.getCode(), BizCodeEnum.PROUCT_UPEXCEPTION.getMsg());

    }
}
