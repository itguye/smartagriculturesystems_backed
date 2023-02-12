package com.dudu.smartagriculture.component;


import com.dudu.smartagriculture.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器,(默认的RequestFilter接口存在一些问题,所以讲义使用OncePerRequestFilter类)
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private
    JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("执行自定义过滤器");
        // 获取请求头部信息
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {// 验证请求头信息
            String authToken = authHeader.substring(this.tokenHead.length());// 获取请求头中Bearer后面的token信息
            String username = jwtTokenUtil.getUserNameFromToken(authToken);// 获取token里的用户信息【载荷信息】
            LOGGER.info("checking username:{}", username);
            // 判断Token中是否存在用户登入信息(载荷中)
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 存在表示用户已经登入了,通过UserDetailsService获取UserDetails
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) { // 验证token
                    // 封装Authentication(UsernamePasswordAuthenticationToken是Authentication的子类【孙子类】)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);// 将Authentication数据存入到SecurityContextHolder中
                }
            }
        }
        chain.doFilter(request, response);// 放行
    }
}
