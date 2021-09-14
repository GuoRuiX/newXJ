package com.rjhc.credit.information.service.api;

import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.api.model.param.StatisticalAnalysisParam;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StatisticalAnalysisInterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@RequestMapping("/statisticalAnalysis")
@Api(value = "借贷信息", tags = {"借贷信息"})
public interface StatisticalAnalysisInterface {
    /**
     * 功能描述：
     * 〈统计分析模糊查询数据〉
     * @Author: grx
     * @Date: 8:13 下午 2020/8/13
     * @param statisticalAnalysisParam
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<java.util.List<com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto>>
     */
    @ApiOperation(value = "根据条件查询借贷信息", notes = "根据条件查询借贷信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping(value = "/selectFuzzy")
    RestfulApiResponse<List<StatisticalAnalysisDto>> selectFuzzy(@RequestBody StatisticalAnalysisParam statisticalAnalysisParam) throws Exception;
    /**
     * 功能描述：
     * 〈借贷信息导出查询导出〉
     * @Author: grx
     * @Date: 2:31 下午 2020/8/14
     * @param statisticalAnalysisParam
     * @param response
     * @param request
     * @return: void
     */
    @ApiOperation(value = "借贷信息导出查询导出", notes = "借贷信息导出查询导出")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping(value = "/exportExcelStatistical")
    RestfulApiResponse<List<StatisticalAnalysisDto>> exportExcelStatistical(@RequestBody StatisticalAnalysisParam statisticalAnalysisParam, HttpServletResponse response, HttpServletRequest request);

    /**
     * 功能描述：
     * 〈信贷模版信息导入并返回前台〉
     * @Author: grx
     * @Date: 9:48 上午 2020/8/18
     * @param response
     * @param request
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<java.util.List<com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto>>
     */
    @ApiOperation(value = "数据统计导入", notes = "数据统计导入")
    /*@PostMapping(value = "/importExcel")*/
    RestfulApiResponse<List<PoorHouseholdsInformationDto>> importExcel(@RequestParam("file") MultipartFile excelFile , HttpServletResponse response, HttpServletRequest request) throws IOException;

    @ApiOperation(value = "数据上报模版下载", notes = "数据上报模版下载",produces = "application/octet-stream")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/downTemplate")
    void downTemplate(HttpServletResponse response, HttpServletRequest request) throws Exception;

    @ApiOperation(value = "测试txt", notes = "测试")
    @GetMapping("/dataTransmission")
    RestfulApiResponse<String> testTxt() throws Exception;

    @ApiOperation(value = "信贷文件上传", notes = "信贷文件上传")
    @PostMapping(value = "/importExcel")
    RestfulApiResponse<String> testImport(@RequestParam("file") MultipartFile excelFile , HttpServletResponse response, HttpServletRequest request) throws Exception;

    @ApiOperation(value = "根据信用等级统计信用户信息", notes = "根据信用等级统计信用户信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @PostMapping(value = "/statByCreditLevel")
    RestfulApiResponse<List<Map<String,String>>> selByLevel();



}
