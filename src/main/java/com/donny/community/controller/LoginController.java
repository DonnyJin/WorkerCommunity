package com.donny.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }
}
