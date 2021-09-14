package com.rjhc.credit.information.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.api.model.param.WaringPoorParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName WaringPoorInterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/9/21
 * @Version V1.0
 **/
@RequestMapping("/waringPoor")
@Api(value = "预警信息", tags = {"预警信息"})
public interface WaringPoorInterface {

    @ApiOperation(value = "预警信息展示", notes = "预警信息展示")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping
    RestfulApiResponse<Page<WaringPoorParam>> queryAuditRecords(@RequestBody WaringPoorParam waringPoorParam) throws Exception;
}
