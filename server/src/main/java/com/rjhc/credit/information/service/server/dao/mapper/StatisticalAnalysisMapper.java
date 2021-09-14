package com.rjhc.credit.information.service.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjhc.credit.information.service.api.model.dto.CountyDto;
import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.api.model.param.CountyParam;
import com.rjhc.credit.information.service.api.model.param.ReportParam;
import com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis;
import org.apache.ibatis.annotations.Param;
import org.apache.xmlbeans.impl.xb.xmlconfig.Qnametargetlist;

import java.util.List;
import java.util.Map;

/**
 * @ClassName StatisticalAnalysisMapper
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
public interface StatisticalAnalysisMapper extends BaseMapper<StatisticalAnalysis> {
    /**
     * 功能描述：
     * 〈根据借据编号插叙重复数据〉
     * @Author: grx
     * @Date: 6:08 下午 2020/8/14
     * @param receiptCode
     * @return: java.lang.Long
     */
    StatisticalAnalysis selectReceiptCodeExist(String receiptCode);

    /**
     * 功能描述：
     * 〈按照地区统计〉
     * @Author: grx
     * @Date: 11:26 2020/11/20
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String,String>> queryReportStatistics(ReportParam reportParam);
    List<Map<String,String>> queryReportStatisticsBymonth(ReportParam reportParam);

    List<StatisticalAnalysis> distinctAddress(@Param("tableName")String tableName,@Param("bankAddressCode")String bankAddressCode);

    Map<String,Object > countAll(@Param("tableName")String tableName);

    /**
     * 功能描述：
     * 〈分组查询当前等级数量，排除为空的等级〉
     * @Author: grx
     * @Date: 11:26 上午 2020/10/19
     * @param
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String,String>> selByLevel(@Param("tableName")String tableName);

    /**
     * 功能描述：
     * 〈根据地区分组查询当前地区信贷总数，信贷总额，信贷余额，贫苦户数量以及贫困户人均收入〉
     * @Author: grx
     * @Date: 1:39 下午 2020/10/19
     * @param bankAddress
     * @param tableName
     * @param queryLike
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String,String>> selstaAndPoorByBankAddress(@Param("bankAddress")String bankAddress,@Param("tableName")String tableName,@Param("queryLike")String queryLike );

    /**
     * 功能描述：
     * 〈根据上报人奇偶分组查询当前等级不为空的信贷数量〉
     * @Author: grx
     * @Date: 2:19 下午 2020/10/19
     * @param
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String,String>> selStaCountByLevel();


    List<CountyDto> slecounty(CountyParam countyParam);
    List<CountyDto> selecountyByPoorNoTest(CountyParam countyParam);

    void delByType(@Param("type")String type);
    void updateByType(@Param("newType")String newType,@Param("oldType")String oldType);
    long selCount(String tableName);

}
