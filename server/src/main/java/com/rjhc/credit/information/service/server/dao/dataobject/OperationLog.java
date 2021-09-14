package com.rjhc.credit.information.service.server.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Date;

/**
 * @ClassName OperationLog
 * @Description: 操作日志表
 * @Author grx
 * @Date 2020/8/21
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("operation_log")
public class OperationLog extends Serializers.Base {
    /**
     * 唯一标识
     */
    @TableId(value = "id")
    private String id;
    /**
     * 机构ID
     */
    @TableField("bank_id")
    private String bankId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 贫困户id
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 模块名称
     */
    @TableField("model_name")
    private String modelName;
    /**
     * 操作名称
     */
    @TableField("operation_name")
    private String operationName;
    /**
     * 操作时间
     */
    @TableField("operation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;
    /**
     * 源码
     */
    @TableField("source_code")
    private String sourceCode;
}
