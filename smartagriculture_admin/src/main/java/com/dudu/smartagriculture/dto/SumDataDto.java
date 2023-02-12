package com.dudu.smartagriculture.dto;



import com.dudu.smartagriculture.mbg.model.DeviceController;
import com.dudu.smartagriculture.mbg.model.DeviceSensor;


import java.util.List;

public class SumDataDto {
    private List<DeviceSensor> sensorData;
    private List<DeviceController> controllerData;

    public SumDataDto() {
    }

    public SumDataDto(List<DeviceSensor> sensorData, List<DeviceController> controllerData) {
        this.sensorData = sensorData;
        this.controllerData = controllerData;
    }

    public List<DeviceSensor> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<DeviceSensor> sensorData) {
        this.sensorData = sensorData;
    }

    public List<DeviceController> getControllerData() {
        return controllerData;
    }

    public void setControllerData(List<DeviceController> controllerData) {
        this.controllerData = controllerData;
    }
}
