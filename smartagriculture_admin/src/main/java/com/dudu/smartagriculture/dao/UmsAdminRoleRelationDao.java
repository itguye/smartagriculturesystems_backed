package com.dudu.smartagriculture.dao;

import com.dudu.smartagriculture.dto.UmsAdminUserInfoParam;
import com.dudu.smartagriculture.mbg.model.UmsAdminRoleRelation;
import com.dudu.smartagriculture.mbg.model.UmsPermission;
import com.dudu.smartagriculture.mbg.model.UmsResource;
import com.dudu.smartagriculture.mbg.model.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsAdminRoleRelationDao {
    /**
     * 批量插入用户角色关系
     */
    int insertList(@Param("list") List<UmsAdminRoleRelation> adminRoleRelationList);

    /**
     * 获取用于所有角色
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有可访问资源
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);

    UmsAdminUserInfoParam getUserInfoAndRoleByName(@Param("name") String name);

    /**
     * 获取用户所有权限(包括+-权限)
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);
}
