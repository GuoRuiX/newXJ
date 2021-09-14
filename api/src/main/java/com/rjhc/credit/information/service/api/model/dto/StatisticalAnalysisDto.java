package com.rjhc.credit.information.service.api.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName StatisticalAnalysisDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/

@Data
public class StatisticalAnalysisDto extends BaseRowModel {
    /**
     * 主键
     */
    private String id;
    /**
     * 文件上传人员所属机构
     */
    private String uploadOrganization;
    @ExcelProperty(value ="年度",index = 0)
    private String years;
    @ExcelProperty(value ="月度",index = 1)
    private String month;
    /**
     * 机构编码，人行标准14位编码
     */
    @ExcelProperty(value ="机构编码",index = 2)
    private String bankCode;
    /**
     * 贷款银行详细名称
     */
    @ExcelProperty(value ="贷款银行详细名称",index = 3)
    private String bankName;
    /**
     * 金融机构地区编码
     */
    @ExcelProperty(value ="金融机构地区编码",index = 4)
    private String bankAddressCode;
    /**
     * 金融机构地区
     */
    @ExcelProperty(value ="金融机构地区",index = 5)
    private String bankAddress;
    /**
     * 地区等级查看
     */
    private String level;
    /**
     * 贫困户户籍号码
     */
    @ExcelProperty(value ="贫困户户籍号码",index = 6)
    private String poorCode;
    /**ßßß
     * 客户编号：指和金融机构后台系统一一对应的客户号码
     */
    @ExcelProperty(value ="客户编号：指和金融机构后台系统一一对应的客户号码",index = 7)

    private String customerCode;
    /**
     * 借款人姓名
     */
    @ExcelProperty(value ="借款人姓名",index = 8)
    private String customerName;
    /**
     * 证件号码
     */
    @ExcelProperty(value ="证件号码",index = 9)
    private String customerIdCard;
    /**
     * 信用户标志：1是0否（若没有此项标识，凡是信贷系统中发放的无抵押、无担保，纯信用贷款的贫困户均是信用户，该项指标按年更新）
     */
    @ExcelProperty(value ="信用户标志：1是0否（若没有此项标识，凡是信贷系统中发放的无抵押、无担保，纯信用贷款的贫困户均是信用户，该项指标按年更新）",index = 10)
    private String creditAccountLogo;
    private String creditAccountLogoName;
    /**
     * 内部信用评级等级
     */
    @ExcelProperty(value ="内部信用评级等级",index = 11)
    private String creditLevel;
    /**
     * 贷款合同编码
     */
    @ExcelProperty(value ="贷款合同编码",index = 12)
    private String contractCode;
    /**
     * 贷款合同借据编码
     */
    @ExcelProperty(value ="贷款合同借据编码",index = 13)
    private String receiptCode;
    /**
     * 贷款合同金额
     */
    @ExcelProperty(value ="贷款合同金额",index = 14)
    private BigDecimal contractaMount;
    /**
     * 借据金额：指实际发放贷款金额
     */
    @ExcelProperty(value ="借据金额：指实际发放贷款金额",index = 15)
    private BigDecimal loanAmount;
    /**
     * 借据余额：指实际发放贷款余额
     */
    @ExcelProperty(value ="借据余额：指实际发放贷款余额",index = 16)
    private BigDecimal loanBalance;
    /**
     * 贷款利率
     */
    @ExcelProperty(value ="贷款利率",index = 17)
    private BigDecimal lendingrate;
    /**
     * 贷款发放日期
     */

    private String receiveloanDate;
    @ExcelProperty(value ="贷款发放日期",index = 18)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;
    /**
     * 贷款到期日期
     */

    private String receivematurityDate;
    @ExcelProperty(value ="贷款到期日期",index = 19)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;
    /**
     * 贷款用途
     */
    @ExcelProperty(value ="贷款用途",index = 20)
    private String loanPurpose;
    private String loanPurposeName;
    /**
     * 贷款品种
     */
    @ExcelProperty(value ="贷款品种",index = 21)
    private String loanVarietie;
    private String loanVarietieName;
    /**
     * 贷款质量：1正常2关注3次级4可疑5损失（五级分类）
     */
    @ExcelProperty(value ="贷款质量：1正常2关注3次级4可疑5损失（五级分类）",index = 22)
    private String loanQuality;
    private String loanQualityName;
    /**
     * 担保方式：1信用2保证3质押4抵押5保证金（人行标准)
     */
    @ExcelProperty(value ="担保方式：1信用2保证3质押4抵押5保证金（人行标准)",index = 23)
    private String guaranteeMethod;
    private String guaranteeMethodName;
    /**
     * 贴息贷款标志：1是0否
     */
    @ExcelProperty(value ="贴息贷款标志：1是0否",index = 24)
    private String discountLoanSign;
    private String discountLoanSignName;
    /**
     * 贴息比例
     */
    @ExcelProperty(value ="贴息比例",index = 25)
    private BigDecimal discountRatio;
    /**
     * 同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationDate;
    private String type;
}
