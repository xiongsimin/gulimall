package kim.aries.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import kim.aries.common.to.es.SkuEsModel;
import kim.aries.gulimall.search.config.GulimallElasticSearchConfig;
import kim.aries.gulimall.search.constant.EsConstant;
import kim.aries.gulimall.search.service.ElasticSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author aries
 * @Data 2021-07-04
 */
@Slf4j
@Service("elasticSaveService")
public class ElasticSaveServiceImpl implements ElasticSaveService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productPutOnShelf(List<SkuEsModel> skuEsModels) throws IOException {
        //将商品信息保存到ES中
        //1、建立索引
        //2、在es中保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODDUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        boolean hasFailures = bulk.hasFailures();
//        bulk.getItems();
        return hasFailures;
    }
}
