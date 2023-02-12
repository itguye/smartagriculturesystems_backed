package com.dudu.smartagriculture.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

public class RulesTriggers implements Serializable {

    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Integer id;

    @ApiModelProperty(value = "设备来源")
    private String source;

    @ApiModelProperty(value = "设备名称")
    private String sensorName;

    @ApiModelProperty(value = "比较条件")
    private String operator;

    @ApiModelProperty(value = "比较值")
    private Double value;

    @ApiModelProperty(value = "外键,rulesforms")
    private Integer fid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", source=").append(source);
        sb.append(", sensorName=").append(sensorName);
        sb.append(", operator=").append(operator);
        sb.append(", value=").append(value);
        sb.append(", fid=").append(fid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}