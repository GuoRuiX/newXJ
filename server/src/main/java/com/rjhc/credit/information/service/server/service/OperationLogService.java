package com.rjhc.credit.information.service.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.OperationLogDto;
import com.rjhc.credit.information.service.api.model.param.OperationLogParam;
import com.rjhc.credit.information.service.server.dao.dataobject.OperationLog;

import java.util.List;

/**
 * @ClassName OperationLogService
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/25
 * @Version V1.0
 **/
public interface OperationLogService {
    /**
     * 功能描述：
     * 〈日志列表展示和模糊查询〉
     * @Author: grx
     * @Date: 3:11 下午 2020/8/25
     * @param operationLogParam
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.api.model.dto.OperationLogDto>
     */
    Page<OperationLog> selectLogsByPage(OperationLogParam operationLogParam);
    /**
     * 功能描述：
     * 〈数据导出不带分页〉
     * @Author: grx
     * @Date: 5:50 下午 2020/8/26
     * @param operationLogParam
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.OperationLogDto>
     */
    List<OperationLogDto> selectLogs(OperationLogParam operationLogParam);

    /**
     * 功能描述：
     * 〈根据证件号码和操作名称查询当前日志，和当前登陆人机构〉
     * @Author: grx
     * @Date: 10:05 上午 2020/8/28
     * @param idCard
     * @param opName
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.OperationLogDto>
     */
    List<OperationLogDto> selByIDCardAndOpName(String idCard,String opName,String bankId) throws Exception;




    void testData();

}
