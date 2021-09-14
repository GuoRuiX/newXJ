package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.param.WaringPoorParam;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;
import com.rjhc.credit.information.service.server.dao.mapper.WaringPoorMapper;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.service.WaringPoorService;
import com.rjhc.matrix.framework.core.util.RequestUtil;
import com.rjhc.matrix.framework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WaringPoorServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/9/21
 * @Version V1.0
 **/
@Service
public class WaringPoorServiceImpl implements WaringPoorService {
    @Resource
    private WaringPoorMapper waringPoorMapper;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private GetXiAnData getXiAnData;
    /**
     * 功能描述：
     * 〈分页查询预警信息数据〉
     * @Author: grx
     * @Date: 11:18 上午 2020/9/21
     * @param waringPoorParam
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor>
     */
    @Override
    public Page<WaringPoor> selPage(WaringPoorParam waringPoorParam) throws Exception {
        Integer currentPageNum = waringPoorParam.getCurrentPageNum();
        Integer pageSize = waringPoorParam.getPageSize();
        LambdaQueryWrapper<WaringPoor> queryWrapper = new LambdaQueryWrapper<>();
        //按照同步时间排序
        queryWrapper.orderByDesc(WaringPoor::getSynchronizationDate);
        if(!StringUtil.isEmpty(waringPoorParam.getYears())){
            queryWrapper.eq(WaringPoor::getYears,waringPoorParam.getYears());
        }
        if(!StringUtil.isEmpty(waringPoorParam.getMonth())){
            queryWrapper.eq(WaringPoor::getMonth,waringPoorParam.getMonth());
        }
        if(!StringUtil.isEmpty(waringPoorParam.getBankCode())){
            queryWrapper.eq(WaringPoor::getBankCode,waringPoorParam.getBankCode());
        }
        Page<WaringPoor> waringPoorPage = waringPoorMapper.selectPage(new Page<WaringPoor>(currentPageNum, pageSize), queryWrapper);
        return waringPoorPage;
    }

    @Override
    public void delByType(String Type) {
        LambdaQueryWrapper<WaringPoor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WaringPoor::getType,Type);
        //根据类型删除预警信息
        waringPoorMapper.delete(queryWrapper);
    }

    @Override
    public void updateByType(String newType, String oldType) {
        LambdaQueryWrapper<WaringPoor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WaringPoor::getType,oldType);
        WaringPoor waringPoor = new WaringPoor();
        waringPoor.setType(newType);
        //根据类型修改预警信息
        waringPoorMapper.update(waringPoor,queryWrapper);
    }
}
