package com.dudu.smartagriculture.service.impl;

import com.dudu.smartagriculture.common.service.RedisService;
import com.dudu.smartagriculture.dao.SaveDataToDBDao;
import com.dudu.smartagriculture.dto.DeviceSymbolParam;
import com.dudu.smartagriculture.mbg.mapper.DeviceControllerMapper;
import com.dudu.smartagriculture.mbg.mapper.DeviceSensorMapper;
import com.dudu.smartagriculture.mbg.mapper.DeviceSymbolMapper;
import com.dudu.smartagriculture.mbg.model.*;
import com.dudu.smartagriculture.service.DeviceDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DeviceDataServiceImpl implements DeviceDataService {

    @Resource
    private SaveDataToDBDao saveDataToDBDao;
    @Resource
    private RedisService redisService;
    @Resource
    private DeviceSensorMapper sensorMapper;
    @Resource
    private DeviceControllerMapper controllerMapper;
    @Resource
    private DeviceSymbolMapper deviceSymbolMapper;

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;


    /**
     * 保存传感器值到数据库中
     * @param sensors
     */
    @Override
    public void saveSensors(List<DeviceSensor> sensors) {
        saveDataToDBDao.saveSensors(sensors);
    }

    /**
     * 保存控制器到数据库中
     * @param controllerDevices
     */
    @Override
    public void saveController(List<DeviceController> controllerDevices) {
        saveDataToDBDao.saveController(controllerDevices);
    }


    /**
     * 从数据库中获取传感器数据
     * @return
     */
    @Override
    public List<DeviceSensor> getSensorListDB(){
        List<DeviceSensor> sensorList = sensorMapper.selectByExample(new DeviceSensorExample());
        return sensorList;
    }


    /**
     * 从数据库中获取控制器数据
     * @return
     */
    @Override
    public List<DeviceController> getControllerListDB() {
        List<DeviceController> deviceControllers = controllerMapper.selectByExample(new DeviceControllerExample());
        return deviceControllers;
    }

    /**
     * 保存传感器数据到Redis缓存中
     * @param sensors
     */
    @Override
    public void saveSensorsRedis(List<DeviceSensor> sensors) {
      String  sensorkey = REDIS_DATABASE+":"+"sensors:list";
        redisService.set(sensorkey, sensors, REDIS_EXPIRE);
    }

    /**
     * 保存控制器设备数据到Redis缓存中
     * @param controllerDevices
     */
    @Override
    public void saveControllerRedis(List<DeviceController> controllerDevices) {
        String  controollerkey = REDIS_DATABASE+":"+"controllers:list";
        redisService.set(controollerkey, controllerDevices, REDIS_EXPIRE);
    }

    /**
     * 从Redis缓存中获取传感器设备数据
     * @return
     */
    @Override
    public List<DeviceSensor> getSensorListCache() {
      String  sensorkey = REDIS_DATABASE+":"+"sensors:list";
        List<DeviceSensor> sensorList = (List<DeviceSensor>) redisService.get(sensorkey);
      return sensorList;
    }

    /**
     * 从Redis缓存中获取控制器设备数据
     * @return
     */
    @Override
    public List<DeviceController> getControllerListCache() {
        String  controollerkey = REDIS_DATABASE+":"+"controllers:list";
        List<DeviceController> deviceControllers = (List<DeviceController>) redisService.get(controollerkey);
        return deviceControllers;
    }

    /**
     * 客户端获取传感器数据
     */
    public List<DeviceSensor>  getSensorList(){
        // 首先从缓存中获取
        List<DeviceSensor> sensorListCache = getSensorListCache();
        if (sensorListCache != null) {
            return sensorListCache;
        }
        // 从数据库获取数据
        return getSensorListDB();
    }

    /**
     * 客户端获取控制器数据
     * @return
     */
    public List<DeviceController> getControllerList(){
        List<DeviceController> deviceControllerList = getControllerListCache();
        // 从缓存中获取数据
        if (deviceControllerList != null) {
            return deviceControllerList;
        }
        // 如果缓存中没有数据就从数据库中获取数据
        return getControllerListDB();
    }


    /**
     * 获取当前设备的规则Map数据
     * @return 规则Map数据
     */
    @Override
    public Map<String,Double> getCurrentSensorMapCache(){
        // 获取缓存中的传感器数据
        List<DeviceSensor> sensorListCache = getSensorListCache();
        // 表示缓存中有数据
        if (sensorListCache.size() > 0) {
            Map<String, Double> rulesMap = new HashMap<>();
            for (DeviceSensor sensor : sensorListCache) {
                rulesMap.put(sensor.getRuleName(), sensor.getData());// 将数据存放到规则Map中
            }
            return rulesMap;
        }
        return null;
    }

    /**
     * 获取所以设备对照表的数据
     * @param deviceSymbolParam
     * @return
     */
    @Override
    public List<DeviceSymbol> getDeviceList(DeviceSymbolParam deviceSymbolParam) {
        DeviceSymbolExample deviceSymbolExample = new DeviceSymbolExample();
        if (deviceSymbolParam.getName() != null) {
            deviceSymbolExample.createCriteria().andNameEqualTo(deviceSymbolParam.getName());
        }

        if (deviceSymbolParam.getRuleName()!=null) {
            deviceSymbolExample.createCriteria().andRuleNameEqualTo(deviceSymbolParam.getRuleName());
        }

        if (deviceSymbolParam.getSymbol() != null) {
            deviceSymbolExample.createCriteria().andSymbolEqualTo(deviceSymbolParam.getSymbol());
        }

        if (deviceSymbolParam.getType() != null) {
            deviceSymbolExample.createCriteria().andTypeEqualTo(deviceSymbolParam.getType());
        }

        return  deviceSymbolMapper.selectByExample(deviceSymbolExample);
    }


}
