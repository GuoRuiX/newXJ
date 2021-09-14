package com.rjhc.credit.information.service.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjhc.credit.information.service.server.dao.dataobject.AnnualStatistics;

import java.util.List;

/**
 * @ClassName AnnualStatisticsMapper
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/10
 * @Version V1.0
 **/
public interface AnnualStatisticsMapper extends BaseMapper<AnnualStatistics> {
    /**
     * 功能描述：
     * 〈获取年份〉
     * @Author: grx
     * @Date: 4:33 下午 2020/10/10
     * @param
     * @return: java.util.List<java.lang.String>
     */
    List<String> selYear();
}
