package com.dudu.smartagriculture.service.impl;

import com.dudu.smartagriculture.dao.UmsRoleDao;
import com.dudu.smartagriculture.mbg.mapper.UmsRoleMapper;
import com.dudu.smartagriculture.mbg.mapper.UmsRoleMenuRelationMapper;
import com.dudu.smartagriculture.mbg.mapper.UmsRoleResourceRelationMapper;
import com.dudu.smartagriculture.mbg.model.*;
import com.dudu.smartagriculture.service.UmsAdminCacheService;
import com.dudu.smartagriculture.service.UmsRoleService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UmsRoleServiceImpl implements UmsRoleService {
    @Resource
    private UmsRoleMapper umsRoleMapper;
    @Resource
    private UmsRoleDao umsRoleDao;
    @Resource
    private UmsAdminCacheService adminCacheService;
    @Resource
    private UmsRoleMenuRelationMapper umsRoleMenuRelationMapper;
    @Resource
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;


    @Override
    public List<UmsMenu> getMenuList(Long id) {
        return umsRoleDao.getMenuList(id);
    }


    // 获取角色列表
    @Override
    public List<UmsRole> getListAll() {
        return umsRoleMapper.selectByExample(new UmsRoleExample());
    }

    // 分页获取角色信息
    @Override
    public List<UmsRole> getList(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        UmsRoleExample example = new UmsRoleExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        return umsRoleMapper.selectByExample(example);
    }

    // 修改角色
    @Override
    public int update(Long id, UmsRole umsRole) {
        umsRole.setId(id);
        return   umsRoleMapper.updateByPrimaryKeySelective(umsRole);
    }

    // 删除角色
    @Override
    public int delete(List<Long> ids) {
        UmsRoleExample example = new UmsRoleExample();
        example.createCriteria().andIdIn(ids);
        int count =  umsRoleMapper.deleteByExample(example);// 删除角色
        adminCacheService.delResourceListByRoleIds(ids);// 删除缓存中的数据
        return count;
    }

    // 创建角色
    @Override
    public int create(UmsRole umsRole) {
        umsRole.setCreateTime(new Date());
        umsRole.setAdminCount(0);
        umsRole.setSort(0);
        return umsRoleMapper.insert(umsRole);
    }

    // 根据角色获取菜单
    @Override
    public List<UmsMenu> getMenuListByRoleId(Long roleId) {
        return umsRoleDao.getMenuListByRoleId(roleId);
    }

    // 添加运行访问的菜单
    @Override
    public int alloMenu(Long roleId, List<Long> menuIds) {
        // 先删除以前的联系
        UmsRoleMenuRelationExample umsRoleMenuRelationExample = new UmsRoleMenuRelationExample();
        umsRoleMenuRelationExample.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleMenuRelationMapper.deleteByExample(umsRoleMenuRelationExample);

        // 新增加菜单访问
        for (Long id : menuIds) {
            UmsRoleMenuRelation relation = new UmsRoleMenuRelation();
            relation.setMenuId(id);
            relation.setRoleId(roleId);
            umsRoleMenuRelationMapper.insert(relation);
        }

        return menuIds.size();
    }

    @Override
    public List<UmsResource> getResourceList(Long roleId) {
        return umsRoleDao.getResourceListByRoleId(roleId);
    }

    @Override
    public int alloResource(Long roleId, List<Long> resourceIds) {
        // 先删除原有资源
        UmsRoleResourceRelationExample umsRoleResourceRelationExample = new UmsRoleResourceRelationExample();
        umsRoleResourceRelationExample.createCriteria().andRoleIdEqualTo(roleId).andResourceIdIn(resourceIds);
        umsRoleResourceRelationMapper.deleteByExample(umsRoleResourceRelationExample);

        // 新增资源
        for (Long id : resourceIds) {
            UmsRoleResourceRelation relation = new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(id);

            umsRoleResourceRelationMapper.insert(relation);
        }

        return resourceIds.size();
    }


}
