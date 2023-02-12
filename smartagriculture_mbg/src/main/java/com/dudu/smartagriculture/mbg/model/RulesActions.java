package com.dudu.smartagriculture.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

public class RulesActions implements Serializable {
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Integer id;

    @ApiModelProperty(value = "设备来源")
    private String source;

    @ApiModelProperty(value = "设备名称")
    private String actionName;

    @ApiModelProperty(value = "设备操作:0:关;1:开")
    private Boolean act;

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

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Boolean getAct() {
        return act;
    }

    public void setAct(Boolean act) {
        this.act = act;
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
        sb.append(", actionName=").append(actionName);
        sb.append(", act=").append(act);
        sb.append(", fid=").append(fid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}