package com.txuselo.geoipproxy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Geoip {

    private String ip;
    private String continent_code;
    private String continent_name;
    private String country_code2;
    private String country_code3;
    private String country_name;
    private String country_capital;
    private String state_prov;
    private String district;
    private String city;
    private String zipcode;
    private Double latitude;
    private Double longitude;
    private Double [] location;
    private Boolean is_eu;
    private String calling_code;
    private String country_tld;
    private String languages;
    private String country_flag;
    private String geoname_id;
    private String isp;
    private String connection_type;
    private String organization;
    private Currency currency;
    private TimeZone time_zone;
    private Boolean is_local;

    public Geoip(String ip) {
        this.ip = ip;
    }



}