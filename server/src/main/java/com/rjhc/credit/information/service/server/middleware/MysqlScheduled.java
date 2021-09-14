package com.rjhc.credit.information.service.server.middleware;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjhc.credit.information.service.server.dao.dataobject.AnnualStatistics;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;
import com.rjhc.credit.information.service.server.dao.mapper.AnnualStatisticsMapper;
import com.rjhc.credit.information.service.server.dao.mapper.StatisticalAnalysisMapper;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName MysqlScheduled
 * @Description: 定时任务创建信贷信息表
 * @Author grx
 * @Date 2020/9/23
 * @Version V1.0
 **/
@Component
@Slf4j
@EnableAsync
public class MysqlScheduled {
    @Value("${spring.datasource.druid.driver-class-name}")
    private String type;
    @Value("${spring.datasource.druid.url}")
    private String url;
    @Value("${spring.datasource.druid.username}")
    private String userName;
    @Value("${spring.datasource.druid.password}")
    private String password;
    @Resource
    private StatisticalAnalysisMapper statisticalAnalysisMapper;
    @Resource
    private AnnualStatisticsMapper annualStatisticsMapper;
    @Async
    @Scheduled(cron = "${data.synchronization}")
    public void  createTable() throws Exception {
        Class.forName(type);
        Connection connection = DriverManager.getConnection(url, userName, password);
        //获取当前年度
        Calendar date = Calendar.getInstance();
        String s = String.valueOf(date.get(Calendar.YEAR)-1);
        PreparedStatement preparedStatement = null;
        String tableName="agricultural_organizations_"+s;
        try {
            //判断当前表是否存在
            DatabaseMetaData metaData = connection.getMetaData();
            String type [] = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, tableName, type);
            boolean next = tables.next();
            if(next){
                String sql = "DROP TABLE IF EXISTS "+tableName;
                preparedStatement = connection.prepareStatement(sql);
                boolean execute = preparedStatement.execute();
            }
        }catch (Exception e){
            log.error("删除表失败");
            e.printStackTrace();
         }

