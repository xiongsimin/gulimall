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
     * 复杂检索示例
     *
     * @throws IOException
     */
    @Test
    public void searchData() throws IOException {
        //1、创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices("bank");
        //指定DSL，检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1.1、 构造查询条件
//        searchSourceBuilder.query();
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
//        searchSourceBuilder.aggregations();
        //1.1.1 构造查询条件1
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        //1.1.2 构造聚合条件1  按年龄的值分布进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(100);
        searchSourceBuilder.aggregation(ageAgg);
        //1.1.3 构造聚合条件2  计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(balanceAvg);

        System.out.println("检索条件：" + searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);


        //2、执行检索
        SearchResponse searchResponse = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //3、分析结果
        System.out.println(searchResponse);
        //3.1、获取所有查到的数据
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
            System.out.println("account：" + account);
        }
        //3.2、 获取聚合信息
        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageAggRs = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAggRs.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄" + keyAsString + "===>" + bucket.getDocCount());
        }

        Avg balanceAvgRs = aggregations.get("balanceAvg");
        System.out.println("平均薪资：" + balanceAvgRs.getValue());
    }

    /**
     * 测试存储/更新数据到ES
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("memberprice");
        indexRequest.id("1");
//        indexRequest.source("username", "zhangsan", "age", "18", "gender", "男");
        MemberPrice memberPrice = new MemberPrice();
        memberPrice.setId(1L);
        memberPrice.setName("会员价");
        memberPrice.setPrice(new BigDecimal(580.0));
        String jsonString = JSON.toJSONString(memberPrice);
        //要保存的内容
        indexRequest.source(jsonString, XContentType.JSON);
        //执行操作
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //todo 提取有用的响应数据
        System.out.println(index);
    }


    @Test
    public void contextLoads() {
        System.out.println(client);
    }

}
