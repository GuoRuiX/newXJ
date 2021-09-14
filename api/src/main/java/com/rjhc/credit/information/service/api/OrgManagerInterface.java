package com.rjhc.credit.information.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.OrgManagerDto;
import com.rjhc.credit.information.service.api.model.param.OrgManagerParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName OrgManagerInterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@RequestMapping("/orgManagerInterface")
@Api(value = "机构管理", tags = {"机构管理"})
public interface OrgManagerInterface {

    /**
     * 功能描述：
     * 〈机构管理列表展示〉
     * @Author: grx
     * @Date: 1:37 下午 2020/8/27
     * @param orgManagerParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.api.model.dto.OrgManagerDto>>
     */
    @ApiOperation(value = "机构列表展示", notes = "机构列表展示")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping
    RestfulApiResponse<Page<OrgManagerDto>> queryAuditRecords(@RequestBody OrgManagerParam orgManagerParam);
}
