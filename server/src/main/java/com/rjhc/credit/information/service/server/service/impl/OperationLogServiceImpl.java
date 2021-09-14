package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.OperationLogDto;
import com.rjhc.credit.information.service.api.model.param.OperationLogParam;
import com.rjhc.credit.information.service.server.common.convertor.OperationLogConvertor;
import com.rjhc.credit.information.service.server.common.enums.BizExceptionEnum;
import com.rjhc.credit.information.service.server.dao.dataobject.OperationLog;
import com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis;
import com.rjhc.credit.information.service.server.dao.mapper.OperationLogMapper;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.service.OperationLogService;
import com.rjhc.matrix.framework.core.exception.bean.BizException;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import com.rjhc.matrix.framework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OperationLogServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/25
 * @Version V1.0
 **/
@Service
public class OperationLogServiceImpl extends Thread implements OperationLogService {
    @Resource
    private OperationLogMapper operationLogMapper;
    @Autowired
    private GetXiAnData getXiAnData;
    @Override
    public Page<OperationLog> selectLogsByPage(OperationLogParam operationLogParam) {
        Integer currentPageNum = operationLogParam.getCurrentPageNum();
        Integer pageSize = operationLogParam.getPageSize();
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        //根据操作时间倒叙排序
        queryWrapper.orderByDesc(OperationLog::getOperationDate);
        //模糊查询条件
        //根据用户名称模糊查询
        if(!StringUtil.isEmpty(operationLogParam.getUserName())){
            queryWrapper.and(wrapper->{
                wrapper.or().like(OperationLog::getUserName,operationLogParam.getUserName());
            });
            queryWrapper.like(OperationLog::getUserName,operationLogParam.getUserName());
        }
        //根据操作名称模糊查询
        if(!StringUtil.isEmpty(operationLogParam.getOperationName())){
            queryWrapper.and(wrapper->{
                wrapper.or().like(OperationLog::getOperationName,operationLogParam.getOperationName());
            });
        }
        //根据操作模块模糊查询
        if(!StringUtil.isEmpty(operationLogParam.getModelName())){
            queryWrapper.and(wrapper->{
                wrapper.or().like(OperationLog::getModelName,operationLogParam.getModelName());
            });
        }
        //根据操作时间查询
        if(operationLogParam.getOperationDate() != null){
            queryWrapper.eq(OperationLog::getOperationDate,operationLogParam.getOperationDate());
        }
        Page<OperationLog> operationLogPage = operationLogMapper.selectPage(new Page<>(currentPageNum, pageSize), queryWrapper);

        return operationLogPage;
    }

    @Override
    public List<OperationLogDto> selectLogs(OperationLogParam operationLogParam) {
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        //根据操作时间倒叙排序
        queryWrapper.orderByDesc(OperationLog::getOperationDate);
        //模糊查询条件
        //根据用户名称模糊查询
        if(!StringUtil.isEmpty(operationLogParam.getUserName())){
            queryWrapper.and(wrapper->{
                wrapper.or().like(OperationLog::getUserName,operationLogParam.getUserName());
            });
            queryWrapper.like(OperationLog::getUserName,operationLogParam.getUserName());
        }
        //根据操作名称模糊查询
        if(!StringUtil.isEmpty(operationLogParam.getOperationName())){
            queryWrapper.and(wrapper->{
                wrapper.or().like(OperationLog::getOperationName,operationLogParam.getOperationName());
            });
        }
        //根据操作模块模糊查询
        if(!StringUtil.isEmpty(operationLogParam.getModelName())){
            queryWrapper.and(wrapper->{
                wrapper.or().like(OperationLog::getModelName,operationLogParam.getModelName());
            });
        }
        //根据操作时间查询
        if(operationLogParam.getOperationDate() != null){
            queryWrapper.eq(OperationLog::getOperationDate,operationLogParam.getOperationDate());
        }
        List<OperationLog> operationLogs = operationLogMapper.selectList(queryWrapper);
        List<OperationLogDto> operationLogDtos = OperationLogConvertor.INSTANCE.entityListToDtoList(operationLogs);
        if(operationLogDtos == null || operationLogDtos.size() == 0){
            throw new BizException(BizExceptionEnum.EXPORT_EXCEL_NULL.getCode(), BizExceptionEnum.EXPORT_EXCEL_NULL.getMsg());
        }
        return operationLogDtos;
    }

    @Override
    public List<OperationLogDto> selByIDCardAndOpName(String idCard, String opName,String bankId) throws Exception {
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtil.isEmpty(bankId)){
            queryWrapper.eq(OperationLog::getBankId,bankId);
        }
        if(!StringUtil.isEmpty(idCard)){
            queryWrapper.eq(OperationLog::getIdCard,idCard);
        }
        if(!StringUtil.isEmpty(opName)){
            queryWrapper.eq(OperationLog::getOperationName,opName);
        }
        List<OperationLog> operationLogs = operationLogMapper.selectList(queryWrapper);
        List<OperationLogDto> operationLogDtos = OperationLogConvertor.INSTANCE.entityListToDtoList(operationLogs);
        for (OperationLogDto operationLogDto : operationLogDtos) {
            Map<String, String> deptOrg = getXiAnData.getDeptOrg(operationLogDto.getBankId());
            operationLogDto.setBanekName(deptOrg.get("orgDscr"));
        }
        return operationLogDtos;
    }

    @Override
    public void testData() {
        for (int i = 0; i < 500; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        OperationLog log = new OperationLog();
                        log.setId(RandomUtil.UUID36());
                        log.setModelName("测试数据");
                        log.setOperationName("测试数据");
                        log.setOperationDate(new Date());
                        log.setSourceCode("测试数据");
                        operationLogMapper.insert(log);
                    }
                }
            }).start();
            System.out.println("第"+i+"行");
        }
    }


}
