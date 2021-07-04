package kim.aries.gulimall.search.service;

import kim.aries.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Author aries
 * @Data 2021-07-04
 */
public interface ElasticSaveService {

    /**
     * 商品上架信息保存到ES中
     *
     * @param skuEsModels
     */
    boolean productPutOnShelf(List<SkuEsModel> skuEsModels) throws IOException;
}
