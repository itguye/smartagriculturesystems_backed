package com.dudu.smartagriculture.dao;


import com.dudu.smartagriculture.mbg.model.DeviceController;
import com.dudu.smartagriculture.mbg.model.DeviceSensor;

import java.util.List;

public interface SaveDataToDBDao {
    public void saveSensors( List<DeviceSensor> devicesSensor);
    public void saveController( List<DeviceController> devicesController);
}
