package com.rjhc.credit.information.service.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PoorHouseholdsInformationInterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
@RequestMapping("/poorHouseholdsInformation")
@Api(value = "贫困户信息分析", tags = {"贫困户信息分析"})
public interface PoorHouseholdsInformationInterface {
    /**
     * 功能描述：
     * 〈贫困户信息分析〉
     * @Author: grx
     * @Date: 10:22 上午 2020/8/14
     * @param poorHouseholdsInformationParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto>>
     */
    @ApiOperation(value = "贫困户信息分析查询", notes = "贫困户信息分析查询")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping
    RestfulApiResponse<Page<PoorHouseholdsInformationDto>> queryAuditRecords(@RequestBody PoorHouseholdsInformationParam poorHouseholdsInformationParam);
    /**
     * 功能描述：
     * 〈数据导出功能〉
     * @Author: grx
     * @Date: 10:49 上午 2020/8/14
     * @param poorHouseholdsInformationParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<java.lang.String>
     */
    @ApiOperation(value = "贫困户信息分析查询导出", notes = "贫困户信息分析查询导出")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping(value = "/exportExcelDate")
    RestfulApiResponse<List<PoorHouseholdsInformationDto>> exportExcelDate(@RequestBody PoorHouseholdsInformationParam poorHouseholdsInformationParam, HttpServletResponse response, HttpServletRequest request);

    /**
     * 功能描述：
     * 〈根据id生成PDF〉
     * @Author: grx
     * @Date: 9:55 上午 2020/8/27
     * @param id
     * @param name
     * @param response
     * @param request
     * @return: void
     */
    @ApiOperation(value = "根据id生成pdf数据", notes = "根据id生成pdf数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "贫困户id", required = true, paramType = "path", dataType = "string")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/{id}/{name}")
    void getProductById(@PathVariable("id")String id,@PathVariable("name")String name, HttpServletResponse response, HttpServletRequest request) throws IOException, Exception;
    /**
     * 功能描述：
     * 〈根据身份证号和户主姓名查询贫困户信息〉
     * @Author: grx
     * @Date: 3:01 下午 2020/10/9
     * @param id
     * @param
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto>
     */
    @ApiOperation(value = "根据身份证号和户主姓名查询贫困户信息", notes = "根据身份证号和户主姓名查询贫困户信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/selectPoor/{id}")
    RestfulApiResponse<PoorHouseholdsInformationDto> selectPoor(@PathVariable("id")String id);


    @ApiOperation(value = "根据身份证号和户主姓名查询贫困户信息生成pdf数据", notes = "根据身份证号和户主姓名查询贫困户信息生成pdf数据")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/selectPoorPDF/{id}/{token}/{pdfName}")
    void selectPoorPDF(@PathVariable("id")String id,@PathVariable("token")String token, @PathVariable("pdfName")String pdfName,HttpServletResponse response, HttpServletRequest request) throws IOException, Exception;


    @ApiOperation(value = "根据当前信贷等级查询当前贫困户数量", notes = "根据当前信贷等级查询当前贫困户数量")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/selePoorByIdCard")
    RestfulApiResponse<Map<String,String>> selePoorByIdCard(@RequestBody List<String> creditLevels);


    @ApiOperation(value = "文件授权书上传", notes = "文件授权书上传")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping(value = "/updateImageByIdCard/{idCard}")
    RestfulApiResponse updateImageByIdCard(@RequestBody MultipartFile excelFile,@PathVariable("idCard")String idCard,HttpServletRequest request) throws Exception;



}
