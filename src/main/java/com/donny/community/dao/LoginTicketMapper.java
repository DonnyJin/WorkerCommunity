package com.donny.community.dao;

import com.donny.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Deprecated
public interface LoginTicketMapper {

    Integer insertLoginTicket(LoginTicket loginTicket);

    LoginTicket selectByTicket(String ticket);

    Integer updateStatus(String ticket, Integer status);

}
