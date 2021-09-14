package com.rjhc.credit.information.service.api.model.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rjhc.matrix.framework.core.bean.BaseModel;
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
public class OperationLogParam extends BaseModel {
    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private String bankId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 贫困户id
     */
    @ApiModelProperty(value = "贫困户id")
    private String idCard;
    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    private String modelName;
    /**
     * 操作名称
     */
    @ApiModelProperty(value = "操作名称")
    private String operationName;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;
    /**
     * 源码
     */
    @ApiModelProperty(value = "源码")
    private String sourceCode;
}
