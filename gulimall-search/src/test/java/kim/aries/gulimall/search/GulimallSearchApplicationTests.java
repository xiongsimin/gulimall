package kim.aries.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kim.aries.common.to.MemberPrice;
import kim.aries.gulimall.search.config.GulimallElasticSearchConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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

    /**
     * 测试存储数据到ES
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
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
