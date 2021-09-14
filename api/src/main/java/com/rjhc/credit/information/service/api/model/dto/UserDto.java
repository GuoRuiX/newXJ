package com.rjhc.credit.information.service.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    /**
     * 主键
     */
    private String id;
    /**
     * 登录ID
     */
    private String loginId;

    /**
     * 登录名
     */
    private String loginName;

    /**
     *密码
     */
    private String password;

    /**
     * 用户类型
     */
    private Integer userType;


    /**
     * 机构编码
     */
    private String pbcCode;

    /**
     * 机构名称
     */
    private String pbcName;

    private VueEntityDto vueEntityDto;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createDate;
}
