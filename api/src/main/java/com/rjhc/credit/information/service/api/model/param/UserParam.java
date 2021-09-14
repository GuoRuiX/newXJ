package com.rjhc.credit.information.service.api.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rjhc.matrix.framework.core.bean.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserParam
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserParam extends BaseModel {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 登录ID
     */
    @ApiModelProperty(value = "登录ID")
    private String loginId;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名")
    private String loginName;

    /**
     *密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private Integer userType;


    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private String pbcCode;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String pbcName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createDate;
}
