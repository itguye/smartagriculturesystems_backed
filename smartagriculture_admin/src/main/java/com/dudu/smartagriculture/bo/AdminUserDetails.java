//package com.dudu.smartagriculture.bo;
//
//import com.dudu.smartagriculture.mbg.model.UmsAdmin;
//import com.dudu.smartagriculture.mbg.model.UmsResource;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * SpringSecurity需要的用户详情(动态权限配置中使用,该项目中没有使用)
// */
//public class AdminUserDetails implements UserDetails {
//    //后台用户
//    private UmsAdmin umsAdmin;
//    //拥有资源列表
//    private List<UmsResource> resourceList;
//    public AdminUserDetails(UmsAdmin umsAdmin, List<UmsResource> resourceList) {
//        this.umsAdmin = umsAdmin;
//        this.resourceList = resourceList;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        //返回当前用户的角色
//        return resourceList.stream()
//                .map(role ->new SimpleGrantedAuthority(role.getId()+":"+role.getName()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getPassword() {
//        return umsAdmin.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return umsAdmin.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return umsAdmin.getStatus().equals(1);
//    }
//}
