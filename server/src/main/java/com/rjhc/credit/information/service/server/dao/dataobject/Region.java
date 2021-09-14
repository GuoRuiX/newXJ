package com.rjhc.credit.information.service.server.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName Region
 * @Description: TODO
 * @Author grx
 * @Date 2020/11/23
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_region")
public class Region {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 级别
     */
    @TableField(value = "region_level")
    private Integer regionLevel;
    /**
     * 地区名称
     */
    @TableField(value = "region_name")
    private String regionName;
    /**
     * 地区编码
     */
    @TableField(value = "region_code")
    private String regionCode;
    /**
     * 所属上级
     */
    @TableField(value = "parent_id")
    private String parentId;
    /**
     * 地区说明
     */
    @TableField(value = "region_explain")
    private String regionExplain;
    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;
    /**
     * 修改时间
     */
    @TableField(value = "update_date")
    private Date updateDate;

}
