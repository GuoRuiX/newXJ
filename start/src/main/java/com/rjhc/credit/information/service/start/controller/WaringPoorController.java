package com.rjhc.credit.information.service.start.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.WaringPoorInterface;
import com.rjhc.credit.information.service.api.model.param.WaringPoorParam;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.WaringPoorService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WaringPoorController
 * @Description: TODO
 * @Author grx
 * @Date 2020/9/21
 * @Version V1.0
 **/
@RestController
@Slf4j
public class WaringPoorController implements WaringPoorInterface {
    @Autowired
    private WaringPoorService waringPoorService;
    @Override
    @OptionalLog(module = "预警信息",operations = "查看")
    public RestfulApiResponse<Page<WaringPoorParam>> queryAuditRecords(WaringPoorParam waringPoorParam) throws Exception {
        Page<WaringPoor> waringPoorPage = waringPoorService.selPage(waringPoorParam);
        return RestfulApiResponse.success(waringPoorPage);
    }
}
