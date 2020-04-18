package com.txuselo.geoipproxy.service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Service;

@Service
public class IpUtilsService {

    public boolean isValidPublicIp(String ip) {
        Inet4Address address;
        try {
            address = (Inet4Address) InetAddress.getByName(ip);
        } catch (UnknownHostException exception) {
            return false; // assuming no logging, exception handling required
        }
        return !(address.isSiteLocalAddress() || 
                 address.isAnyLocalAddress()  || 
                 address.isLinkLocalAddress() || 
                 address.isLoopbackAddress() || 
                 address.isMulticastAddress());
    }
}