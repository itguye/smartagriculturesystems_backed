package com.dudu.smartagriculture.service;

import com.dudu.smartagriculture.dto.SensorDeviceParam;
import com.dudu.smartagriculture.mbg.model.DeviceSensor;
import com.dudu.smartagriculture.mbg.model.DeviceSymbol;


import java.util.List;

public interface DeviceSensorService {
    List<DeviceSensor> getSensorDevice(SensorDeviceParam sensorDeviceParam);
}
