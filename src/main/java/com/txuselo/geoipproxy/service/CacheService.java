package com.txuselo.geoipproxy.service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.txuselo.geoipproxy.config.GeoipConfig;
import com.txuselo.geoipproxy.model.Geoip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    Logger logger = LoggerFactory.getLogger(CacheService.class);

    Map<String,Geoip> cache;
    Map<String,Instant> evictControl;
    int expiration;

    @Autowired
    public CacheService(GeoipConfig config){
        logger.info("Setting evict time for each cache registry: {}s", config.getCache().getEvictTime());
        logger.info("Setting cron for launch evict task: {}", config.getCache().getEvictCron());
        this.expiration = config.getCache().getEvictTime();
        cache = new HashMap<String,Geoip>();
        evictControl = new HashMap<String,Instant>();
    }

    @Nullable
    public Geoip searchIp(String ip){
        return cache.get(ip);
    }

    public void insertGeoip(String ip, Geoip geoip){
        logger.debug("Caching resgistry... [{},{}]", ip, geoip);
        cache.put(ip, geoip);
        evictControl.put(ip, Instant.now());
    }

    @Scheduled(cron = "${geoip-service.cache.evict-cron}")
    public void evict(){
        Instant now = Instant.now();
        int sizePreEvict = cache.size();
        logger.info("Starting evict task. Cache size: {} ", cache.size());
        Iterator<Entry<String,Instant>> iterator = evictControl.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,Instant> evictEntry = (Map.Entry<String,Instant>)iterator.next();  
            if (now.getEpochSecond() - evictEntry.getValue().getEpochSecond() >= expiration){
                logger.debug("Evicting registry {} with timestamp {}", evictEntry.getKey(), DateTimeFormatter.ISO_INSTANT.format(evictEntry.getValue()));
                iterator.remove();
                cache.remove(evictEntry.getKey());
            }
        }
        logger.info("Ending evict task. {} registries evicted. Cache size: {} ", sizePreEvict - cache.size(), cache.size());
    }

}