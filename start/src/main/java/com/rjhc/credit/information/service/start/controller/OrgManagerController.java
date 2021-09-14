package com.rjhc.credit.information.service.start.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.OrgManagerInterface;
import com.rjhc.credit.information.service.api.model.dto.OrgManagerDto;
import com.rjhc.credit.information.service.api.model.param.OrgManagerParam;
import com.rjhc.credit.information.service.server.dao.dataobject.OrgManager;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.OrgManagerService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OrgManagerController
 * @Description: 机构管理
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@RestController
@Slf4j
public class OrgManagerController implements OrgManagerInterface {
    @Autowired
    private OrgManagerService orgManagerService;
    @OptionalLog(module = "机构管理",operations = "机构列表展示")
    @Override
    public RestfulApiResponse<Page<OrgManagerDto>> queryAuditRecords(OrgManagerParam orgManagerParam) {
        Page<OrgManager> orgManagerPage = orgManagerService.selectByPage(orgManagerParam);
        return RestfulApiResponse.success(orgManagerPage);
    }
}
