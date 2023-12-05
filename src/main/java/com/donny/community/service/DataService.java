package com.donny.community.service;

import java.util.Date;

public interface DataService {

    /**
     * 将指定的Ip计入UV
     */
    void recordUV(String ip);

    Long calculateUV(Date start, Date end);

    /**
     * 将指定用户计入DAU
     */
    void recordDAU(Integer userId);

    Long calculateDAU(Date start, Date end);
}