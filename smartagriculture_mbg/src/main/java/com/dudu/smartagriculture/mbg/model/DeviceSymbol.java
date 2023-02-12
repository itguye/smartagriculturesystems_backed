package com.dudu.smartagriculture.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class DeviceSymbol implements Serializable {
    @ApiModelProperty(value = "编号")
    private Integer id;

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "符号")
    private String symbol;

    @ApiModelProperty(value = "类型:0->传感器 1->控制器")
    private String type;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", symbol=").append(symbol);
        sb.append(", type=").append(type);
        sb.append(", ruleName=").append(ruleName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}