package com.rjhc.credit.information.service.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName CountyDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/12/23
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountyDto {
    /**
     * 县名称
     */
    private String county;
    /**
     * 新用户数量
     */
    private long creditNo;
    /**
     * 贫困户数量
     */
    private long poorNo;
    /**
     * 信贷笔数
     */
    private long loanNo;
    /**
     * 信贷总额
     */
    private BigDecimal loanAmount;

}
