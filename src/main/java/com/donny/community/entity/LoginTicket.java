package com.donny.community.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class LoginTicket {

    private Integer id;

    private Integer userId;

    private String ticket;

    private Integer status;

    private Date expired;
}