        /**
         * 修改表名
         */
        try {
            //执行sql语句

            //修改数据库表名
            String sql ="alter table agricultural_organizations rename "+tableName;
            preparedStatement = connection.prepareStatement(sql);
            boolean execute = preparedStatement.execute();
            log.info("数据迁移成功");
            //修改表名成功，统计表数据
            Map<String, Object> map = statisticalAnalysisMapper.countAll(tableName);
            AnnualStatistics annualStatistics = new AnnualStatistics();
            annualStatistics.setId(RandomUtil.UUID36());
            annualStatistics.setYears(s);
            annualStatistics.setSynchronizationTime(new Date());
            annualStatistics.setTotalCredit(new BigDecimal(String.valueOf(map.get("loanamount"))));
            annualStatistics.setCreditBalance(new BigDecimal(String.valueOf(map.get("loanbalance"))));
            //根据年份先删除然后再新增
            LambdaQueryWrapper<AnnualStatistics> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AnnualStatistics::getYears,annualStatistics.getYears());
            annualStatisticsMapper.delete(queryWrapper);
            //重新增加当前年份数据
            annualStatisticsMapper.insert(annualStatistics);

        }catch (Exception e){
            e.printStackTrace();
            log.error("数据迁移失败");
        }

        /**
         *创建新表
         */

        try {
            String createTableSql="CREATE TABLE `agricultural_organizations` (\n" +
                    "  `id` varchar(40) NOT NULL COMMENT '主键id',\n" +
                    "  `upload_organization` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上传文件所属机构',\n" +
                    "  `years` varchar(255) NOT NULL COMMENT '年份',\n" +
                    "  `month` varchar(255) NOT NULL COMMENT '月份',\n" +
                    "  `bank_code` varchar(14) NOT NULL COMMENT '机构编码，人行标准14位编码',\n" +
                    "  `bank_name` varchar(100) NOT NULL COMMENT '贷款银行详细名称',\n" +
                    "  `bank_address_code` varchar(255) NOT NULL COMMENT '金融机构地区编码',\n" +
                    "  `bank_address` varchar(50) NOT NULL COMMENT '金融机构地区',\n" +
                    "  `poor_code` varchar(100) DEFAULT NULL COMMENT '贫困户户籍号码',\n" +
                    "  `customer_code` varchar(100) NOT NULL COMMENT '客户编号：指和金融机构后台系统一一对应的客户号码',\n" +
                    "  `customer_name` varchar(70) NOT NULL COMMENT '借款人姓名',\n" +
                    "  `customer_id_card` varchar(18) NOT NULL COMMENT '证件号码',\n" +
                    "  `credit_account_logo` varchar(1) DEFAULT NULL COMMENT '信用户标志：1是0否（若没有此项标识，凡是信贷系统中发放的无抵押、无担保，纯信用贷款的贫困户均是信用户，该项指标按年更新）',\n" +
                    "  `credit_level` varchar(3) DEFAULT NULL COMMENT '内部信用评级等级',\n" +
                    "  `contract_code` varchar(50) DEFAULT NULL COMMENT '贷款合同编码',\n" +
                    "  `receipt_code` varchar(50) NOT NULL COMMENT '贷款合同借据编码',\n" +
                    "  `contracta_mount` decimal(17,2) NOT NULL COMMENT '贷款合同金额',\n" +
                    "  `loan_amount` decimal(17,2) NOT NULL COMMENT '借据金额：指实际发放贷款金额',\n" +
                    "  `loan_balance` decimal(17,2) NOT NULL COMMENT '借据余额：指实际发放贷款余额',\n" +
                    "  `lendingrate` decimal(10,6) NOT NULL COMMENT '贷款利率',\n" +
                    "  `loan_date` date NOT NULL COMMENT '贷款发放日期',\n" +
                    "  `maturity_date` date NOT NULL COMMENT '贷款到期日期',\n" +
                    "  `loan_purpose` varchar(100) NOT NULL COMMENT '贷款用途',\n" +
                    "  `loan_varietie` varchar(100) NOT NULL COMMENT '贷款品种',\n" +
                    "  `loan_quality` varchar(1) NOT NULL COMMENT '贷款质量：1正常2关注3次级4可疑5损失（五级分类）',\n" +
                    "  `guarantee_method` varchar(1) NOT NULL COMMENT '担保方式：1信用2保证3质押4抵押5保证金（人行标准)',\n" +
                    "  `discount_loan_sign` varchar(1) NOT NULL COMMENT '贴息贷款标志：1是0否',\n" +
                    "  `discount_ratio` decimal(5,2) DEFAULT NULL COMMENT '贴息比例',\n" +
                    "  `synchronization_date` datetime NOT NULL COMMENT '同步时间',\n" +
                    "  `type` varchar(100) DEFAULT NULL COMMENT '上传数据文件',\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  KEY `loanBalance` (`loan_balance`),\n" +
                    "  KEY `loanAmount` (`loan_amount`),\n" +
                    "  KEY `bank_address` (`bank_address`),\n" +
                    "  KEY `synchronization_date` (`synchronization_date`) USING BTREE,\n" +
                    "  KEY `upload_organization` (`upload_organization`) USING BTREE,\n" +
                    "  KEY `bank_address_2` (`bank_address`,`loan_balance`,`loan_amount`),\n" +
                    "  KEY `month` (`month`,`loan_balance`,`loan_amount`),\n" +
                    "  KEY `bank_address_3` (`bank_address`,`loan_purpose`,`loan_varietie`,`loan_balance`,`loan_amount`),\n" +
                    "  KEY `bank_address_4` (`bank_address`,`credit_account_logo`,`loan_balance`,`loan_amount`),\n" +
                    "  KEY `bank_address_5` (`bank_address`,`credit_account_logo`,`customer_id_card`,`loan_balance`,`loan_amount`),\n" +
                    "  KEY `bank_address_6` (`bank_address`,`loan_date`,`credit_account_logo`,`customer_id_card`,`loan_balance`,`loan_amount`),\n" +
                    "  KEY `bank_address_7` (`bank_address`,`loan_date`,`upload_organization`,`credit_account_logo`,`customer_id_card`,`loan_balance`,`loan_amount`),\n" +
                    "  KEY `month_2` (`month`,`upload_organization`,`bank_address`,`loan_purpose`,`loan_varietie`,`loan_balance`,`loan_amount`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='涉农机构数据表';";
             preparedStatement = connection.prepareStatement(createTableSql);
            boolean execute = preparedStatement.execute();

        }catch (Exception e){
            log.error("创建表失败");
            e.printStackTrace();
        }
        preparedStatement.close();

    }
}
