package com.rjhc.credit.information.service.server.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName statisticalAnalysis
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("agricultural_organizations")
public class StatisticalAnalysis {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 文件上传人员所属机构
     */
    @TableField(value = "upload_organization")
    private String uploadOrganization;
    /**
     * 年度
     */
    @TableField(value = "years")
    private String years;
    /**
     * 月度
     */
    @TableField(value = "month")
    private String month;
    /**
     * 机构编码，人行标准14位编码
     */
    @TableField(value = "bank_code")
    private String bankCode;
    /**
     * 贷款银行详细名称
     */
    @TableField(value = "bank_name")
    private String bankName;
    /**
     * 金融机构地区编码
     */
    @TableField(value = "bank_address_code")
    private String bankAddressCode;
    /**
     * 金融机构地区
     */
    @TableField(value = "bank_address")
    private String bankAddress;
    /**
     * 贫困户户籍号码
     */
    @TableField(value = "poor_code")
    private String poorCode;
    /**
     * 客户编号：指和金融机构后台系统一一对应的客户号码
     */
    @TableField(value = "customer_code")
    private String customerCode;
    /**
     * 借款人姓名
     */
    @TableField(value = "customer_name")
    private String customerName;
    /**
     * 证件号码
     */
    @TableField(value = "customer_id_card")
    private String customerIdCard;
    /**
     * 信用户标志：1是0否（若没有此项标识，凡是信贷系统中发放的无抵押、无担保，纯信用贷款的贫困户均是信用户，该项指标按年更新）
     */
    @TableField(value = "credit_account_logo")
    private String creditAccountLogo;
    /**
     * 内部信用评级等级
     */
    @TableField(value = "credit_level")
    private String creditLevel;
    /**
     * 贷款合同编码
     */
    @TableField(value = "contract_code")
    private String contractCode;
    /**
     * 贷款合同借据编码
     */
    @TableField(value = "receipt_code")
    private String receiptCode;
    /**
     * 贷款合同金额
     */
    @TableField(value = "contracta_mount")
    private BigDecimal contractaMount;
    /**
     * 借据金额：指实际发放贷款金额
     */
    @TableField(value = "loan_amount")
    private BigDecimal loanAmount;
    /**
     * 借据余额：指实际发放贷款余额
     */
    @TableField(value = "loan_balance")
    private BigDecimal loanBalance;
    /**
     * 贷款利率
     */
    @TableField(value = "lendingrate")
    private BigDecimal lendingrate;
    /**
     * 贷款发放日期
     */
    @TableField(value = "loan_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;
    /**
     * 贷款到期日期
     */
    @TableField(value = "maturity_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;
    /**
     * 贷款用途
     */
    @TableField(value = "loan_purpose")
    private String loanPurpose;
    /**
     * 贷款品种
     */
    @TableField(value = "loan_varietie")
    private String loanVarietie;
    /**
     * 贷款质量：1正常2关注3次级4可疑5损失（五级分类）
     */
    @TableField(value = "loan_quality")
    private String loanQuality;
    /**
     * 担保方式：1信用2保证3质押4抵押5保证金（人行标准)
     */
    @TableField(value = "guarantee_method")
    private String guaranteeMethod;
    /**
     * 贴息贷款标志：1是0否
     */
    @TableField(value = "discount_loan_sign")
    private String discountLoanSign;
    /**
     * 贴息比例
     */
    @TableField(value = "discount_ratio")
    private BigDecimal discountRatio;
    /**
     * 同步时间
     */
    @TableField(value = "synchronization_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationDate;
    @TableField(value = "type")
    private String type;




}
