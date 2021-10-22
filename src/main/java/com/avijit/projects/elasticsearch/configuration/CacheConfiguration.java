package com.avijit.projects.elasticsearch.configuration;

import com.avijit.projects.elasticsearch.helper.Constants;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnProperty(name="spring.cache.type", havingValue = "hazelcast")
public class CacheConfiguration {

    @Bean
    Config config() {
        Config config = new Config();

        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(300);
        config.getMapConfigs().put(Constants.CACHE_NAME, mapConfig);

        return config;
    }


}
