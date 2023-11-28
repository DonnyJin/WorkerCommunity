package com.donny.community.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class User {

    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private Integer type;
    private Integer status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
