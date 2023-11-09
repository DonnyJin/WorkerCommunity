package com.donny.community.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginTicket {

    private Integer id;

    private Integer userId;

    private String ticket;

    private Integer status;

    private LocalDateTime expired;
}
