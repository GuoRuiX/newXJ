package com.rjhc.credit.information.service.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ClassName AuthDto
 * @Description: TODO
 * @Author grx
 * @Date 2021/1/15
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    /**
     * token
     */
    private String token;

    /**
     * 刷新token
     */
    private String refreshToken;

    private Map<String,Object> user;
}
