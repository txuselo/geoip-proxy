package com.txuselo.geoipproxy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Currency {
    private String code;
    private String name;
    private String symbol;
}