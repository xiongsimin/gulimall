package kim.aries.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author aries
 * @Data 2021-06-17
 */
@Configuration
public class GulimallElasticSearchConfig {

    @Bean
    public RestHighLevelClient esRestClient() {
        RestClientBuilder builder = null;
        builder = RestClient.builder(new HttpHost("47.93.185.207", 9200, "http"));

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
