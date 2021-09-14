package com.rjhc.credit.information.service.start.controller;

import com.rjhc.credit.information.service.api.RegionInterface;
import com.rjhc.credit.information.service.api.model.dto.Area;
import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import com.rjhc.credit.information.service.api.model.dto.RegionSelDto;
import com.rjhc.credit.information.service.server.dao.dataobject.Region;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.service.RegionService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName RegionController
 * @Description: TODO
 * @Author grx
 * @Date 2020/11/23
 * @Version V1.0
 **/
@RestController
@Slf4j
public class RegionController implements RegionInterface {
    @Autowired
    private RegionService regionService;
    @Autowired
    private GetXiAnData anData;
    @Override
    public RestfulApiResponse<RegionDto> selectAll() {
        List<Region> regions = regionService.selectAll();
        return RestfulApiResponse.success(regions);
    }

    @Override
    public RestfulApiResponse<RegionDto> selectByLevel(String level) {
        List<Region> regions = regionService.selectByLevel(level);
        return RestfulApiResponse.success(regions);
    }

    @Override
    public RestfulApiResponse<List<RegionSelDto>> getTree() throws Exception {
        List<Area> area = anData.getArea();
        return RestfulApiResponse.success(area);
    }

    @Override
    public RestfulApiResponse<RegionDto> selectByParentId(String id) {
        List<RegionDto> regionDtos = regionService.selectByParentId(id);
        return RestfulApiResponse.success(regionDtos);
    }

    @Override
    public RestfulApiResponse<List<RegionSelDto>> testArea() throws Exception {
        List<Area> area = anData.getArea();
        return RestfulApiResponse.success(area);
    }

    @Override
    public RestfulApiResponse<List<RegionSelDto>> testAreaByLevel(String id, String level) throws Exception {
        List<String> areaByLevel = anData.getAreaByLevel(id, level);
        return RestfulApiResponse.success();
    }
}
