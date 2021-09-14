package com.rjhc.credit.information.service.api.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName PoorHouseholdsInformationDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
@Data
public class PoorHouseholdsInformationDto extends BaseRowModel {
    /**
     * 主键
     */

    private String id;
    /**
     * 年份
     */
    private String years;
    /**
     * 月份
     */
    private String month;
    /**
     * 机构编码，人行标准14位编码
     */
    private String bankCode;
    /**
     * 地州
     */
    @ExcelProperty(value ="地州",index = 0)
    private String prefecture;
    /**
     * 县市
     */
    @ExcelProperty(value ="县市",index = 1)
    private String counties;
    /**
     * 乡镇
     */
    @ExcelProperty(value ="乡镇",index = 2)
    private String township;
    /**
     * 行政村
     */
    @ExcelProperty(value ="行政村",index = 3)
    private String administrativeVillage;
    /**
     * 行政村编码
     */
    @ExcelProperty(value ="行政村编码",index = 4)
    private String administrativeVillageCode;
    /**
     * 户主姓名
     */
    @ExcelProperty(value ="户主姓名",index = 5)
    private String householderName;
    /**
     * 证件号码
     */
    @ExcelProperty(value ="证件号码",index = 6)
    private String idCard;
    /**
     * 家庭住址
     */
    @ExcelProperty(value ="家庭住址",index = 7)
    private String address;
    /**
     * 贫困户编码
     */
    @ExcelProperty(value ="贫困户编码",index = 8)
    private String poorHouseholdsCode;
    /**
     * 贫困户人口编码
     */
    @ExcelProperty(value ="贫困户人口编码",index = 9)
    private String populationCode;
    /**
     * 贫困户属性
     */
    @ExcelProperty(value ="贫困户属性",index = 10)
    private String poorHouseholdsAttribute;
    /**
     * 证件类型
     */
    @ExcelProperty(value ="证件类型",index = 11)
    private String idCardType;
    /**
     * 健康状态
     */
    @ExcelProperty(value ="健康状态",index = 12)
    private String health;
    /**
     * 脱贫标志
     */
    @ExcelProperty(value ="脱贫标志",index = 13)
    private String povertySign;
    @ExcelProperty(value ="脱贫标志标准",index = 14)
    private String povertySignStandard;
    /**
     * 家庭人口数
     */
    @ExcelProperty(value ="家庭人口数",index = 15)
    private Integer familySize;
    @ExcelProperty(value ="家庭人口数标准",index = 16)
    private Integer familySizeStandard;
    /**
     * 与户主关系和字典表关联
     */
    private String relationShip;
    @ExcelProperty(value ="与户主关系",index = 17)
    private String relationShipName;
    /**
     * 性别
     */
    @ExcelProperty(value ="性别",index = 18)
    private String gender;
    /**
     * 年龄
     */
    @ExcelProperty(value ="年龄",index = 19)
    private Integer age;
    /**
     * 名族
     */
    @ExcelProperty(value ="名族",index = 20)
    private String nation;
    /**
     * 耕地面积
     */
    @ExcelProperty(value ="耕地面积",index = 21)
    private BigDecimal landarea;
    @ExcelProperty(value ="耕地面积标准",index = 22)
    private BigDecimal landareaStandard;
    /**
     * 林地面积
     */
    @ExcelProperty(value ="林地面积",index = 23)
    private BigDecimal woodlandarea;
    @ExcelProperty(value ="林地面积标准",index = 24)
    private BigDecimal woodlandareaStandard;
    /**
     * 人均纯收入
     */
    @ExcelProperty(value ="人均纯收入",index = 25)
    private BigDecimal income;
    @ExcelProperty(value ="人均纯收入标准",index = 26)
    private BigDecimal incomeStandard;

    /**
     * 文件授权书
     */
    private Object image;
    /**
     * 同步时间
     */
    @ExcelProperty(value ="时间",index = 27)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationDate;
}
