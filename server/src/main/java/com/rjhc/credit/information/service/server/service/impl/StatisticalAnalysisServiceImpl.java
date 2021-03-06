package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.CountyDto;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.api.model.param.ReportParam;
import com.rjhc.credit.information.service.api.model.param.StatisticalAnalysisParam;
import com.rjhc.credit.information.service.server.common.convertor.PoorHouseholdsInformationConvertor;
import com.rjhc.credit.information.service.server.common.convertor.StatisticalAnalysisConvertor;
import com.rjhc.credit.information.service.server.common.enums.BizExceptionEnum;
import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;
import com.rjhc.credit.information.service.server.dao.mapper.PoorHouseholdsInformationMapper;
import com.rjhc.credit.information.service.server.dao.mapper.RegionMapper;
import com.rjhc.credit.information.service.server.dao.mapper.StatisticalAnalysisMapper;
import com.rjhc.credit.information.service.server.dao.mapper.WaringPoorMapper;
import com.rjhc.credit.information.service.server.middleware.CountySel;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.middleware.Staaysn;
import com.rjhc.credit.information.service.server.service.StatisticalAnalysisService;
import com.rjhc.matrix.framework.core.exception.bean.BizException;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import com.rjhc.matrix.framework.core.util.RequestUtil;
import com.rjhc.matrix.framework.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName StatisticalAnalysisServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@Service
@Slf4j
public class StatisticalAnalysisServiceImpl implements StatisticalAnalysisService {
    @Resource
    private StatisticalAnalysisMapper statisticalAnalysisMapper;
    @Resource
    private PoorHouseholdsInformationMapper poorHouseholdsInformationMapper;
    @Autowired
    private Staaysn staaysn;
    @Autowired
    private GetXiAnData xiAnData;
    @Autowired
    private CountySel countySel;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private WaringPoorMapper waringPoorMapper;

