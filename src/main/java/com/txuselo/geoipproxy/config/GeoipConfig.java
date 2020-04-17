package com.txuselo.geoipproxy.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix="geoip-service")
public class GeoipConfig {

    private String apiKey;
    private String url;

    @NotNull
    private Cache cache;

    @Data
    public static class Cache{
        private int evictTime = 60;
        private String evictCron = "0 0 * * * ?";
    }
}