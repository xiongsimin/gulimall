package kim.aries.gulimall.search;

import com.alibaba.fastjson.JSON;
import kim.aries.common.to.MemberPrice;
import kim.aries.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Data
    @ToString
    static class Account {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    /**
     * ??????????????????
     *
     * @throws IOException
     */
    @Test
    public void searchData() throws IOException {
        //1?????????????????????
        SearchRequest searchRequest = new SearchRequest();
        //????????????
        searchRequest.indices("bank");
        //??????DSL???????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1.1??? ??????????????????
//        searchSourceBuilder.query();
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
//        searchSourceBuilder.aggregations();
        //1.1.1 ??????????????????1
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        //1.1.2 ??????????????????1  ?????????????????????????????????
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(100);
        searchSourceBuilder.aggregation(ageAgg);
        //1.1.3 ??????????????????2  ??????????????????
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(balanceAvg);

        System.out.println("???????????????" + searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);


        //2???????????????
        SearchResponse searchResponse = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //3???????????????
        System.out.println(searchResponse);
        //3.1??????????????????????????????
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            /**
             * "_index" : "bank",
             *         "_type" : "account",
             *         "_id" : "970",
             *         "_score" : 5.4032025,
             *         "_source" : {
             */
//            hit.getIndex();
//            hit.getType();
//            hit.getId();
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println("account???" + account);
        }
        //3.2??? ??????????????????
        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageAggRs = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAggRs.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("??????" + keyAsString + "===>" + bucket.getDocCount());
        }

        Avg balanceAvgRs = aggregations.get("balanceAvg");
        System.out.println("???????????????" + balanceAvgRs.getValue());
    }

    /**
     * ????????????/???????????????ES
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("memberprice");
        indexRequest.id("1");
//        indexRequest.source("username", "zhangsan", "age", "18", "gender", "???");
        MemberPrice memberPrice = new MemberPrice();
        memberPrice.setId(1L);
        memberPrice.setName("?????????");
        memberPrice.setPrice(new BigDecimal(580.0));
        String jsonString = JSON.toJSONString(memberPrice);
        //??????????????????
        indexRequest.source(jsonString, XContentType.JSON);
        //????????????
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //todo ???????????????????????????
        System.out.println(index);
    }


    @Test
    public void contextLoads() {
        System.out.println(client);
    }

}