    /**
     * ???????????????
     *  ???????????????????????????????????????????????????
     *
     * @param statisticalAnalysisParam
     * @Author: grx
     * @Date: 11:36 ?????? 2020/9/21
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<StatisticalAnalysisDto> selectFuzzy(StatisticalAnalysisParam statisticalAnalysisParam) throws Exception {
        Integer currentPageNum = statisticalAnalysisParam.getCurrentPageNum();
        Integer pageSize = statisticalAnalysisParam.getPageSize();
        LambdaQueryWrapper<StatisticalAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        //?????????????????????
        String userIdHeader = requestUtil.getUserIdHeader();
        //???????????????????????????
        Map<String, Object> userandOrg = xiAnData.getUserandOrg(userIdHeader);
        if(userandOrg.get("userType").equals("2")){
            //??????????????????????????????????????????????????????????????????
            Map<String, String> pbcCode = xiAnData.getDeptOrg(userandOrg.get("pbcCode").toString());
            List<String> list = xiAnData.getOrgLevel(pbcCode.get("orgId"), pbcCode.get("orgLvl"));
            //????????????????????????
            queryWrapper.in(StatisticalAnalysis::getUploadOrganization,list);
        }


        //??????????????????????????????
        queryWrapper.orderByDesc(StatisticalAnalysis::getSynchronizationDate);
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getCustomerName())) {
            queryWrapper.eq(StatisticalAnalysis::getCustomerName, statisticalAnalysisParam.getCustomerName());
        }
        // ????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getBankName())) {
            queryWrapper.eq(StatisticalAnalysis::getBankName, statisticalAnalysisParam.getBankName());
        }
        //??????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getBankAddress())) {
            //????????????????????????????????????
            List<String> areaByLevel = xiAnData.getAreaByLevel(statisticalAnalysisParam.getBankAddress(), statisticalAnalysisParam.getLevel());
            queryWrapper.in(StatisticalAnalysis::getBankAddress, areaByLevel);
        }
        //????????????????????????
        if(statisticalAnalysisParam.getStartMaturityDate() != null && statisticalAnalysisParam.getEndMaturityDate() != null){
            queryWrapper.between(StatisticalAnalysis::getMaturityDate, statisticalAnalysisParam.getStartMaturityDate(), statisticalAnalysisParam.getEndMaturityDate());
        }
        if(statisticalAnalysisParam.getStartMaturityDate() != null && statisticalAnalysisParam.getEndMaturityDate() == null){
            queryWrapper.ge(StatisticalAnalysis::getMaturityDate, statisticalAnalysisParam.getStartMaturityDate());
        }
        if(statisticalAnalysisParam.getStartMaturityDate() == null && statisticalAnalysisParam.getEndMaturityDate() != null){
            queryWrapper.le(StatisticalAnalysis::getMaturityDate, statisticalAnalysisParam.getEndMaturityDate());
        }
        //????????????????????????
        if(statisticalAnalysisParam.getStartLoanDate() != null && statisticalAnalysisParam.getEndLoanDate() != null){
            queryWrapper.between(StatisticalAnalysis::getLoanDate, statisticalAnalysisParam.getStartLoanDate(), statisticalAnalysisParam.getEndLoanDate());
        }
        if(statisticalAnalysisParam.getStartLoanDate() != null && statisticalAnalysisParam.getEndLoanDate() == null){
            queryWrapper.ge(StatisticalAnalysis::getLoanDate, statisticalAnalysisParam.getStartLoanDate());
        }
        if(statisticalAnalysisParam.getStartLoanDate() == null && statisticalAnalysisParam.getEndLoanDate() != null){
            queryWrapper.le(StatisticalAnalysis::getLoanDate, statisticalAnalysisParam.getEndLoanDate());
        }
        //????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getCreditLevel())) {
            queryWrapper.eq(StatisticalAnalysis::getCreditLevel, statisticalAnalysisParam.getCreditLevel());
        }
        //????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getLoanQuality())) {
            queryWrapper.eq(StatisticalAnalysis::getLoanQuality, statisticalAnalysisParam.getLoanQuality());
        }
        //????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getGuaranteeMethod())) {
            queryWrapper.eq(StatisticalAnalysis::getGuaranteeMethod, statisticalAnalysisParam.getGuaranteeMethod());
        }
        //??????????????????????????????
        if (statisticalAnalysisParam.getStartLoanAmount() != null && statisticalAnalysisParam.getEndLoanAmount() != null) {
            queryWrapper.between(StatisticalAnalysis::getLoanAmount, statisticalAnalysisParam.getStartLoanAmount(), statisticalAnalysisParam.getEndLoanAmount());
        }
        if (statisticalAnalysisParam.getStartLoanAmount() != null) {
            queryWrapper.ge(StatisticalAnalysis::getLoanAmount, statisticalAnalysisParam.getStartLoanAmount());
        }
        if (statisticalAnalysisParam.getEndLoanAmount() != null) {
            queryWrapper.le(StatisticalAnalysis::getLoanAmount, statisticalAnalysisParam.getEndLoanAmount());
        }
        //??????????????????????????????
        if(statisticalAnalysisParam.getOrgList() != null && statisticalAnalysisParam.getOrgList().size()!= 0){
            queryWrapper.in(StatisticalAnalysis::getUploadOrganization,statisticalAnalysisParam.getOrgList());
        }
        //????????????????????????????????????
        if(!StringUtil.isEmpty(statisticalAnalysisParam.getBankName())){
            queryWrapper.eq(StatisticalAnalysis::getBankName, statisticalAnalysisParam.getBankName());
        }
        //???????????????????????????
        if(!StringUtil.isEmpty(statisticalAnalysisParam.getBankAddressCode())){
            List<String> orgLevel = xiAnData.getAreaByLevel(statisticalAnalysisParam.getBankAddressCode(), statisticalAnalysisParam.getLevel());
            queryWrapper.in(StatisticalAnalysis::getBankAddress,orgLevel);
        }
        //??????????????????
        if(!StringUtil.isEmpty(statisticalAnalysisParam.getDeptOrg())){
            if("????????????????????????????????????????????????".equals(statisticalAnalysisParam.getDeptOrg())){
                HashSet<String> set = new HashSet<>();
                //?????????????????????????????????????????????
                HashSet<String> list1 = xiAnData.getLevelNXS("?????????????????????");
                statisticalAnalysisParam.setDeptList(list1);
            }else {
                HashSet<String> deptOrgBylevelOne = xiAnData.getDeptOrgBylevelsel(statisticalAnalysisParam.getDeptOrg());
                statisticalAnalysisParam.setDeptList(deptOrgBylevelOne);
            }
            if(statisticalAnalysisParam.getDeptList() != null && statisticalAnalysisParam.getDeptList().size() > 0 ){
                queryWrapper.in(StatisticalAnalysis::getBankCode,statisticalAnalysisParam.getDeptList());
            }

        }

        //????????????????????????????????????
        Page<StatisticalAnalysis> statisticalAnalysisPage = statisticalAnalysisMapper.selectPage(new Page<StatisticalAnalysis>(currentPageNum, pageSize), queryWrapper);
        List<StatisticalAnalysis> records = statisticalAnalysisPage.getRecords();
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = StatisticalAnalysisConvertor.INSTANCE.entityListToDtoList(records);
        ArrayList<StatisticalAnalysisDto> list = new ArrayList<>();
        if (statisticalAnalysisPage.getRecords() != null || statisticalAnalysisPage.getRecords().size() != 0) {
            statisticalAnalysisDtos.forEach(statisticalAnalysisDto -> {
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getCreditAccountLogo())) {
                    if ("1".equals(statisticalAnalysisDto.getCreditAccountLogo())) {
                        statisticalAnalysisDto.setCreditAccountLogoName("???");
                    }
                    if ("0".equals(statisticalAnalysisDto.getCreditAccountLogo())) {
                        statisticalAnalysisDto.setCreditAccountLogoName("???");
                    }
                }
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getLoanQuality())) {
                    if ("1".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("2".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("3".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("4".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("5".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                }
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getGuaranteeMethod())) {
                    if ("1".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("2".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("3".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("4".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("5".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("?????????");
                    }
                }
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getDiscountLoanSign())) {
                    if ("1".equals(statisticalAnalysisDto.getDiscountLoanSign())) {
                        statisticalAnalysisDto.setDiscountLoanSignName("???");
                    }
                    if ("0".equals(statisticalAnalysisDto.getDiscountLoanSign())) {
                        statisticalAnalysisDto.setDiscountLoanSignName("???");
                    }
                }
                //??????????????????
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getLoanPurpose())) {
                    if ("1".equals(statisticalAnalysisDto.getLoanPurpose())) {
                        statisticalAnalysisDto.setLoanPurposeName("?????????");
                    }
                    if ("2".equals(statisticalAnalysisDto.getLoanPurpose())) {
                        statisticalAnalysisDto.setLoanPurposeName("?????????");
                    }
                    if ("3".equals(statisticalAnalysisDto.getLoanPurpose())) {
                        statisticalAnalysisDto.setLoanPurposeName("?????????");
                    }
                }
                //????????????
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getLoanVarietie())) {
                    if ("1".equals(statisticalAnalysisDto.getLoanVarietie())) {
                        statisticalAnalysisDto.setLoanVarietieName("??????????????????");
                    }
                    if ("2".equals(statisticalAnalysisDto.getLoanVarietie())) {
                        statisticalAnalysisDto.setLoanVarietieName("????????????");
                    }
                    if ("3".equals(statisticalAnalysisDto.getLoanVarietie())) {
                        statisticalAnalysisDto.setLoanVarietieName("????????????");
                    }
                    if ("4".equals(statisticalAnalysisDto.getLoanVarietie())) {
                        statisticalAnalysisDto.setLoanVarietieName("????????????");
                    }
                }
                list.add(statisticalAnalysisDto);
            });
        }

        Page<StatisticalAnalysisDto> page = new Page<>(currentPageNum, pageSize);
        page.setSize(statisticalAnalysisPage.getSize());
        page.setCurrent(statisticalAnalysisPage.getCurrent());
        page.setRecords(list);
        page.setTotal(statisticalAnalysisPage.getTotal());
        return page;
    }

    /**
     * ???????????????
     *
     *
     * @param statisticalAnalysisParam
     * @Author: grx
     * @Date: 11:37 ?????? 2020/9/21
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto>
     */
    @Override
    public List<StatisticalAnalysisDto> selectAll(StatisticalAnalysisParam statisticalAnalysisParam) {
        LambdaQueryWrapper<StatisticalAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        //??????????????????????????????
        queryWrapper.orderByDesc(StatisticalAnalysis::getSynchronizationDate);
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getCustomerName())) {
            queryWrapper.and(wrapper -> {
                wrapper.or().like(StatisticalAnalysis::getCustomerName, statisticalAnalysisParam.getCustomerName());
            });
        }
        // ????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getBankName())) {
            queryWrapper.and(wrapper -> {
                wrapper.or().like(StatisticalAnalysis::getBankName, statisticalAnalysisParam.getBankName());
            });
        }
        //????????????????????????
        if (statisticalAnalysisParam.getMaturityDate() != null) {
            queryWrapper.le(StatisticalAnalysis::getMaturityDate, statisticalAnalysisParam.getMaturityDate());
        }
        //??????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getBankAddress())) {
            queryWrapper.eq(StatisticalAnalysis::getBankAddress, statisticalAnalysisParam.getBankAddress());
        }
        //????????????????????????
        if (statisticalAnalysisParam.getLoanDate() != null) {
            queryWrapper.le(StatisticalAnalysis::getLoanDate, statisticalAnalysisParam.getLoanDate());
        }
        //????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getCreditLevel())) {
            queryWrapper.and(wrapper -> {
                wrapper.or().like(StatisticalAnalysis::getCreditLevel, statisticalAnalysisParam.getCreditLevel());
            });
        }
        //????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getLoanQuality())) {
            queryWrapper.eq(StatisticalAnalysis::getLoanQuality, statisticalAnalysisParam.getLoanQuality());
        }
        //????????????????????????
        if (!StringUtil.isEmpty(statisticalAnalysisParam.getGuaranteeMethod())) {
            queryWrapper.eq(StatisticalAnalysis::getGuaranteeMethod, statisticalAnalysisParam.getGuaranteeMethod());
        }
        //??????????????????????????????
        if (statisticalAnalysisParam.getStartLoanAmount() != null && statisticalAnalysisParam.getEndLoanAmount() != null) {
            queryWrapper.between(StatisticalAnalysis::getLoanAmount, statisticalAnalysisParam.getStartLoanAmount(), statisticalAnalysisParam.getEndLoanAmount());
        }
        if (statisticalAnalysisParam.getStartLoanAmount() != null) {
            queryWrapper.ge(StatisticalAnalysis::getLoanAmount, statisticalAnalysisParam.getStartLoanAmount());
        }
        if (statisticalAnalysisParam.getEndLoanAmount() != null) {
            queryWrapper.le(StatisticalAnalysis::getLoanAmount, statisticalAnalysisParam.getEndLoanAmount());
        }
        List<StatisticalAnalysis> statisticalAnalyses = statisticalAnalysisMapper.selectList(queryWrapper);
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = StatisticalAnalysisConvertor.INSTANCE.entityListToDtoList(statisticalAnalyses);
        ArrayList<StatisticalAnalysisDto> list = new ArrayList<>();
        if (statisticalAnalyses != null || statisticalAnalyses.size() != 0) {
            statisticalAnalysisDtos.forEach(statisticalAnalysisDto -> {
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getCreditAccountLogo())) {
                    if ("1".equals(statisticalAnalysisDto.getCreditAccountLogo())) {
                        statisticalAnalysisDto.setCreditAccountLogoName("???");
                    }
                    if ("0".equals(statisticalAnalysisDto.getCreditAccountLogo())) {
                        statisticalAnalysisDto.setCreditAccountLogoName("???");
                    }
                }
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getLoanQuality())) {
                    if ("1".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("2".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("3".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("4".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                    if ("5".equals(statisticalAnalysisDto.getLoanQuality())) {
                        statisticalAnalysisDto.setLoanQualityName("??????");
                    }
                }
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getGuaranteeMethod())) {
                    if ("1".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("2".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("3".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("4".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("??????");
                    }
                    if ("5".equals(statisticalAnalysisDto.getGuaranteeMethod())) {
                        statisticalAnalysisDto.setGuaranteeMethodName("?????????");
                    }
                }
                if (!StringUtil.isEmpty(statisticalAnalysisDto.getDiscountLoanSign())) {
                    if ("1".equals(statisticalAnalysisDto.getDiscountLoanSign())) {
                        statisticalAnalysisDto.setDiscountLoanSignName("???");
                    }
                    if ("0".equals(statisticalAnalysisDto.getDiscountLoanSign())) {
                        statisticalAnalysisDto.setDiscountLoanSignName("???");
                    }
                }
                list.add(statisticalAnalysisDto);
            });
        }
        return list;
    }
    @Value("${spring.datasource.druid.url}")
    private String DB_URL;
    @Value("${spring.datasource.druid.username}")
    private String USER;
    @Value("${spring.datasource.druid.password}")
    private String PASSWORD;
    @Override
    public List<StatisticalAnalysisDto> dataStatistics(List<StatisticalAnalysisDto> list) throws SQLException {
        /*try {*/

        ArrayList<PoorHouseholdsInformationDto> errorList = new ArrayList<>();

        AtomicInteger count = new AtomicInteger(1);
        PreparedStatement pstmt = null;
        Connection conn = null;
//        try{
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        boolean closed = conn.isClosed();
        conn.setAutoCommit(false);

        pstmt = conn.prepareStatement("INSERT INTO agricultural_organizations(id,upload_organization,years,month,bank_code,bank_name,bank_address_code,bank_address,poor_code,customer_code" +
                ",customer_name,customer_id_card,credit_account_logo,credit_level,contract_code,receipt_code,contracta_mount,loan_amount,loan_balance,lendingrate,loan_date,maturity_date,loan_purpose" +
                ",loan_varietie,loan_quality,guarantee_method,discount_loan_sign,discount_ratio,synchronization_date,type) VALUES(?,?,?,?, ?,?,?,?, ?,?,?,?, ?,?,?,?,?, ?,?,?,?, ?,?,?,?, ?,?,?,?,?)");
        long start = System.currentTimeMillis();
        for (StatisticalAnalysisDto statisticalAnalysisDto : list) {
            try {
                pstmt.setString(1, RandomUtil.UUID36());
                pstmt.setString(2, statisticalAnalysisDto.getUploadOrganization());
                pstmt.setString(3, statisticalAnalysisDto.getYears());
                pstmt.setString(4, statisticalAnalysisDto.getMonth());
                pstmt.setString(5, statisticalAnalysisDto.getBankCode());
                pstmt.setString(6, statisticalAnalysisDto.getBankName());
                pstmt.setString(7, statisticalAnalysisDto.getBankAddressCode());
                pstmt.setString(8, statisticalAnalysisDto.getBankAddress());
                pstmt.setString(9, statisticalAnalysisDto.getPoorCode());
                pstmt.setString(10, statisticalAnalysisDto.getCustomerCode());
                pstmt.setString(11, statisticalAnalysisDto.getCustomerName());
                pstmt.setString(12, statisticalAnalysisDto.getCustomerIdCard());
                pstmt.setString(13, statisticalAnalysisDto.getCreditAccountLogo());
                pstmt.setString(14, statisticalAnalysisDto.getCreditLevel());
                pstmt.setString(15, statisticalAnalysisDto.getContractCode());
                pstmt.setString(16, statisticalAnalysisDto.getReceiptCode());
                pstmt.setBigDecimal(17, statisticalAnalysisDto.getContractaMount());
                pstmt.setBigDecimal(18, statisticalAnalysisDto.getLoanAmount());
                pstmt.setBigDecimal(19, statisticalAnalysisDto.getLoanBalance());
                pstmt.setBigDecimal(20, statisticalAnalysisDto.getLendingrate());
                pstmt.setDate(21, new java.sql.Date(statisticalAnalysisDto.getLoanDate().getTime()));
                pstmt.setDate(22, new java.sql.Date(statisticalAnalysisDto.getMaturityDate().getTime()));
                pstmt.setString(23, statisticalAnalysisDto.getLoanPurpose());
                pstmt.setString(24, statisticalAnalysisDto.getLoanVarietie());
                pstmt.setString(25, statisticalAnalysisDto.getLoanQuality());
                pstmt.setString(26, statisticalAnalysisDto.getGuaranteeMethod());
                pstmt.setString(27, statisticalAnalysisDto.getDiscountLoanSign());
                pstmt.setBigDecimal(28, statisticalAnalysisDto.getDiscountRatio());
                pstmt.setTimestamp(29,  new Timestamp(new java.util.Date().getTime()));
                pstmt.setString(30,  statisticalAnalysisDto.getType());
                pstmt.addBatch();
                if (count.get() % 10000 == 0) {// ????????????10000??????????????????????????????
                    pstmt.executeBatch();// ???????????????
                    conn.commit();
                    pstmt.clearBatch();
                }
                count.getAndIncrement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        pstmt.executeBatch();// ???????????????
        conn.commit();
        pstmt.close();
        long end = System.currentTimeMillis();
        long l = TimeUnit.MICROSECONDS.toMinutes(end - start);
        System.out.println("??????????????????????????????"+l+" ???");
        return list;
    }

    @Override
    public void errorPoor(List<StatisticalAnalysisDto> list,int errorI) throws Exception {
        log.info("?????????????????????????????????");
        long start = System.currentTimeMillis();
        ArrayList<PoorHouseholdsInformationDto> listPoor = new ArrayList<>();
        ArrayList<StatisticalAnalysisDto> listPoor1 = new ArrayList<>();
        ArrayList<String> listPoor2 = new ArrayList<>();
        listPoor1.addAll(list);
        int i = 1;
        int erri=0;
        if(listPoor1.size() != 0 && listPoor1 != null){
            for (StatisticalAnalysisDto statisticalAnalysisDto : listPoor1) {
                if(statisticalAnalysisDto != null){
                    listPoor2.add(statisticalAnalysisDto.getCustomerIdCard());
                }else {
                    erri++;
                    continue;
                }

                if(i % 1000 == 0){
                    List<PoorHouseholdsInformation> poorHouseholdsInformations = poorHouseholdsInformationMapper.seleAllByIdCard(listPoor2);
                    List<PoorHouseholdsInformationDto> poorHouseholdsInformationDtos = PoorHouseholdsInformationConvertor.INSTANCE.entityListToDtoList(poorHouseholdsInformations);
                    listPoor.addAll(poorHouseholdsInformationDtos);
                    listPoor2.clear();
                }
                i++;
            }
            if(listPoor2 != null && listPoor2.size() != 0){
                List<PoorHouseholdsInformation> poorHouseholdsInformations = poorHouseholdsInformationMapper.seleAllByIdCard(listPoor2);
                List<PoorHouseholdsInformationDto> poorHouseholdsInformationDtos = PoorHouseholdsInformationConvertor.INSTANCE.entityListToDtoList(poorHouseholdsInformations);
                listPoor.addAll(poorHouseholdsInformationDtos);
            }
            if(listPoor !=null && listPoor.size() != 0){
                log.error("????????????????????????"+errorI);
                ArrayList<PoorHouseholdsInformationDto> collect = listPoor.stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PoorHouseholdsInformationDto::getId))), ArrayList::new));
                //?????????????????????
                log.error("?????????????????????"+listPoor1.size());
                insWooringPoor(collect,listPoor1.get(0).getYears(),listPoor1.get(0).getMonth(),listPoor1.get(0).getBankCode(),listPoor1.get(0).getUploadOrganization());
            }
            long end = System.currentTimeMillis();
            log.info("??????????????????????????????????????????"+(end-start));
        }
        log.error("???????????????????????????"+erri+"???");
    }

    @Override
    public List<StatisticalAnalysisDto> seleByIdCard(String idCard) {
        LambdaQueryWrapper<StatisticalAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        //??????????????????????????????
        queryWrapper.orderByDesc(StatisticalAnalysis::getSynchronizationDate);
        queryWrapper.eq(StatisticalAnalysis::getCustomerIdCard, idCard);
        List<StatisticalAnalysis> statisticalAnalyses = statisticalAnalysisMapper.selectList(queryWrapper);
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = StatisticalAnalysisConvertor.INSTANCE.entityListToDtoList(statisticalAnalyses);
        return statisticalAnalysisDtos;
    }

    @Override
    public List<Map<String,String>> queryReportStatistics(ReportParam reportParam) throws Exception {
        List<Map<String, String>> stringStringMap = new ArrayList<>();
        //?????????????????????
        String userIdHeader = requestUtil.getUserIdHeader();
        //???????????????????????????
        Map<String, Object> userandOrg = xiAnData.getUserandOrg(userIdHeader);
        if(userandOrg.get("userType").equals("2")){
            //??????????????????????????????????????????????????????????????????
            Map<String, String> pbcCode = xiAnData.getDeptOrg(userandOrg.get("pbcCode").toString());
            List<String> list = xiAnData.getOrgLevel(pbcCode.get("orgId"), pbcCode.get("orgLvl"));
            reportParam.setUploadList(list);
        }
        String tableName = null;
        //??????????????????
        Calendar date = Calendar.getInstance();
        String s = String.valueOf(date.get(Calendar.YEAR));
        if(reportParam.getYears().equals(s)){
            reportParam.setTableName("agricultural_organizations");
        }else {
            reportParam.setTableName("agricultural_organizations_"+reportParam.getYears());
        }
        log.info("????????????"+reportParam.getTableName());
        List<String> regionDtos = new ArrayList<>();
        if(!StringUtil.isEmpty(reportParam.getRegionId())){
            //??????????????????????????????
//            regionDtos = regionMapper.selectByParentId(reportParam.getRegionId());
            List<String> orgLevel = xiAnData.getAreaByLevel(reportParam.getRegionId(), reportParam.getLevel());
            reportParam.setRegionDtos(orgLevel);
        }
        List<Map<String, String>> maps = new ArrayList<>();

        try {
            if(reportParam.getType().equals("bank_address_code")){
                reportParam.setOrderLike(reportParam.getQueryLike()+" DESC");
                reportParam.setBankAddress("bank_address");
            }
            if(reportParam.getType().equals("bank_address_code")){
                maps = statisticalAnalysisMapper.queryReportStatistics(reportParam);
            }else {
                maps = statisticalAnalysisMapper.queryReportStatisticsBymonth(reportParam);
            }

            if(maps != null && maps.size() != 0){
                if(reportParam.getType().equals("bank_address_code")){
                    for (Map<String, String> stringMap : maps) {
                        if(!StringUtil.isEmpty(stringMap.get("bank_address"))){
                            stringMap.put("bankAddress",stringMap.get("bank_address"));
                            stringStringMap.add(stringMap);
                        }
                    }
                }else
                //????????????
                if(reportParam.getType().equals("loan_quality")){
                    for (Map<String, String> stringMap : maps) {
                        if(stringMap.get("loan_quality").equals("1")){
                            stringMap.put("loanQualityName","??????");
                        }
                        if(stringMap.get("loan_quality").equals("2")){
                            stringMap.put("loanQualityName","??????");
                        }
                        if(stringMap.get("loan_quality").equals("3")){
                            stringMap.put("loanQualityName","??????");
                        }
                        if(stringMap.get("loan_quality").equals("4")){
                            stringMap.put("loanQualityName","??????");
                        }
                        if(stringMap.get("loan_quality").equals("5")){
                            stringMap.put("loanQualityName","??????");
                        }
                        stringStringMap.add(stringMap);
                    }

                } else
                //????????????
                if(reportParam.getType().equals("guarantee_method")){
                    for (Map<String, String> stringMap : maps) {
                        if(stringMap.get("guarantee_method").equals("1")){
                            stringMap.put("guaranteeMethodName","??????");
                        }
                        if(stringMap.get("guarantee_method").equals("2")){
                            stringMap.put("guaranteeMethodName","??????");
                        }
                        if(stringMap.get("guarantee_method").equals("3")){
                            stringMap.put("guaranteeMethodName","??????");
                        }
                        if(stringMap.get("guarantee_method").equals("4")){
                            stringMap.put("guaranteeMethodName","??????");
                        }
                        if(stringMap.get("guarantee_method").equals("5")){
                            stringMap.put("guaranteeMethodName","?????????");
                        }
                        stringStringMap.add(stringMap);
                    }
                } else {
                    for (Map<String, String> stringMap : maps) {
                        stringStringMap.add(stringMap);
                    }
                }
                return stringStringMap;
            }
        }catch (Exception e){
            log.error(tableName+"??????????????????");
        }
        return maps;
    }

    @Override
    public List<Map<String, String>> selByLevel() {
        String tableName = "agricultural_organizations";
        return statisticalAnalysisMapper.selByLevel(tableName);
    }

    @Override
    public List<Map<String, String>> selstaAndPoorByBankAddress(String bankAddressCode, String tableName, String queryLike) {
        List<Map<String, String>> stringStringMap = new ArrayList<>();
        //??????????????????
        Calendar date = Calendar.getInstance();
        String s = String.valueOf(date.get(Calendar.YEAR));
        if(tableName.equals(s)){
            tableName="agricultural_organizations";
        }else {
            tableName="agricultural_organizations_"+tableName;
        }
        List<Map<String, String>> maps = new ArrayList<>();
        try {
            if(queryLike.equals("bank_address_code")){
                queryLike="bank_address";
            }
            maps = statisticalAnalysisMapper.selstaAndPoorByBankAddress(queryLike, tableName, queryLike);
        }catch (Exception e){
            log.error(tableName+"??????????????????");
        }
        return maps;
    }

    @Override
    public List<Map<String, String>> selStaCountByLevel(String years) {
        Calendar date = Calendar.getInstance();
        String s = String.valueOf(date.get(Calendar.YEAR));
        if(years.equals(s)){
            years="agricultural_organizations";
        }else {
            years="agricultural_organizations_"+years;
        }
        return statisticalAnalysisMapper.selByLevel(years);
    }

    @Override
    public Long selectByDate(StatisticalAnalysisParam statisticalAnalysisParam) {
        LambdaQueryWrapper<StatisticalAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        //????????????????????????
        if(statisticalAnalysisParam.getStartMaturityDate() != null && statisticalAnalysisParam.getEndMaturityDate() != null){
            queryWrapper.between(StatisticalAnalysis::getMaturityDate, statisticalAnalysisParam.getStartMaturityDate(), statisticalAnalysisParam.getEndMaturityDate());
        }
        if(statisticalAnalysisParam.getStartMaturityDate() != null && statisticalAnalysisParam.getEndMaturityDate() == null){
            queryWrapper.ge(StatisticalAnalysis::getMaturityDate, statisticalAnalysisParam.getStartMaturityDate());
        }
        if(statisticalAnalysisParam.getStartMaturityDate() == null && statisticalAnalysisParam.getEndMaturityDate() != null){
            queryWrapper.le(StatisticalAnalysis::getMaturityDate, statisticalAnalysisParam.getEndMaturityDate());
        }
        Integer integer = statisticalAnalysisMapper.selectCount(queryWrapper);
        return integer.longValue();
    }

    @Override
    public List<CountyDto> slecounty(CountyParam countyParam) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        List<String> strings = setCalender(countyParam);
        String tableName="agricultural_organizations";
        List<CountyDto> maps1 = new ArrayList<>();
        List<CountyDto> returnList = new ArrayList<>();
        if(!StringUtil.isEmpty(countyParam.getCounty())){
            //??????????????????????????????????????????????????????
            List<String> areaByLevel = xiAnData.getAreaByLevel(countyParam.getCounty(), countyParam.getLevel());
            countyParam.setCountyList(areaByLevel);

        }
        if(!StringUtil.isEmpty(countyParam.getDeptOrg())){
            if("????????????????????????????????????????????????".equals(countyParam.getDeptOrg())){
                HashSet<String> set = new HashSet<>();
                //?????????????????????????????????????????????
                Future<HashSet<String>> deptOrgBylevelOne1 = xiAnData.getLevelNXSAsync("?????????????????????");
                countyParam.setDeptList(deptOrgBylevelOne1.get());
            }else {
                Future<HashSet<String>> deptOrgBylevelOne = xiAnData.getDeptOrgBylevelOne(countyParam.getDeptOrg());
                HashSet<String> strings1 = deptOrgBylevelOne.get();
                countyParam.setDeptList(strings1);
            }

        }
        if(strings != null && strings.size() >0){
            List<CountyDto> sumList = new ArrayList<>();
            //????????????????????????
            for (String string : strings) {
                if(dateFormat.format(new java.util.Date()).equals(string)){
                    countyParam.setTableName(tableName);
                }else {
                    countyParam.setTableName(tableName+"_"+string);
                }
               /* List<CountyDto> countyDtos = slecountyYear(countyParam);*/
                Future<List<CountyDto>> listFuture = countySel.selCountry(countyParam);
                List<CountyDto> countyDtos = listFuture.get();
                if(countyDtos != null && countyDtos.size() >0){
                    sumList.addAll(countyDtos);
                }

            }
            Map<String, CountyDto> map = new HashMap<>();
            for (CountyDto countyDto : sumList) {
                if(map.containsKey(countyDto.getCounty())){
                    CountyDto countyDto1 = map.get(countyDto.getCounty());
                    countyDto1.setCreditNo(countyDto1.getCreditNo()+countyDto.getCreditNo());
                    countyDto1.setLoanNo(countyDto1.getLoanNo()+countyDto.getLoanNo());
                    countyDto1.setLoanAmount(countyDto1.getLoanAmount().add(countyDto.getLoanAmount()));
                    map.put(countyDto.getCounty(),countyDto1);
                }else {
                    map.put(countyDto.getCounty(),countyDto);
                }
            }
            for (Map.Entry<String, CountyDto> entry : map.entrySet()) {
                maps1.add(entry.getValue());
            }
            Stream<CountyDto> sorted = maps1.stream().sorted(Comparator.comparing(CountyDto::getPoorNo, Comparator.reverseOrder()).thenComparing(CountyDto::getCreditNo, Comparator.reverseOrder())
                    .thenComparing(CountyDto::getLoanAmount, Comparator.reverseOrder()).thenComparing(CountyDto::getLoanNo, Comparator.reverseOrder())
            );
             maps1 = sorted.collect(Collectors.toList());
        }else {
            countyParam.setTableName(tableName);
            Future<List<CountyDto>> listFuture = countySel.selCountry(countyParam);
            if(listFuture.get() != null && listFuture.get().size() >0){
                maps1.addAll(listFuture.get());
            }
        }


