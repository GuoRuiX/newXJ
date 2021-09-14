package com.rjhc.credit.information.service.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PoorHouseholdsInformationMapper
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
public interface PoorHouseholdsInformationMapper extends BaseMapper<PoorHouseholdsInformation> {

    List<PoorHouseholdsInformation> seleAllByIdCard(@Param("idCards") List<String> idCards);

    /**
     * 功能描述：
     * 〈根据当前信贷等级查询当前贫苦户信息数量〉
     * @Author: grx
     * @Date: 10:01 上午 2020/10/19
     * @param creditLevels
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String,String> selePoorByIdCard(@Param("creditLevels") List<String> creditLevels);
    void savePoor(List<PoorHouseholdsInformation> poorList);
}
