package com.dudu.smartagriculture.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
public class RulesListDto {
    @Excel(name = "id", width = 10)
    private Integer id;
    @Excel(name = "ruleName", width = 20, needMerge = true)
    private String ruleName;
    @Excel(name = "ation", width = 20, needMerge = true)
    private String ation;

    @Excel(name = "createTime", width = 20,format = "yyyy-MM-dd HH:mm:ss" , needMerge = true)
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @Excel(name = "updateTime", width = 20,format = "yyyy-MM-dd HH:mm:ss", needMerge = true)
    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    @Excel(name = "remark", width = 30, needMerge = true)
    private String remark;

    @Excel(name = "expressions", width = 90, needMerge = true)
    private String expressions;


    @Excel(name = "startTime", width = 20,format = "yyyy-MM-dd HH:mm:ss", needMerge = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @Excel(name = "endTime", width = 20,format = "yyyy-MM-dd HH:mm:ss", needMerge = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    @Excel(name = "intervalTime", width = 20, needMerge = true)
    private Integer intervalTime;

    public RulesListDto() {
    }

    public RulesListDto(Integer id, String ruleName, String ation, Date createTime, Date updateTime, String remark, String expressions, Date startTime, Date endTime, Integer intervalTime) {
        this.id = id;
        this.ruleName = ruleName;
        this.ation = ation;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.expressions = expressions;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getAtion() {
        return ation;
    }

    public void setAtion(String ation) {
        this.ation = ation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExpressions() {
        return expressions;
    }

    public void setExpressions(String expressions) {
        this.expressions = expressions;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RulesList{" +
                "ruleName='" + ruleName + '\'' +
                ", ation='" + ation + '\'' +
                ", createTime='" + createTime + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
