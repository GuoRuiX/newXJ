package com.rjhc.credit.information.service.api.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName OperationLogDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/25
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogDto extends BaseRowModel {
    /**
     * 唯一标识
     */

    private String id;
    /**
     * 机构ID
     */
    private String bankId;
    /**
     * 机构名称
     */
    private String banekName;
    /**
     * 用户名
     */
    @ExcelProperty(value ="用户名",index = 0)
    private String userName;
    /**
     * 贫困户id
     */
    private String idCard;
    /**
     * 模块名称
     */
    @ExcelProperty(value ="模块名称",index = 0)
    private String modelName;
    /**
     * 操作名称
     */
    @ExcelProperty(value ="操作名称",index = 0)
    private String operationName;
    /**
     * 操作时间
     */
    @ExcelProperty(value ="操作时间",index = 0)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;
    /**
     * 源码
     */
    private String sourceCode;
}
