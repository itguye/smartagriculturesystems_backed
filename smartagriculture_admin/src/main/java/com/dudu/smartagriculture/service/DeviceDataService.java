package com.dudu.smartagriculture.service;



import com.dudu.smartagriculture.dto.DeviceSymbolParam;
import com.dudu.smartagriculture.mbg.model.DeviceController;
import com.dudu.smartagriculture.mbg.model.DeviceSensor;
import com.dudu.smartagriculture.mbg.model.DeviceSymbol;


import java.util.List;
import java.util.Map;

/*
将传感器数据保存到数据库中
 */
public interface DeviceDataService {
    void saveSensors(List<DeviceSensor> sensors);
    void saveController(List<DeviceController> controllerDevices);
    void saveSensorsRedis(List<DeviceSensor> sensors);
    void saveControllerRedis(List<DeviceController> controllerDevices);
    List<DeviceController> getControllerListCache();
    List<DeviceController> getControllerListDB();
    List<DeviceSensor> getSensorListDB();
    List<DeviceSensor> getSensorListCache();
    Map<String,Double> getCurrentSensorMapCache();
    List<DeviceSymbol> getDeviceList(DeviceSymbolParam deviceSymbolParam);
}
