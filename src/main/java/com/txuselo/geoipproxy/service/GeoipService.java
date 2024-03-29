package com.txuselo.geoipproxy.service;


import com.txuselo.geoipproxy.config.GeoipConfig;
import com.txuselo.geoipproxy.model.Geoip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoipService {

    Logger logger = LoggerFactory.getLogger(GeoipService.class);

    CacheService cacheService;

    IpUtilsService ipUtils;

    RestTemplate restTemplate;
    HttpHeaders headers;
    HttpEntity<Geoip> request;

    String url;
    String apiKey;

    public GeoipService(CacheService cacheService, IpUtilsService ipUtilsService, GeoipConfig config){
        this.cacheService = cacheService;
        this.ipUtils = ipUtilsService;
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.request = new HttpEntity<Geoip>(headers);
        
        logger.info("Setting geoip url: {} ", config.getUrl());
        this.url = config.getUrl();
        logger.info("Setting api key for geoip service: {} ", config.getApiKey());
        this.apiKey = config.getApiKey();
    }

	public Geoip getGeoFromIp(String ip) {
        if (ip.equals("favicon.ico")){
            return null;
        }
        Geoip cachedResult = cacheService.searchIp(ip);
        if (cachedResult != null){
            return cachedResult;
        } if (!ipUtils.isValidPublicIp(ip)){
            Geoip geoip = new Geoip(ip);
            geoip.setIs_local(true);
            return geoip;
        } else{
            String finalUrl = String.format(url, this.apiKey, ip);
            logger.info("Request for... {}", finalUrl);
            ResponseEntity<Geoip> response = restTemplate.exchange(finalUrl, HttpMethod.GET, request, Geoip.class);
            if (response.getStatusCode().is2xxSuccessful()){
                Geoip geoip = response.getBody();
                Double[] location = { geoip.getLongitude(), geoip.getLatitude() };
                geoip.setLocation(location);
                geoip.setIs_local(false);
                logger.debug("Caching result for ip: {} geoip: {}", ip, response.getBody());
                cacheService.insertGeoip(ip, response.getBody());
                return response.getBody();
            }else{
                return new Geoip(ip);
            }
        }
	}


}