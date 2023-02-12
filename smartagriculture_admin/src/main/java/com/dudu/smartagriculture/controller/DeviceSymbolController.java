package com.dudu.smartagriculture.controller;

import com.dudu.smartagriculture.common.api.CommonResult;
import com.dudu.smartagriculture.mbg.model.DeviceSymbol;
import com.dudu.smartagriculture.service.DeviceSymbolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/symbol")
@Api(tags = "SymbolDeviceController",description = "设备符号管理")
public class DeviceSymbolController {
    @Resource
    private DeviceSymbolService deviceSymbolService;

    @ApiOperation("修改设备符号表")
    @RequestMapping(value = "/updateDeviceSymbol",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('datapresentaion:datapresentaion:update')")
    @ResponseBody
    public CommonResult updateDeviceSymbol(@RequestBody DeviceSymbol deviceSymbol){
        try {
            int i = deviceSymbolService.updateDeviceSymbol(deviceSymbol);
            if (i > 0) {
                return CommonResult.success("success");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

        return CommonResult.failed();
    }

    @ApiOperation("删除设备符号表")
    @PreAuthorize("hasAuthority('datapresentaion:datapresentaion:delete')")
    @RequestMapping(value = "/deleteDeviceSymbol",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteDeviceSymbol(String ruleName){
        try {
            int i = deviceSymbolService.deleteDeviceSymbolByRuleName(ruleName);
            if (i > 0) {
                return CommonResult.success("success");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

        return CommonResult.failed();
    }
}
