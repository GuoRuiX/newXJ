package com.rjhc.credit.information.service.server.service;

import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import com.rjhc.credit.information.service.api.model.dto.RegionSelDto;
import com.rjhc.credit.information.service.server.dao.dataobject.Region;

import javax.swing.*;
import java.util.List;

/**
 * @ClassName RegionService
 * @Description: TODO
 * @Author grx
 * @Date 2020/11/23
 * @Version V1.0
 **/
public interface RegionService {
    List<Region> selectAll();

    List<Region> selectByLevel(String level);

    List<RegionSelDto> getTree();

    List<RegionDto> selectByParentId(String id);
}
