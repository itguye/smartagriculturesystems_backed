package com.dudu.smartagriculture.dto;

import com.dudu.smartagriculture.mbg.model.UmsAdmin;
import com.dudu.smartagriculture.mbg.model.UmsRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 *用户个人信息的参数
 */
@Getter
@Setter
public class UmsAdminUserInfoParam {
    @ApiModelProperty(value = "用户名标识")
    private Long id;
    @NotEmpty
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户头像")
    private String icon;
    @Email
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    @ApiModelProperty(value = "用户昵称", required = true)
    private String nickName;
    @ApiModelProperty(value = "备注", required = true)
    private String note;
    @ApiModelProperty(value = "角色", required = true)
    private String roles;
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}
