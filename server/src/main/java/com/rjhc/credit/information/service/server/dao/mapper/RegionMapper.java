package com.rjhc.credit.information.service.server.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import com.rjhc.credit.information.service.server.dao.dataobject.Region;

import java.util.List;

/**
 * @ClassName RegionMapper
 * @Description: TODO
 * @Author grx
 * @Date 2020/11/23
 * @Version V1.0
 **/
public interface RegionMapper extends BaseMapper<Region> {
    List<RegionDto> selectByParentId(String id);
}
