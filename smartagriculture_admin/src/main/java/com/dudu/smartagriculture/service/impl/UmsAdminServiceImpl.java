package com.dudu.smartagriculture.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dudu.smartagriculture.common.exception.Asserts;
import com.dudu.smartagriculture.common.utils.RequestUtil;
import com.dudu.smartagriculture.dao.UmsAdminRoleRelationDao;
import com.dudu.smartagriculture.dto.UmsAdminParam;
import com.dudu.smartagriculture.dto.UmsAdminUserInfoParam;
import com.dudu.smartagriculture.dto.UpdateAdminPasswordParam;
import com.dudu.smartagriculture.mbg.mapper.UmsAdminLoginLogMapper;
import com.dudu.smartagriculture.mbg.mapper.UmsAdminMapper;
import com.dudu.smartagriculture.mbg.mapper.UmsAdminRoleRelationMapper;
import com.dudu.smartagriculture.mbg.model.*;
import com.dudu.smartagriculture.service.UmsAdminCacheService;
import com.dudu.smartagriculture.service.UmsAdminService;
import com.dudu.smartagriculture.utils.JwtTokenUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Resource
    private JwtTokenUtil tokenUtil;
    @Resource
    private UmsAdminMapper umsAdminMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UmsAdminRoleRelationDao adminRoleRelationDao;
    @Resource
    private UmsAdminLoginLogMapper umsAdminLoginLogMapper;
    @Resource
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;
    @Resource
    private UserDetailsService userDetailsService;


    @Transactional(rollbackFor=Exception.class)
    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        // 初始化UmsAdmin对象
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);

        // 验证UmsAdmin对象
        //查询是否有相同用户名的用户
        UmsAdminExample example = new UmsAdminExample();// 条件
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdmins = umsAdminMapper.selectByExample(example);
        if (umsAdmins.size() > 0) {
            return null;// 表示当前数据库中该名称存在
        }

        // 默认头像
        umsAdmin.setIcon("http://qnysas.itguye.top/avatar/head.png");

        // 加密码加密,然后将用户信息存入数据库中
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        umsAdminMapper.insert(umsAdmin);
        // 分配普通角色
        UmsAdminRoleRelation umsAdminRoleRelation = new UmsAdminRoleRelation();
        umsAdminRoleRelation.setAdminId(umsAdmin.getId());
        umsAdminRoleRelation.setRoleId(2L);
        umsAdminRoleRelationMapper.insert(umsAdminRoleRelation);//umsAdmin.getId(), 2
        return umsAdmin;
    }
    @Override
    public String login(String username, String password) {
        // Token对象
        String token = null;
        try {
//            UserDetails userDetails = loadUserByUsername(username); // 自定义加载方式
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password,userDetails.getPassword())) {
                Asserts.fail("密码不正确");
            }
            // 验证用户是否被禁用
            if (!userDetails.isEnabled()) {
                Asserts.fail("帐号已被禁用");
            }
            // 将用户信息存入UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);// 存储用户信息
            // 生成Toke
            token = tokenUtil.generateToken(userDetails);
            // 插入登入日志
            insertLoginLog(username);
            // 更新登入时间
            UmsAdmin umsAdmin = new UmsAdmin();
            umsAdmin.setLoginTime(new Date());
            UmsAdminExample umsAdminExample = new UmsAdminExample();
            umsAdminExample.createCriteria().andUsernameEqualTo(username);
            int i = umsAdminMapper.updateByExampleSelective(umsAdmin, umsAdminExample);
            if (i <= 0) {
                Asserts.fail("插入登入记录失败");
            }
            return token;
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }

        return null;
    }
    @Override
    public UmsAdmin getItem(Long id) {
        return umsAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public UmsAdminUserInfoParam getUserInfoByName(String name) {
        UmsAdminUserInfoParam userInfoAndRoleByName = adminRoleRelationDao.getUserInfoAndRoleByName(name);
        return userInfoAndRoleByName;
    }

    @Override
    public UmsAdmin getAdminByUserName(String username) {
        // 从缓存中获取
        UmsAdmin umsAdmin = getCacheService().getAdmin(username);
        if (umsAdmin != null) {
            return umsAdmin;
        }

        // 从数据库中获取
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> umsAdmins = umsAdminMapper.selectByExample(example);
        if (umsAdmins != null && umsAdmins.size() > 0) {
            umsAdmin = umsAdmins.get(0);
            getCacheService().setAdmin(umsAdmin);// 将数据存放缓存中去
            return umsAdmin;
        }
        return null;
    }
    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }


    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        if (!StrUtil.isEmpty(keyword)) {
            keyword = keyword.replaceAll("_", "\\\\_");
            keyword = keyword.replaceAll("%", "\\\\%");
            criteria.andUsernameLike("%" + keyword + "%");
            example.or(example.createCriteria().andNickNameLike("%" + keyword + "%"));
        }
        return umsAdminMapper.selectByExample(example);
    }
    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = umsAdminMapper.selectByPrimaryKey(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        int count = umsAdminMapper.updateByPrimaryKeySelective(admin);
        getCacheService().delAdmin(id);
        return count;
    }
    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        // 非空判断
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        // 查询需要修改密码的用户
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
        if(CollUtil.isEmpty(adminList)){
            return -2;
        }
        UmsAdmin umsAdmin = adminList.get(0);
        // 输入的旧密码和新密码是否一致
        if(!passwordEncoder.matches(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        // 加密
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        umsAdminMapper.updateByPrimaryKey(umsAdmin);
        getCacheService().delAdmin(umsAdmin.getId());
        return 1;
    }
    @Override
    public int delete(Long id) {
        getCacheService().delAdmin(id);
        int count = umsAdminMapper.deleteByPrimaryKey(id);
        getCacheService().delResourceList(id);
        return count;
    }
    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        UmsAdminRoleRelationExample adminRoleRelationExample = new UmsAdminRoleRelationExample();
        adminRoleRelationExample.createCriteria().andAdminIdEqualTo(adminId);
        umsAdminRoleRelationMapper.deleteByExample(adminRoleRelationExample);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationDao.insertList(list);
        }
        getCacheService().delResourceList(adminId);// 删除缓存中的指定用户id的资源信息
        return count;
    }
    @Override
    public String refreshToken(String oldToken) {
        return tokenUtil.refreshHeadToken(oldToken);
    }



