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
 * @ClassName DataReporting
 * @Description: 贫困户基础信息数据
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("poor_households_Information")
public class PoorHouseholdsInformation {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 年份
     */
    @TableField(value = "years")
    private String years;
    /**
     * 地州
     */
    @TableField(value = "prefecture")
    private String prefecture;
    /**
     * 县市
     */
    @TableField(value = "counties")
    private String counties;
    /**
     * 乡镇
     */
    @TableField(value = "township")
    private String township;
    /**
     * 行政村
     */
    @TableField(value = "administrative_village")
    private String administrativeVillage;
    /**
     * 行政村编码
     */
    @TableField(value = "administrative_village_code")
    private String administrativeVillageCode;
    /**
     * 户主姓名
     */
    @TableField(value = "householdername")
    private String householderName;
    /**
     * 证件号码
     */
    @TableField(value = "id_card")
    private String idCard;
    /**
     * 家庭住址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 贫困户编码
     */
    @TableField(value = "poor_households_code")
    private String poorHouseholdsCode;
    /**
     * 贫困户人口编码
     */
    @TableField(value = "population_code")
    private String populationCode;
    /**
     * 贫困户属性
     */
    @TableField(value = "poor_households_attribute")
    private String poorHouseholdsAttribute;
    /**
     * 证件类型
     */
    @TableField(value = "id_card_type")
    private String idCardType;
    /**
     * 健康状态
     */
    @TableField(value = "health")
    private String health;
    /**
     * 脱贫标志
     */
    @TableField(value = "povertysign")
    private String povertySign;
    /**
     * 家庭人口数
     */
    @TableField(value = "familysize")
    private Integer familySize;
    /**
     * 与户主关系和字典表关联
     */
    @TableField(value = "relationship")
    private String relationShip;
    /**
     * 性别
     */
    @TableField(value = "gender")
    private String gender;
    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;
    /**
     * 名族
     */
    @TableField(value = "nation")
    private String nation;
    /**
     * 耕地面积
     */
    @TableField(value = "landarea")
    private BigDecimal landarea;
    /**
     * 林地面积
     */
    @TableField(value = "woodlandarea")
    private BigDecimal woodlandarea;
    /**
     * 人均纯收入
     */
    @TableField(value = "income")
    private BigDecimal income;
    /**
     * 文件授权书
     */
    @TableField(value = "image")
    private Object image;
    /**
     * 同步时间
     */
    @TableField(value = "synchronization_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationDate;
}
