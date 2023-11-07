package com.donny.community.controller;

import com.donny.community.entity.User;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.soap.SOAPBinding;
import java.util.Map;

@Slf4j
@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "/site/login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        // map为空时代表注册成功
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功,请到邮箱中点击激活邮件进行激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "site/register";
        }
    }

    // http://localhost:8082/community/activation/id/code
    @GetMapping("/activation/{id}/{code}")
    public String activate(Model model, @PathVariable Integer id, @PathVariable String code) {
        Integer result = userService.activate(id, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功!");
            model.addAttribute("target","/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活！");
            model.addAttribute("target","/index");
        } else {
            model.addAttribute("msg", "激活失败!发生了不可思议的错误！");
            model.addAttribute("target","/index");
        }
        return "site/operate-result";
    }
}
