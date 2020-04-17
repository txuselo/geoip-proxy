package com.txuselo.geoipproxy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeZone {

    private String name;
    private int offset;
    private String current_time;
    private double current_time_unix;
    private boolean is_dst;
    private long dst_savings;
}