//        Thread.sleep(100000);
        return maps1;
    }

    @Override
    public void delByType(String type) {
        statisticalAnalysisMapper.delByType(type);
    }

    @Override
    public void updateByType(String newType, String oldType) {
        LambdaQueryWrapper<StatisticalAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StatisticalAnalysis::getType, oldType);
        StatisticalAnalysis statisticalAnalysis = new StatisticalAnalysis();
        statisticalAnalysis.setType(newType);
        statisticalAnalysisMapper.update(statisticalAnalysis,queryWrapper);
//        statisticalAnalysisMapper.updateByType(newType,oldType);
    }

    @Override
    public void delByUploadBankAndMonth(String uploadBank, String month) {
        //????????????????????????????????????????????????
        LambdaQueryWrapper<StatisticalAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StatisticalAnalysis::getUploadOrganization,uploadBank);
        queryWrapper.eq(StatisticalAnalysis::getMonth,month);
//        queryWrapper.eq(StatisticalAnalysis::getYears,statisticalAnalysisDto.getYears());
        statisticalAnalysisMapper.delete(queryWrapper);
        //????????????????????????????????????
        //????????????????????????????????????????????????
        LambdaQueryWrapper<WaringPoor> queryWrapperWaring = new LambdaQueryWrapper<>();
        queryWrapperWaring.eq(WaringPoor::getType,uploadBank);
        queryWrapperWaring.eq(WaringPoor::getMonth,month);
