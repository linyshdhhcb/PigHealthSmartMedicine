package com.linyi.pig.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 添加拦截器配置
     *
     * @param registry 拦截器注册器，用于注册和配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置Sa-Token拦截器，用于权限验证和登录检查
        registry.addInterceptor(new SaInterceptor(handle -> {
                    System.out.println("--------- flag 2，请求进入了拦截器，访问的 path 是：" + SaHolder.getRequest().getRequestPath());
                    // 跳过OPTIONS请求
                    if ("OPTIONS".equals(SaHolder.getRequest().getMethod())) {
                        return;
                    }
                    StpUtil.checkLogin();  // 登录校验，只有会话登录后才能通过这句代码
                }))
                .addPathPatterns("/**")
                // knife4j 白名单
                .excludePathPatterns(
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/favicon.ico",
                        "/favicon.ico/**",
                        "/v3/**")
                // 登录白名单
                .excludePathPatterns(
                        "/user/register",
                        "/user/login",
                        "/user/emailLogin",
                        "/user/sendEmailCode",
                        "/user/logout",
                        "/user/checkLogin"
                );
    }


}
