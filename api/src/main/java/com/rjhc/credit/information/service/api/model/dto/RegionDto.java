package com.rjhc.credit.information.service.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName RegionDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/11/23
 * @Version V1.0
 **/
@Data
public class RegionDto {
    /**
     * 主键
     */
    private String id;
    /**
     * 级别
     */
    private Integer regionLevel;
    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 地区编码
     */
    private String regionCode;
    /**
     * 所属上级
     */
    private String parentId;
    /**
     * 地区说明
     */
    private String regionExplain;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date updateDate;
}
