package com.rjhc.credit.information.service.start.controller;

import com.rjhc.credit.information.service.api.InsertTestPoor;
import com.rjhc.credit.information.service.server.service.InsertPoorService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName InsertTestPoorController
 * @Description: TODO
 * @Author grx
 * @Date 2021/3/5
 * @Version V1.0
 **/
@RestController
@Slf4j
public class InsertTestPoorController implements InsertTestPoor {
    @Autowired
    private InsertPoorService insertPoorService;
    @Override
    public RestfulApiResponse savePoor() throws Exception {
        insertPoorService.savePoor();
        return RestfulApiResponse.success();
    }
}
