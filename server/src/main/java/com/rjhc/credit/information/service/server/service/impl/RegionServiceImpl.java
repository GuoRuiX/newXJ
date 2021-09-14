package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import com.rjhc.credit.information.service.api.model.dto.RegionSelDto;
import com.rjhc.credit.information.service.server.dao.dataobject.Region;
import com.rjhc.credit.information.service.server.dao.mapper.RegionMapper;
import com.rjhc.credit.information.service.server.service.RegionService;
import com.rjhc.matrix.framework.core.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName RegionServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/11/23
 * @Version V1.0
 **/
@Service
public class RegionServiceImpl implements RegionService {
    @Resource
    private RegionMapper regionMapper;
    @Override
    public List<Region> selectAll() {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        List<Region> list = regionMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<Region> selectByLevel(String level) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtil.isEmpty(level)){
            wrapper.eq(Region::getRegionLevel,level);
        }
        List<Region> list = regionMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<RegionSelDto> getTree() {
        List<Region> regions = regionMapper.selectList(null);
        ArrayList<RegionSelDto> selDtos = new ArrayList<>();
        for (Region region : regions) {
            RegionSelDto selDto = new RegionSelDto();
            if(StringUtil.isEmpty(region.getParentId())){
                region.setParentId("0");
            }
            selDto.setValue(region.getId());
            selDto.setLabel(region.getRegionName());
            selDto.setParentId(region.getParentId());
            selDtos.add(selDto);
        }
        List<RegionSelDto> list = selDtos.stream().filter(subjects -> subjects.getParentId().equals("0"))
                .map((tree)->{
                    tree.setChildren(getChildrens(tree,selDtos));
                    return tree;
                })
                .collect(Collectors.toList());
        return list;

    }

    @Override
    public List<RegionDto> selectByParentId(String id) {
        List<RegionDto> regionDtos = regionMapper.selectByParentId(id);
        return regionDtos;
    }


    private List<RegionSelDto> getChildrens(RegionSelDto regionSelDto,List<RegionSelDto> all){
        List<RegionSelDto> children=all.stream().filter(subjects ->{
            return subjects.getParentId().equals(regionSelDto.getValue());
            //递归遍历
        }).map(subjects -> {subjects.setChildren(getChildrens(subjects,all));
            return subjects;
        }).collect(Collectors.toList());

        return children;

    }

}
