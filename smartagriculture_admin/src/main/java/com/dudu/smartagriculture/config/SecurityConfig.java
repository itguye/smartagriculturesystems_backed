package com.dudu.smartagriculture.config;



import com.dudu.smartagriculture.component.JwtAuthenticationTokenFilter;
import com.dudu.smartagriculture.component.RestAuthenticationEntryPoint;
import com.dudu.smartagriculture.component.RestfulAccessDeniedHandler;
import com.dudu.smartagriculture.dto.AdminUserDetails;
import com.dudu.smartagriculture.mbg.model.UmsAdmin;
import com.dudu.smartagriculture.mbg.model.UmsPermission;
import com.dudu.smartagriculture.service.UmsAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;


/**
 * SpringSecurity的配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true) // 开启注解权限配置
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UmsAdminService adminService;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;// 当访问接口没有权限时，自定义的返回结果
    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;// 当未登录或者token失效访问接口时，自定义的返回结果

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                       "/swagger-ui/",
                        "/swagger-resources/**"
                )
                .permitAll()
                .antMatchers("/admin/login", "/admin/register")// 对登录注册要允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
//                .antMatchers("/**")//测试时全部运行访问
//                .permitAll()
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter【添加过滤器】
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回【配置异常处理器】
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        httpSecurity.cors(); //允许跨域
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    // 密码格式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// 默认方式{id}password 。它会根据id去判断密码的加密方式,这里修改为BCryptPasswordEncoder
    }





    // UserDetailsService:加载用户特定数据的核心接口。里面定义了一个根据用户名查询用户信息的方法。,默认采用内存的账号和密码,这里修改为采用数据库中获取信息
    @Bean
    public UserDetailsService userDetailsService() {

        //获取登录用户信息
        return username -> {
            System.out.println("执行userDetailsService");
            UmsAdmin admin = adminService.getAdminByUsername(username);// 通过用户name获取用户信息
            if (admin != null) {
                List<UmsPermission> permissionList = adminService.getPermissionList(admin.getId());// 根据admin的id获取权限集合
                return new AdminUserDetails(admin,permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    // 在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。
    //#添加RestfulAccessDeniedHandler
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    // AuthenticationManager接口：定义了认证Authentication的方法【AuthenticationManager将其作为Bean公开】
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}