//        queryWrapperWaring.eq(WaringPoor::getYears,years);
        waringPoorMapper.delete(queryWrapperWaring);
    }


    /**
     *  ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    public void insWooringPoor(List<PoorHouseholdsInformationDto> list,String years,String month,String bankCode,String type) throws Exception {
        String sql="INSERT INTO `waring_poor_households_information`(`id`, `years`, `month`,`bank_code`,`prefecture`, `counties`, `township`, `administrative_village`, `administrative_village_code`, `householdername`, `id_card`, `address`, `poor_households_code`, `population_code`, `poor_households_attribute`, `id_card_type`, `health`, `povertysign`, `familysize`, `relationship`, `gender`, `age`, `nation`, `landarea`, `woodlandarea`, `income`, `synchronization_date`, `type`) VALUES (?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        AtomicInteger count = new AtomicInteger(1);
        PreparedStatement pstmt = null;
        Connection conn = null;
//        try{
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        conn.setAutoCommit(false);
        pstmt = conn.prepareStatement(sql);
        for (PoorHouseholdsInformationDto poorHouseholdsInformationDto : list) {

            try {
                pstmt.setString(1, RandomUtil.UUID36());
                pstmt.setString(2, years);
                pstmt.setString(3, month);
                pstmt.setString(4, bankCode);
                pstmt.setString(5, poorHouseholdsInformationDto.getPrefecture());
                pstmt.setString(6, poorHouseholdsInformationDto.getCounties());
                pstmt.setString(7, poorHouseholdsInformationDto.getTownship());
                pstmt.setString(8, poorHouseholdsInformationDto.getAdministrativeVillage());
                pstmt.setString(9, poorHouseholdsInformationDto.getAdministrativeVillageCode());
                pstmt.setString(10, poorHouseholdsInformationDto.getHouseholderName());
                pstmt.setString(11, poorHouseholdsInformationDto.getIdCard());
                pstmt.setString(12, poorHouseholdsInformationDto.getAddress());
                pstmt.setString(13, poorHouseholdsInformationDto.getPoorHouseholdsCode());
                pstmt.setString(14, poorHouseholdsInformationDto.getPopulationCode());
                pstmt.setString(15, poorHouseholdsInformationDto.getPoorHouseholdsAttribute());
                pstmt.setString(16, poorHouseholdsInformationDto.getIdCardType());
                pstmt.setString(17, poorHouseholdsInformationDto.getHealth());
                pstmt.setString(18, poorHouseholdsInformationDto.getPovertySign());
                pstmt.setInt(19, poorHouseholdsInformationDto.getFamilySize());
                pstmt.setString(20, poorHouseholdsInformationDto.getRelationShip());
                pstmt.setString(21, poorHouseholdsInformationDto.getGender());
                pstmt.setInt(22, poorHouseholdsInformationDto.getAge());
                pstmt.setString(23, poorHouseholdsInformationDto.getNation());
                pstmt.setBigDecimal(24, poorHouseholdsInformationDto.getLandarea());
                pstmt.setBigDecimal(25, poorHouseholdsInformationDto.getWoodlandarea());
                pstmt.setBigDecimal(26, poorHouseholdsInformationDto.getIncome());
                pstmt.setDate(27, new java.sql.Date(new java.util.Date().getTime()));
                pstmt.setString(28,type);
                pstmt.addBatch();
                if (count.get() % 10000 == 0) {// ????????????10000??????????????????????????????
                    pstmt.executeBatch();// ???????????????
                    conn.commit();
                    pstmt.clearBatch();
                }
                count.getAndIncrement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        pstmt.executeBatch();// ???????????????
        conn.commit();
        pstmt.close();
    }


    public List<String> setCalender(CountyParam countyParam){
        List<String> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        if(countyParam.getStartDate() != null && countyParam.getEndDate() != null){
        if(!dateFormat.format(countyParam.getStartDate()).equals(dateFormat.format(countyParam.getEndDate()))){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(countyParam.getStartDate());
                while (calendar.getTime().before(countyParam.getEndDate())){
                    list.add(dateFormat.format(countyParam.getStartDate()));
                    calendar.add(Calendar.YEAR,1);
                    log.info(dateFormat.format(calendar.getTime()));
                    list.add(dateFormat.format(calendar.getTime()));
                }
            }else {
            list.add(dateFormat.format(countyParam.getStartDate()));
        }
        }

        List<String> collect = list.stream().distinct().collect(Collectors.toList());
        return collect;

    }

    public List<CountyDto> slecountyYear(CountyParam countyParam){
        List<CountyDto> slecounty = new ArrayList<>();

       try {
        slecounty = statisticalAnalysisMapper.slecounty(countyParam);
        }catch (Exception e){
            log.error(countyParam.getTableName()+"??????????????????????????????");
        }
        return slecounty;

    }









}
