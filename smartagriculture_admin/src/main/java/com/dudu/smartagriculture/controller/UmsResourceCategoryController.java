package com.dudu.smartagriculture.controller;

import com.dudu.smartagriculture.common.api.CommonResult;
import com.dudu.smartagriculture.mbg.model.UmsResourceCategory;
import com.dudu.smartagriculture.service.UmsResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "UmsResourceCategoryController",description = "后台资源管理")
@RequestMapping("/resourceCategory")
public class UmsResourceCategoryController {
    @Resource
    private UmsResourceCategoryService umsResourceCategoryService;


    @ApiOperation("查询后台所有资源分类")
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    public CommonResult<List<UmsResourceCategory>> listAll() {
        List<UmsResourceCategory> list = umsResourceCategoryService.getListAll();
        return CommonResult.success(list);
    }

    @ApiOperation("添加后台资源分类")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody UmsResourceCategory umsResourceCategory) {
        int count = umsResourceCategoryService.create(umsResourceCategory);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }


    @ApiOperation("修改后台资源分类")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@PathVariable Long id,
                               @RequestBody UmsResourceCategory umsResourceCategory) {
        int count = umsResourceCategoryService.update(id, umsResourceCategory);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        int count = umsResourceCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
