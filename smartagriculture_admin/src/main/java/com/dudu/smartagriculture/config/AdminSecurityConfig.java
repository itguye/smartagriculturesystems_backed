//package com.dudu.smartagriculture.config;
//
//import com.dudu.smartagriculture.component.DynamicAccessDecisionManager;
//import com.dudu.smartagriculture.component.DynamicSecurityService;
//import com.dudu.smartagriculture.mbg.model.UmsResource;
//import com.dudu.smartagriculture.service.UmsAdminService;
//import com.dudu.smartagriculture.service.UmsResourceService;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * security模块相关配置
// * 2022-11-29
// *
// */
////@Configuration
//public class AdminSecurityConfig {
//    private static final Log logger = LogFactory.getLog(AdminSecurityConfig.class);
//    @Resource
//    private UmsAdminService adminService;
//    @Resource
//    private UmsResourceService resourceService;
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        //获取登录用户信息
//        return username -> adminService.loadUserByUsername(username);
//    }
//
//
//    /**
//     * 加載資源
//     * @return
//     */
//    @Bean
//    public DynamicSecurityService dynamicSecurityService() {
//        return new DynamicSecurityService() {
//            @Override
//            public Map<String, ConfigAttribute> loadDataSource() {
//                Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
//                List<UmsResource> resourceList = resourceService.listAll();// 获取资源列表
//                for (UmsResource resource : resourceList) {
//                    logger.info("资源的key:"+resource.getUrl());
//                    logger.info("资源的value:"+new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
//                    map.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
//                }
//                return map;
//            }
//        };
//    }
//}
