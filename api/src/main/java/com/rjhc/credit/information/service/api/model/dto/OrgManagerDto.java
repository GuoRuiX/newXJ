package com.rjhc.credit.information.service.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName OrgManagerDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgManagerDto {

    /**
     * 主键
     */
    private String id;
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     *状态：启用、停用
     */
    private String orgStatus;

    /**
     * 机构类型 人民银行01 金融机构02
     */
    private String orgType;


    /**
     * 所属地区
     */
    private String areaId;

    /**
     * 是否是法人  是 否
     */
    private String isLegal;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createDate;
}