//    // 根据用户名加载用户信息
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        // 【1】 动态获取的方式
//        // 获取用户信息
//        UmsAdmin umsAdmin = getAdminByUserName(username);
//        if (umsAdmin != null) {
//            // 获取用户资源
//            List<UmsResource> umsRoleServices = getResourceList(umsAdmin.getId());
//            return new AdminUserDetails(umsAdmin, umsRoleServices);
//        }
//        throw new UsernameNotFoundException("用户名或密码错误");
//
//    }




    // 根据用户Id获取用户所需资源【权限】
    @Override
    public List<UmsResource> getResourceList(Long id) {
        List<UmsResource> umsResourceList = getCacheService().getResourceList(id);// 从缓存中获取
        if (CollUtil.isNotEmpty(umsResourceList)) {// 缓存中有数据返回
            return umsResourceList;
        }

        // 从数据库中获取
        umsResourceList = adminRoleRelationDao.getResourceList(id);
        if (CollUtil.isNotEmpty(umsResourceList)) {
            // 将数据存放到缓存中去
            getCacheService().setResourceList(id,umsResourceList);
        }
        return umsResourceList;
    }

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        // 封装条件对象
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {
        return adminRoleRelationDao.getPermissionList(adminId);
    }

    // 缓存数据
    @Override
    public UmsAdminCacheService getCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }
    // 登入日志【单表操作】
    private void insertLoginLog(String username) {
        // 查询用户是否存在用户表中
        UmsAdmin umsAdmin = getAdminByUserName(username);
        if (umsAdmin == null) {
            return;
        }

        // 用户存在,添加相关日志数据
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(umsAdmin.getId());
        loginLog.setCreateTime(new Date());
        // 获取IP相关信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(RequestUtil.getRequestIp(request));

        // 将日志存放到数据库中
        umsAdminLoginLogMapper.insert(loginLog);
    }
}
