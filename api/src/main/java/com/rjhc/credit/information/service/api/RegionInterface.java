package com.rjhc.credit.information.service.api;

import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import com.rjhc.credit.information.service.api.model.dto.RegionSelDto;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName Regioninterface
 * @Description: TODO
 * @Author grx
 * @Date 2020/11/23
 * @Version V1.0
 **/
@RequestMapping("/region")
@Api(value = "地区信息", tags = {"地区信息"})
public interface RegionInterface {
    @ApiOperation(value = "地区信息列表展示", notes = "地区信息列表展示")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping
    RestfulApiResponse<RegionDto> selectAll();
    /**
     * 功能描述：
     * 〈查询3级机构数据〉
     * @Author: grx
     * @Date: 上午10:47 2020/12/1
     * @param level
     * @return: com.rjhc.matrix.framework.core.response.RestfulApiResponse<com.rjhc.credit.information.service.api.model.dto.RegionDto>
     */
    @ApiOperation(value = "查询3级地区数据", notes = "查询3级地区数据")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/selectByLevel/{level}")
    RestfulApiResponse<RegionDto> selectByLevel(@PathVariable("level")String level);

    @ApiOperation(value = "获取地区结构树", notes = "获取地区结构树")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/getTree")
    RestfulApiResponse<List<RegionSelDto>> getTree() throws Exception;

    @ApiOperation(value = "根据主id查询下级地区", notes = "根据主id查询下级地区")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/selectByParentId/{id}")
    RestfulApiResponse<RegionDto> selectByParentId(@PathVariable("id")String id);


    @ApiOperation(value = "获取地区结构树", notes = "获取地区结构树")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/testArea")
    RestfulApiResponse<List<RegionSelDto>> testArea() throws Exception;


    @ApiOperation(value = "获取地区结构树", notes = "获取地区结构树")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = RestfulApiResponse.class)})
    @GetMapping("/testAreaByLevel/{id}/{level}")
    RestfulApiResponse<List<RegionSelDto>> testAreaByLevel(@PathVariable("id")String id,@PathVariable("level")String level) throws Exception;



}
