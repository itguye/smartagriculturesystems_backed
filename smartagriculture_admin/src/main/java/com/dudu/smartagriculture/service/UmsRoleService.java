package com.dudu.smartagriculture.service;

import com.dudu.smartagriculture.mbg.model.UmsMenu;
import com.dudu.smartagriculture.mbg.model.UmsResource;
import com.dudu.smartagriculture.mbg.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UmsRoleService {
    // 根据管理员ID获取对应菜单
    List<UmsMenu> getMenuList(Long adminId);

    // 获取角色列表
    List<UmsRole> getListAll();
    // 分页获取角色列表
    List<UmsRole> getList(String keyword, Integer pageNum, Integer pageSize);

    // 修改角色状态
    int update(Long id, UmsRole umsRole);
    // 根据id删除角色信息
    int delete(List<Long> ids);

    // 添加角色
    int create(UmsRole umsRole);

    // 根据角色Id获取菜单
    List<UmsMenu> getMenuListByRoleId(Long roleId);

    // 添加运行访问的菜单
    int alloMenu(Long roleId, List<Long> menuIds);

    // 根据角色id获取资源
    @Transactional
    List<UmsResource> getResourceList(Long roleId);

    // 给角色分配资源
    @Transactional
    int alloResource(Long roleId, List<Long> resourceIds);
}
