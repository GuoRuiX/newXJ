package com.rjhc.credit.information.service.api.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rjhc.matrix.framework.core.bean.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName OrgManagerParam
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgManagerParam extends BaseModel {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String orgName;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private String orgCode;

    /**
     *状态：启用、停用
     */
    @ApiModelProperty(value = "状态")
    private String orgStatus;

    /**
     * 机构类型 人民银行01 金融机构02
     */
    @ApiModelProperty(value = "机构类型")
    private String orgType;


    /**
     * 所属地区
     */
    @ApiModelProperty(value = "所属地区")
    private String areaId;

    /**
     * 是否是法人  是 否
     */
    @ApiModelProperty(value = "是否是法人")
    private String isLegal;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createDate;
}
