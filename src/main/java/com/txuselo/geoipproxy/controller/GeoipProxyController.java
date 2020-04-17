package com.txuselo.geoipproxy.controller;

import java.io.IOException;

import com.txuselo.geoipproxy.model.Geoip;
import com.txuselo.geoipproxy.service.GeoipService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GeoipProxyController {

    Logger logger = LoggerFactory.getLogger(GeoipProxyController.class);

    GeoipService geoipService;

    @Autowired
    public GeoipProxyController(GeoipService geoipService) {
        this.geoipService = geoipService;
    }

    @GetMapping(value = "/{ip}")
    public ResponseEntity<Geoip> getGeoFromIpb(@PathVariable String ip)
            throws UnsupportedOperationException, IOException {
        return new ResponseEntity<Geoip> (geoipService.getGeoFromIp(ip), HttpStatus.OK);
    }
    
}