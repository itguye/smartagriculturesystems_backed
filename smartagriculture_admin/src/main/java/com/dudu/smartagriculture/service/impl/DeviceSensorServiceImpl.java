package com.dudu.smartagriculture.service.impl;

import com.dudu.smartagriculture.dto.SensorDeviceParam;
import com.dudu.smartagriculture.mbg.mapper.DeviceSensorMapper;
import com.dudu.smartagriculture.mbg.model.DeviceSensor;
import com.dudu.smartagriculture.mbg.model.DeviceSensorExample;
import com.dudu.smartagriculture.service.DeviceSensorService;;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;


@Service
public class DeviceSensorServiceImpl implements DeviceSensorService {
    @Resource
    private DeviceSensorMapper deviceSensorMapper;

    @Override
    public List<DeviceSensor> getSensorDevice(SensorDeviceParam sensorDeviceParam) {
        DeviceSensorExample example = new DeviceSensorExample();
        example.createCriteria().andNameEqualTo(sensorDeviceParam.getName()).andTimeBetween(sensorDeviceParam.getStartTime(),sensorDeviceParam.getEndTime());
        List<DeviceSensor> sensorDevices = deviceSensorMapper.selectByExample(example);
        return sensorDevices;
    }
}
