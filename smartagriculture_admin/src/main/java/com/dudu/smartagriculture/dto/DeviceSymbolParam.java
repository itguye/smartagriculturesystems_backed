package com.dudu.smartagriculture.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceSymbolParam {
    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "符号")
    private String symbol;

    @ApiModelProperty(value = "类型:0->传感器 1->控制器")
    private String type;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    public DeviceSymbolParam() {
    }

    public DeviceSymbolParam(String name, String symbol, String type, String ruleName) {
        this.name = name;
        this.symbol = symbol;
        this.type = type;
        this.ruleName = ruleName;
    }
}
