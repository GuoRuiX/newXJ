package com.rjhc.credit.information.service.server.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName OrgManager
 * @Description: 机构管理
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("organizational_management")
public class OrgManager {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 机构名称
     */
    @TableField(value = "org_name")
    private String orgName;

    /**
     * 机构编码
     */
    @TableField("org_code")
    private String orgCode;

    /**
     *状态：启用、停用
     */
    @TableField("org_status")
    private String orgStatus;

    /**
     * 机构类型 人民银行01 金融机构02
     */
    @TableField("org_type")
    private String orgType;


    /**
     * 所属地区
     */
    @TableField("area_id")
    private String areaId;

    /**
     * 是否是法人  是 否
     */
    @TableField("is_legal")
    private String isLegal;
    /**
     * 创建时间
     */
    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createDate;

}
