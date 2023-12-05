package com.donny.community.config;

import com.donny.community.util.CommunityConstant;
import com.donny.community.util.CommunityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {
    /**
     * springsecurity 持久化分为两个步骤:
     * 在运行前，SecurityContextHolder 从 SecurityContextRepository 中读取 SercurityContext
     * 运行结束后，SecurityContextHolder 将修改后的 SercurityContext 再存入 SecurityContextRepository 中，以便下次访问
     * 而在 springsecurity6.1.0 中使用 SecurityContextHolder 更改 SercurityContext 时，没有上述的第二步，即虽然更改了但是没有保存，下次访问时无法识别更改的内容。
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权
        http.authorizeRequests()
                .antMatchers("/user/setting", "/user/upload", "user/profile/**",
                        "/discuss/add", "/comment/add/**",
                        "/message/**",  "message/notice/**",
                        "/like", "/follow", "/unfollow")
                .hasAnyAuthority(AUTHORITY_USER, AUTHORITY_ADMIN, AUTHORITY_MODERATOR)
                .antMatchers("/discuss/top", "/discuss/wonderful")
                .hasAnyAuthority(AUTHORITY_MODERATOR)
                .antMatchers("/discuss/delete", "/data/**")
                .hasAnyAuthority(AUTHORITY_ADMIN)
                .anyRequest().permitAll().and().csrf().disable();

        // 权限不够时
        http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            // 没有登录的处理
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                String xRequestedWith = request.getHeader("x-requested-with");
                if ("XMLHttpRequest".equals(xRequestedWith)) {
                    response.setContentType("application/plain;charset=utf-8");
                    response.getWriter().write(CommunityUtil.getJSONString(403, "用户还未登录"));
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            }
        }).accessDeniedHandler(new AccessDeniedHandler() {
            // 登录了但是权限不足
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                String xRequestedWith = request.getHeader("x-requested-with");
                if ("XMLHttpRequest".equals(xRequestedWith)) {
                    response.setContentType("application/plain;charset=utf-8");
                    response.getWriter().write(CommunityUtil.getJSONString(403, "用户没有权限"));
                } else {
                    response.sendRedirect(request.getContextPath() + "/denied");
                }
            }
        });

        // Security默认拦截/logout, 覆盖默认逻辑才能执行自己的推出代码, 给它一个不存在的路径覆盖
        http.logout().logoutUrl("/securitylogout");
    }
}
