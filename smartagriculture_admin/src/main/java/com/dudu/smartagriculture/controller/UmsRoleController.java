package com.dudu.smartagriculture.controller;

import com.dudu.smartagriculture.common.api.CommonPage;
import com.dudu.smartagriculture.common.api.CommonResult;
import com.dudu.smartagriculture.mbg.model.UmsMenu;
import com.dudu.smartagriculture.mbg.model.UmsResource;
import com.dudu.smartagriculture.mbg.model.UmsRole;
import com.dudu.smartagriculture.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限管理
 */
@RestController
@RequestMapping("/role")
@Api(tags = "UmsRoleController", description = "后台菜单管理")
public class UmsRoleController {
    @Resource
    private UmsRoleService umsRoleService;

    @ApiOperation("获取所有角色")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public CommonResult<List<UmsRole>> listAll() {
        List<UmsRole> list = umsRoleService.getListAll();
        return CommonResult.success(list);
    }

    @ApiOperation("根据角色名称分页获取角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsRole>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<UmsRole> roleList = umsRoleService.getList(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(roleList));
    }

    @ApiOperation("修改角色状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsRole umsRole = new UmsRole();
        umsRole.setStatus(status);
        int count = umsRoleService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改角色信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@RequestBody UmsRole umsRole, @PathVariable Long id) {
        int count = umsRoleService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据Id删除角色信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult delete(@RequestParam(value = "ids") List<Long> ids) {
        int count = umsRoleService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("新增角色")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody UmsRole umsRole) {
        int count = umsRoleService.create(umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("根据角色Id获取菜单信息")
    @RequestMapping(value = "/listMenu/{roleId}", method = RequestMethod.GET)
    public CommonResult listMenu(@PathVariable Long roleId) {
        List<UmsMenu> list = umsRoleService.getMenuListByRoleId(roleId);
        return CommonResult.success(list);
    }

    @ApiOperation("添加角色运行访问的菜单")
    @RequestMapping(value = "/allocMenu", method = RequestMethod.POST)
    public CommonResult allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        int count = umsRoleService.alloMenu(roleId, menuIds);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }


    @ApiOperation("给角色分配菜单")
    @RequestMapping(value = "/listResource/{roleId}", method = RequestMethod.GET)
    public CommonResult<List<UmsResource>> listResource(@PathVariable Long roleId) {
        List<UmsResource> list = umsRoleService.getResourceList(roleId);
        return CommonResult.success(list);
    }

    @ApiOperation("给角色分配资源")
    @RequestMapping(value = "/allocResource", method = RequestMethod.POST)
    public CommonResult allocResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        int count = umsRoleService.alloResource(roleId,resourceIds);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }
}
