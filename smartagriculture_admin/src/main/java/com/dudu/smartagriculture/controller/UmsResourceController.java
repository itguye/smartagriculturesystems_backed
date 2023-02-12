package com.dudu.smartagriculture.controller;

import com.dudu.smartagriculture.common.api.CommonPage;
import com.dudu.smartagriculture.common.api.CommonResult;
import com.dudu.smartagriculture.mbg.model.UmsResource;
import com.dudu.smartagriculture.service.UmsResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/resource")
@Api(tags = "UmsResourceController",description = "后台资源管理")
public class UmsResourceController {

    @Resource
    private UmsResourceService umsResourceService;

//    @Resource
//    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @ApiOperation("获取资源列表")
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    public CommonResult<List<UmsResource>> listAll() {
        List<UmsResource> list = umsResourceService.listAll();
        return CommonResult.success(list);
    }

    @ApiOperation("分页模糊查询后台资源")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UmsResource>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsResource> resourceList = umsResourceService.list(categoryId,nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }


//    @ApiOperation("添加后台资源")
//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    public CommonResult create(@RequestBody UmsResource umsResource) {
//        int count = umsResourceService.create(umsResource);
//        dynamicSecurityMetadataSource.clearDataSource();
//        if (count > 0) {
//            return CommonResult.success(count);
//        } else {
//            return CommonResult.failed();
//        }
//    }

//    @ApiOperation("修改后台资源")
//    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
//    public CommonResult update(@PathVariable Long id,
//                               @RequestBody UmsResource umsResource) {
//        int count = umsResourceService.update(id, umsResource);
//        dynamicSecurityMetadataSource.clearDataSource();
//        if (count > 0) {
//            return CommonResult.success(count);
//        } else {
//            return CommonResult.failed();
//        }
//    }

    @ApiOperation("获取资源详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<UmsResource> getItem(@PathVariable Long id) {
        UmsResource umsResource = umsResourceService.getItem(id);
        return CommonResult.success(umsResource);
    }

//    @ApiOperation("删除后台资源")
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//    public CommonResult delete(@PathVariable Long id) {
//        int count = umsResourceService.delete(id);
//        dynamicSecurityMetadataSource.clearDataSource();
//        if (count > 0) {
//            return CommonResult.success(count);
//        } else {
//            return CommonResult.failed();
//        }
//    }

}
