package com.dudu.smartagriculture.easyrules.smartrule;

import java.util.Map;

public class
Trigger {
    // source:null,trigger_name:"",operator:"",value:20
    private String source;
    private String sensorName;
    private String operator;
    private Map<String,Double> value;
    // 传感器
    private Double temperature;
    private Double humidity;
    private Double illumination;
    private Double waterLevel;

    public Trigger() {
    }


    public Trigger(String source, String sensorName, String operator, Double temperature, Double humidity, Double illumination, Double waterLevel) {
        this.source = source;
        this.sensorName = sensorName;
        this.operator = operator;
        this.temperature = temperature;
        this.humidity = humidity;
        this.illumination = illumination;
        this.waterLevel = waterLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Map<String, Double> getValue() {
        return value;
    }

    public void setValue(Map<String, Double> value) {
        this.value = value;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getIllumination() {
        return illumination;
    }

    public void setIllumination(Double illumination) {
        this.illumination = illumination;
    }

    public Double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(Double waterLevel) {
        this.waterLevel = waterLevel;
    }
}
