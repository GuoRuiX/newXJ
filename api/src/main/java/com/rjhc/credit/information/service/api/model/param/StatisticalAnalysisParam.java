package com.rjhc.credit.information.service.api.model.param;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rjhc.matrix.framework.core.bean.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @ClassName StatisticalAnalysisParam
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalAnalysisParam extends BaseModel {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 文件上传人员所属机构
     */
    @ApiModelProperty(value = "文件上传人员所属机构")
    private String uploadOrganization;
    /**
     * 年度
     */
    @ApiModelProperty(value = "年度")
    private String years;
    /**
     * 月度
     */
    @ApiModelProperty(value = "月度")
    private String month;

    /**
     * 机构编码，人行标准14位编码
     */
    @ApiModelProperty(value = "机构编码")
    private String bankCode;
    /**
     * 贷款银行详细名称
     */
    @ApiModelProperty(value = "贷款银行详细名称")
    private String bankName;
    /**
     * 金融机构地区编码
     */
    @ApiModelProperty(value = "金融机构地区编码")
    private String bankAddressCode;
    /**
     * 金融机构地区
     */
    @ApiModelProperty(value = "金融机构地区")
    private String bankAddress;
    /**
     * 贫困户户籍号码
     */
    @ApiModelProperty(value = "贫困户户籍号码")
    private String poorCode;
    /**
     * 客户编号：指和金融机构后台系统一一对应的客户号码
     */
    @ApiModelProperty(value = "客户编号")
    private String customerCode;
    /**
     * 借款人姓名
     */
    @ApiModelProperty(value = "借款人姓名")
    private String customerName;
    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码")
    private String customerIdCard;
    /**
     * 信用户标志：1是0否（若没有此项标识，凡是信贷系统中发放的无抵押、无担保，纯信用贷款的贫困户均是信用户，该项指标按年更新）
     */
    @ApiModelProperty(value = "信用户标志")
    private String creditAccountLogo;
    /**
     * 内部信用评级等级
     */
    @ApiModelProperty(value = "内部信用评级等级")
    private String creditLevel;
    /**
     * 贷款合同编码
     */
    @ApiModelProperty(value = "贷款合同编码")
    private String contractCode;
    /**
     * 贷款合同借据编码
     */
    @ApiModelProperty(value = "贷款合同借据编码")
    private String receiptCode;
    /**
     * 贷款合同金额
     */
    @ApiModelProperty(value = "贷款合同金额")
    private BigDecimal contractaMount;
    /**
     * 借据金额：指实际发放贷款金额
     */
    @ApiModelProperty(value = "借据金额")
    private BigDecimal loanAmount;
    /**
     * 借据余额：指实际发放贷款余额
     */
    @ApiModelProperty(value = "借据余额")
    private BigDecimal loanBalance;
    /**
     * 贷款利率
     */
    @ApiModelProperty(value = "贷款利率")
    private BigDecimal lendingrate;
    /**
     * 贷款发放日期
     */
    @ApiModelProperty(value = "贷款发放日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;
    /**
     * 贷款到期日期
     */
    @ApiModelProperty(value = "贷款到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;
    /**
     * 贷款用途
     */
    @ApiModelProperty(value = "贷款用途")
    private String loanPurpose;
    /**
     * 贷款品种
     */
    @ApiModelProperty(value = "贷款品种")
    private String loanVarietie;
    /**
     * 贷款质量：1正常2关注3次级4可疑5损失（五级分类）
     */
    @ApiModelProperty(value = "贷款质量")
    private String loanQuality;
    /**
     * 担保方式：1信用2保证3质押4抵押5保证金（人行标准)
     */
    @ApiModelProperty(value = "担保方式")
    private String guaranteeMethod;
    /**
     * 贴息贷款标志：1是0否
     */
    @ApiModelProperty(value = "贴息贷款标志")
    private String discountLoanSign;
    /**
     * 贴息比例
     */
    @ApiModelProperty(value = "贴息比例")
    private BigDecimal discountRatio;
    /**
     * 同步时间
     */
    @ApiModelProperty(value = "同步时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date synchronizationDate;



    private BigDecimal startLoanAmount;//开始查询借据金额
    private BigDecimal endLoanAmount;//结束查询借据金额


    //借款时间查询
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startMaturityDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endMaturityDate;
    //还款时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startLoanDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endLoanDate;
    //当前商行下级机构
    private List<String> orgList;

    private String level;

    private String deptOrg;
    private HashSet<String> deptList;
}
