package com.rjhc.credit.information.service.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PoorHouseholdsInformationService
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
public interface PoorHouseholdsInformationService {
    /**
     * 功能描述：
     * 〈模糊查询贫困户数据〉
     * @Author: grx
     * @Date: 9:54 上午 2020/8/14
     * @param poorHouseholdsInformationParam
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto>
     */
    Page<PoorHouseholdsInformation> selectPage(PoorHouseholdsInformationParam poorHouseholdsInformationParam);

    List<PoorHouseholdsInformationDto> slectAll(PoorHouseholdsInformationParam poorHouseholdsInformationParam);

    PoorHouseholdsInformationDto seleById(String id);
    PoorHouseholdsInformationDto selectPoor(String idcard,String name);
    /**
     * 功能描述：
     * 〈根据当前信贷等级查询当前贫苦户数量〉
     * @Author: grx
     * @Date: 10:03 上午 2020/10/19
     * @param level
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String,String> selePoorByIdCard(List<String> level);

    /**
     * 功能描述：
     * 〈根据当前用户ID查询修改当前贫困户授权书〉
     * @Author: grx
     * @Date: 10:33 上午 2020/10/19
     * @param idCard
     * @param imageBase64
     * @return: void
     */
    void updateImageByIdCard(String idCard,Object imageBase64);
}
