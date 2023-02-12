package com.dudu.smartagriculture.controller;


import com.dudu.smartagriculture.common.api.CommonResult;
import com.dudu.smartagriculture.dto.DeviceSymbolParam;
import com.dudu.smartagriculture.dto.SensorDeviceParam;
import com.dudu.smartagriculture.dto.SumDataDto;
import com.dudu.smartagriculture.mbg.model.DeviceController;
import com.dudu.smartagriculture.mbg.model.DeviceSensor;
import com.dudu.smartagriculture.mbg.model.DeviceSymbol;
import com.dudu.smartagriculture.service.DeviceDataService;
import com.dudu.smartagriculture.service.DeviceSensorService;
import com.dudu.smartagriculture.service.DeviceSymbolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/datapresentaion")
@Api(tags = "SensorDeviceController",description = "传感器设备管理")
public class DeviceSensorController {

    @Resource
    private DeviceSensorService deviceSensorService;
    @Resource
    private DeviceDataService deviceDataService;
    @Resource
    private DeviceSymbolService deviceSymbolService;



    @ApiOperation("获取传感器历史数据")
    @RequestMapping(value = "/getChartData",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<DeviceSensor>> getChartData(@Validated  @RequestBody SensorDeviceParam sensorDeviceParam){
        try
        {
            List<DeviceSensor> sensorDevice = deviceSensorService.getSensorDevice(sensorDeviceParam);
            return   CommonResult.success(sensorDevice);
        }catch (Exception e){
            return CommonResult.failed(e.toString());
        }
    }

    @ApiOperation("获取所有传感器控制设备的数据")
    @RequestMapping(value = "/getDataList",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SumDataDto> getDataList(){
        try
        {
            List<DeviceController> controllerListCache = deviceDataService.getControllerListCache();
            List<DeviceSensor> sensorListCache = deviceDataService.getSensorListCache();
            SumDataDto sumDataDto = new SumDataDto();
            sumDataDto.setControllerData(controllerListCache);
            sumDataDto.setSensorData(sensorListCache);
            return   CommonResult.success(sumDataDto);
        }catch (Exception e){
            return CommonResult.failed(e.toString());
        }
    }

    @ApiOperation("获取所有设备集合表")
    @RequestMapping(value = "/getDeviceList",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<DeviceSymbol>> getDeviceList(@Validated  @RequestBody DeviceSymbolParam deviceSymbolParam){
        try
        {

            List<DeviceSymbol> deviceSymbolList =deviceDataService.getDeviceList(deviceSymbolParam);
            return   CommonResult.success(deviceSymbolList);
        }catch (Exception e){
            return CommonResult.failed(e.toString());
        }
    }

    @ApiOperation("添加设备")
    @RequestMapping(value = "/addDevice",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('datapresentaion:datapresentaion:insert')")
    @ResponseBody
    public CommonResult addDevice(@Validated  @RequestBody DeviceSymbol deviceSymbol){
        try
        {

            int result = deviceSymbolService.addDevice(deviceSymbol);
            if(result >0 ){
                return CommonResult.success("success");
             }
        }catch (Exception e){
            return CommonResult.failed(e.toString());
        }

        return CommonResult.failed();
    }


}
