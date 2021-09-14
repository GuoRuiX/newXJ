package com.rjhc.credit.information.service.server.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName AnnualStatistics
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("annual_statistics")
public class AnnualStatistics {
    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;
    /**
     * 年份
     */
    @TableField(value = "years")
    private String years;
    /**
     * 借贷总额
     */
    @TableField(value = "total_credit")
    private BigDecimal totalCredit;
    /**
     * 借贷余额
     */
    @TableField(value = "credit_balance")
    private BigDecimal creditBalance;
    /**
     * 同步时间
     */
    @TableField(value = "synchronization_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationTime;
}
