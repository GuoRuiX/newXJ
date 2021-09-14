package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.param.OrgManagerParam;
import com.rjhc.credit.information.service.server.dao.dataobject.OrgManager;
import com.rjhc.credit.information.service.server.dao.mapper.OrgManagerMapper;
import com.rjhc.credit.information.service.server.service.OrgManagerService;
import com.rjhc.matrix.framework.core.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName OrgManagerServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/27
 * @Version V1.0
 **/
@Service
public class OrgManagerServiceImpl implements OrgManagerService {
    @Resource
    private OrgManagerMapper orgManagerMapper;
    @Override
    public Page<OrgManager> selectByPage(OrgManagerParam orgManagerParam) {
        LambdaQueryWrapper<OrgManager> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtil.isEmpty(orgManagerParam.getOrgType())){
            if("1".equals(orgManagerParam.getOrgType())){
                orgManagerParam.setOrgType("01");
            }
            if("2".equals(orgManagerParam.getOrgType())) {
                orgManagerParam.setOrgType("02");
            }
            queryWrapper.eq(OrgManager::getOrgType,orgManagerParam.getOrgType());
        }
        Page<OrgManager> orgManagerPage = orgManagerMapper.selectPage(new Page<OrgManager>(orgManagerParam.getCurrentPageNum(), orgManagerParam.getPageSize()), queryWrapper);
        return orgManagerPage;
    }
}
