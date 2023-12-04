package com.donny.community.interceptor;

import com.donny.community.entity.LoginTicket;
import com.donny.community.entity.User;
import com.donny.community.service.UserService;
import com.donny.community.util.CookieUtil;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            LoginTicket loginTicket = userService.getLoginTicket(ticket);
            //检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus().equals(1) && loginTicket.getExpired().after(new Date())) {
                User user = userService.getUserById(loginTicket.getUserId());
                // 线程里存入user
                hostHolder.setUser(user);

                // 构建用户认证结果, 存入SecurityContext, 以便于授权
                /**
                 * springsecurity 持久化分为两个步骤：
                 * 在运行前，SecurityContextHolder 从 SecurityContextRepository 中读取 SercurityContext
                 * 运行结束后，SecurityContextHolder 将修改后的 SercurityContext 再存入 SecurityContextRepository 中，以便下次访问
                 * 而在 springsecurity6.1.0 中使用 SecurityContextHolder 更改 SercurityContext 时，没有上述的第二步，即虽然更改了但是没有保存，下次访问时无法识别更改的内容。
                 */
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
                securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (hostHolder.getUser() != null && modelAndView != null) {
            modelAndView.addObject("loginUser", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
//        SecurityContextHolder.clearContext();
//        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
    }
}
