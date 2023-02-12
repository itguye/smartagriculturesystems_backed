package com.dudu.smartagriculture.dto;



import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.dudu.smartagriculture.mbg.model.RulesActions;
import com.dudu.smartagriculture.mbg.model.RulesForms;
import com.dudu.smartagriculture.mbg.model.RulesTriggers;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RulesFormsDataDto extends RulesForms implements Serializable {
    private Integer id;
    private String sceneName;
    private String remark;
    private List<RulesTriggers> triggers;
    private List<RulesActions> actions;
    @JsonProperty("condition")
    private String conditional;
    private Integer intervalTime;


    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;



    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;



    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    public RulesFormsDataDto() {
    }

    public RulesFormsDataDto(List<RulesTriggers> triggers, List<RulesActions> actions) {
        this.triggers = triggers;
        this.actions = actions;
    }

    public List<RulesTriggers> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<RulesTriggers> triggers) {
        this.triggers = triggers;
    }

    public List<RulesActions> getActions() {
        return actions;
    }

    public void setActions(List<RulesActions> actions) {
        this.actions = actions;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }
}
