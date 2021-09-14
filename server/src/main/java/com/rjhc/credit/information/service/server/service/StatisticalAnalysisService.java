package com.rjhc.credit.information.service.server.service;

import com.rjhc.credit.information.service.api.model.dto.CountyDto;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.api.model.param.ReportParam;
import com.rjhc.credit.information.service.api.model.param.StatisticalAnalysisParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName StatisticalAnalysisService
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
public interface StatisticalAnalysisService {
    /**
     * 功能描述：
     * 〈分页模糊查询数据〉
     * @Author: grx
     * @Date: 2:32 下午 2020/8/14
     * @param statisticalAnalysisParam
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis>
     */
    Page<StatisticalAnalysisDto> selectFuzzy(StatisticalAnalysisParam statisticalAnalysisParam) throws Exception;
    /**
     * 功能描述：
     * 〈不分页模糊查询数据〉
     * @Author: grx
     * @Date: 2:32 下午 2020/8/14
     * @param statisticalAnalysisParam
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto>
     */
    List<StatisticalAnalysisDto> selectAll(StatisticalAnalysisParam statisticalAnalysisParam);
    /**
     * 功能描述：
     * 〈数据统计功能〉
     * @Author: grx
     * @Date: 4:48 下午 2020/8/14
     * @param list
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto>
     */
    List<StatisticalAnalysisDto> dataStatistics(List<StatisticalAnalysisDto> list) throws SQLException;
    /**
     * 功能描述：
     * 〈处理预警信息〉
     * @Author: grx
     * @Date: 2:51 下午 2020/10/13
     * @param list
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto>
     */
    public void errorPoor(List<StatisticalAnalysisDto> list,int errorI) throws Exception;

    /**
     * 功能描述：
     * 〈根据证件号码查询借贷信息〉
     * @Author: grx
     * @Date: 10:58 上午 2020/8/25
     * @param idCard
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto>
     */
    List<StatisticalAnalysisDto> seleByIdCard(String idCard);

    /**
     * 功能描述：
     * 〈数据统计信息〉
     * @Author: grx
     * @Date: 1:12 下午 2020/10/10
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    List<Map<String,String>> queryReportStatistics(ReportParam reportParam) throws Exception;
    /**
     * 功能描述：
     * 〈分组查询当前等级数量，排除为空等级的数据〉
     * @Author: grx
     * @Date: 11:27 上午 2020/10/19
     * @param
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String,String>> selByLevel();
    /**
     * 功能描述：
     * 〈根据地区分组查询当前地区信贷总数，信贷总额，信贷余额，贫苦户数量以及贫困户人均收入〉
     * @Author: grx
     * @Date: 1:39 下午 2020/10/19
     * @param bankAddressCode 分组条件
     * @param tableName 表名
     * @param queryLike 分组条件
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String,String>> selstaAndPoorByBankAddress(String bankAddressCode,String tableName,String queryLike );


    /**
     * 功能描述：
     * 〈根据上报人奇偶分组查询当前等级不为空的信贷数量〉
     * @Author: grx
     * @Date: 2:19 下午 2020/10/19
     * @param
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String,String>> selStaCountByLevel(String years);




    Long selectByDate(StatisticalAnalysisParam statisticalAnalysisParam);

    /**
     * 功能描述：
     * 〈根据深度县信息获取当前贫困县名称、贫困县层级、贫困户数量、信用户数量、信贷笔数〉
     * @Author: grx
     * @Date: 3:11 下午 2020/10/26
     * @param
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<CountyDto> slecounty(CountyParam countyParam) throws Exception;
    void delByType(String type);
    void updateByType(String newType,String oldType);
    void delByUploadBankAndMonth(String uploadBank,String month);







}
