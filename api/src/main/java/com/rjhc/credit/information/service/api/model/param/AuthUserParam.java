package com.rjhc.credit.information.service.api.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AuthUserParam
 * @Description: TODO
 * @Author grx
 * @Date 2021/1/15
 * @Version V1.0
 **/
@Data
public class AuthUserParam {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String passWord;
}
