package com.rjhc.credit.information.service.api.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rjhc.matrix.framework.core.bean.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName PoorHouseholdsInformationParam
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoorHouseholdsInformationParam extends BaseModel {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String years;
    /**
     * 地州
     */
    @ApiModelProperty(value = "地州")
    private String prefecture;
    /**
     * 县市
     */
    @ApiModelProperty(value = "县市")
    private String counties;
    /**
     * 乡镇
     */
    @ApiModelProperty(value = "乡镇")
    private String township;
    /**
     * 行政村
     */
    @ApiModelProperty(value = "行政村")
    private String administrativeVillage;
    /**
     * 行政村编码
     */
    @ApiModelProperty(value = "行政村编码")
    private String administrativeVillageCode;
    /**
     * 户主姓名
     */
    @ApiModelProperty(value = "户主姓名")
    private String householderName;
    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码")
    private String idCard;
    /**
     * 家庭住址
     */
    @ApiModelProperty(value = "家庭住址")
    private String address;
    /**
     * 贫困户编码
     */
    @ApiModelProperty(value = "贫困户编码")
    private String poorHouseholdsCode;
    /**
     * 贫困户人口编码
     */
    @ApiModelProperty(value = "贫困户人口编码")
    private String populationCode;
    /**
     * 贫困户属性
     */
    @ApiModelProperty(value = "贫困户属性")
    private String poorHouseholdsAttribute;
    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型")
    private String idCardType;
    /**
     * 健康状态
     */
    @ApiModelProperty(value = "健康状态")
    private String health;
    /**
     * 脱贫标志
     */
    @ApiModelProperty(value = "脱贫标志")
    private String povertySign;
    /**
     * 家庭人口数
     */
    @ApiModelProperty(value = "家庭人口数")
    private Integer familySize;
    /**
     * 与户主关系和字典表关联
     */
    @ApiModelProperty(value = "与户主关系和字典表关联")
    private String relationShip;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;
    /**
     * 名族
     */
    @ApiModelProperty(value = "名族")
    private String nation;
    /**
     * 耕地面积
     */
    @ApiModelProperty(value = "耕地面积")
    private BigDecimal landarea;
    /**
     * 林地面积
     */
    @ApiModelProperty(value = "林地面积")
    private BigDecimal woodlandarea;
    /**
     * 人均纯收入
     */
    @ApiModelProperty(value = "人均纯收入")
    private BigDecimal income;

    /**
     * 文件授权书
     */
    @ApiModelProperty(value = "文件授权书")
    private Object image;
    /**
     * 同步时间
     */
    @ApiModelProperty(value = "同步时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationDate;

    //区间查询字段 年龄
    private Integer startAge;
    private Integer endAge;
    //家庭人口数
    private Integer startFamilySize;
    private Integer endFamilySize;
    //耕地面积
    private BigDecimal startLandarea;
    private BigDecimal endLandarea;
    //林地面积
    private BigDecimal startWoodlandarea;
    private BigDecimal endWoodlandarea;
    //人均纯收入
    private BigDecimal startIncome;
    private BigDecimal endIncome;

}
