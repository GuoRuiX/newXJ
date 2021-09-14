package com.rjhc.credit.information.service.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName AnnualStatisticsDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualStatisticsDto {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 年份
     */
    private String years;
    /**
     * 借贷总额
     */
    private BigDecimal totalCredit;
    /**
     * 借贷余额
     */
    private BigDecimal creditBalance;
    /**
     * 同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationTime;

    private long counts;
}
