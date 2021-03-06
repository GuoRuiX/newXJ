package com.rjhc.credit.information.service.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.server.common.convertor.PoorHouseholdsInformationConvertor;
import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import com.rjhc.credit.information.service.server.dao.dataobject.StatisticalAnalysis;
import com.rjhc.credit.information.service.server.dao.mapper.PoorHouseholdsInformationMapper;
import com.rjhc.credit.information.service.server.service.PoorHouseholdsInformationService;
import com.rjhc.matrix.framework.core.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PoorHouseholdsInformationServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
@Service
public class PoorHouseholdsInformationServiceImpl implements PoorHouseholdsInformationService {
    @Resource
    private PoorHouseholdsInformationMapper poorHouseholdsInformationMapper;
    @Override
    public Page<PoorHouseholdsInformation> selectPage(PoorHouseholdsInformationParam poorHouseholdsInformationParam) {
        Integer currentPageNum = poorHouseholdsInformationParam.getCurrentPageNum();
        Integer pageSize = poorHouseholdsInformationParam.getPageSize();
        LambdaQueryWrapper<PoorHouseholdsInformation> queryWrapper = new LambdaQueryWrapper<>();
        PoorHouseholdsInformation poorHouseholdsInformation = PoorHouseholdsInformationConvertor.INSTANCE.prarmToentity(poorHouseholdsInformationParam);

        /*queryWrapper.between(StatisticalAnalysis::getLoanAmount, statisticalAnalysisParam.getStartLoanAmount(), statisticalAnalysisParam.getEndLoanAmount());*/
        //??????????????????????????????
        queryWrapper.orderByDesc(PoorHouseholdsInformation::getSynchronizationDate);
        //????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getHouseholderName())){
            queryWrapper.eq(PoorHouseholdsInformation::getHouseholderName,poorHouseholdsInformation.getHouseholderName());
        }
        //??????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getNation())){
            queryWrapper.eq(PoorHouseholdsInformation::getNation,poorHouseholdsInformation.getNation());
        }
        //???????????????????????? ????????????
        if(poorHouseholdsInformationParam.getStartAge() != null && poorHouseholdsInformationParam.getEndAge() != null){
            queryWrapper.between(PoorHouseholdsInformation::getAge, poorHouseholdsInformationParam.getStartAge(), poorHouseholdsInformationParam.getEndAge());
        }
        //???????????????????????????
        if(poorHouseholdsInformationParam.getStartAge() != null && poorHouseholdsInformationParam.getEndAge() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getAge,poorHouseholdsInformationParam.getStartAge());
        }
        if(poorHouseholdsInformationParam.getStartAge() == null && poorHouseholdsInformationParam.getEndAge() != null){
            queryWrapper.le(PoorHouseholdsInformation::getAge,poorHouseholdsInformationParam.getEndAge());
        }
        //????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getGender())){
            queryWrapper.eq(PoorHouseholdsInformation::getGender,poorHouseholdsInformation.getGender());
        }
        //??????????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getAddress())){
            queryWrapper.eq(PoorHouseholdsInformation::getAddress,poorHouseholdsInformation.getAddress());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartFamilySize() != null && poorHouseholdsInformationParam.getEndFamilySize() != null){
            queryWrapper.between(PoorHouseholdsInformation::getFamilySize, poorHouseholdsInformationParam.getStartFamilySize(), poorHouseholdsInformationParam.getEndFamilySize());
        }
        if(poorHouseholdsInformationParam.getStartFamilySize() != null && poorHouseholdsInformationParam.getEndFamilySize() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getFamilySize,poorHouseholdsInformationParam.getStartFamilySize());
        }
        if(poorHouseholdsInformationParam.getStartFamilySize() == null && poorHouseholdsInformationParam.getEndFamilySize() != null){
            queryWrapper.le(PoorHouseholdsInformation::getFamilySize,poorHouseholdsInformationParam.getEndFamilySize());
        }
        //????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getPovertySign())){
            queryWrapper.eq(PoorHouseholdsInformation::getPovertySign,poorHouseholdsInformation.getPovertySign());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartLandarea() != null && poorHouseholdsInformationParam.getEndLandarea() != null){
            queryWrapper.between(PoorHouseholdsInformation::getLandarea, poorHouseholdsInformationParam.getStartLandarea(), poorHouseholdsInformationParam.getEndLandarea());
        }
        if(poorHouseholdsInformationParam.getStartLandarea() != null && poorHouseholdsInformationParam.getEndLandarea() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getLandarea,poorHouseholdsInformationParam.getStartLandarea());
        }
        if(poorHouseholdsInformationParam.getStartLandarea() == null && poorHouseholdsInformationParam.getEndLandarea() != null){
            queryWrapper.le(PoorHouseholdsInformation::getLandarea,poorHouseholdsInformationParam.getEndLandarea());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartWoodlandarea() != null && poorHouseholdsInformationParam.getEndWoodlandarea() != null){
            queryWrapper.between(PoorHouseholdsInformation::getWoodlandarea, poorHouseholdsInformationParam.getStartWoodlandarea(), poorHouseholdsInformationParam.getEndWoodlandarea());
        }
        if(poorHouseholdsInformationParam.getStartWoodlandarea() != null && poorHouseholdsInformationParam.getEndWoodlandarea() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getWoodlandarea,poorHouseholdsInformationParam.getStartWoodlandarea());
        }
        if(poorHouseholdsInformationParam.getStartWoodlandarea() == null && poorHouseholdsInformationParam.getEndWoodlandarea() != null){
            queryWrapper.le(PoorHouseholdsInformation::getWoodlandarea,poorHouseholdsInformationParam.getEndWoodlandarea());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartIncome() != null && poorHouseholdsInformationParam.getEndIncome() != null){
            queryWrapper.between(PoorHouseholdsInformation::getIncome, poorHouseholdsInformationParam.getStartIncome(), poorHouseholdsInformationParam.getEndIncome());
        }
        if(poorHouseholdsInformationParam.getStartIncome() != null && poorHouseholdsInformationParam.getEndIncome() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getIncome,poorHouseholdsInformationParam.getStartIncome());
        }
        if(poorHouseholdsInformationParam.getStartIncome() == null && poorHouseholdsInformationParam.getEndIncome() != null){
            queryWrapper.le(PoorHouseholdsInformation::getIncome,poorHouseholdsInformationParam.getEndIncome());
        }
        Page<PoorHouseholdsInformation> poorHouseholdsInformationPage = poorHouseholdsInformationMapper.selectPage(new Page<PoorHouseholdsInformation>(currentPageNum, pageSize), queryWrapper);

        return poorHouseholdsInformationPage;
    }

    @Override
    public List<PoorHouseholdsInformationDto> slectAll(PoorHouseholdsInformationParam poorHouseholdsInformationParam) {
        LambdaQueryWrapper<PoorHouseholdsInformation> queryWrapper = new LambdaQueryWrapper<>();
        PoorHouseholdsInformation poorHouseholdsInformation = PoorHouseholdsInformationConvertor.INSTANCE.prarmToentity(poorHouseholdsInformationParam);
        //??????????????????????????????
        queryWrapper.orderByDesc(PoorHouseholdsInformation::getSynchronizationDate);
        //????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getHouseholderName())){
            queryWrapper.eq(PoorHouseholdsInformation::getHouseholderName,poorHouseholdsInformation.getHouseholderName());
        }
        //???????????????????????? ????????????
        if(poorHouseholdsInformationParam.getStartAge() != null && poorHouseholdsInformationParam.getEndAge() != null){
            queryWrapper.between(PoorHouseholdsInformation::getAge, poorHouseholdsInformationParam.getStartAge(), poorHouseholdsInformationParam.getEndAge());
        }
        //???????????????????????????
        if(poorHouseholdsInformationParam.getStartAge() != null && poorHouseholdsInformationParam.getEndAge() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getAge,poorHouseholdsInformationParam.getStartAge());
        }
        if(poorHouseholdsInformationParam.getStartAge() == null && poorHouseholdsInformationParam.getEndAge() != null){
            queryWrapper.le(PoorHouseholdsInformation::getAge,poorHouseholdsInformationParam.getEndAge());
        }
        //????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getGender())){
            queryWrapper.eq(PoorHouseholdsInformation::getGender,poorHouseholdsInformation.getGender());
        }
        //??????????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getAddress())){
            queryWrapper.eq(PoorHouseholdsInformation::getAddress,poorHouseholdsInformation.getAddress());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartFamilySize() != null && poorHouseholdsInformationParam.getEndFamilySize() != null){
            queryWrapper.between(PoorHouseholdsInformation::getFamilySize, poorHouseholdsInformationParam.getStartFamilySize(), poorHouseholdsInformationParam.getEndFamilySize());
        }
        if(poorHouseholdsInformationParam.getStartFamilySize() != null && poorHouseholdsInformationParam.getEndFamilySize() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getFamilySize,poorHouseholdsInformationParam.getStartFamilySize());
        }
        if(poorHouseholdsInformationParam.getStartFamilySize() == null && poorHouseholdsInformationParam.getEndFamilySize() != null){
            queryWrapper.le(PoorHouseholdsInformation::getFamilySize,poorHouseholdsInformationParam.getEndFamilySize());
        }
        //????????????????????????
        if(!StringUtil.isEmpty(poorHouseholdsInformation.getPovertySign())){
            queryWrapper.eq(PoorHouseholdsInformation::getPovertySign,poorHouseholdsInformation.getPovertySign());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartLandarea() != null && poorHouseholdsInformationParam.getEndLandarea() != null){
            queryWrapper.between(PoorHouseholdsInformation::getLandarea, poorHouseholdsInformationParam.getStartLandarea(), poorHouseholdsInformationParam.getEndLandarea());
        }
        if(poorHouseholdsInformationParam.getStartLandarea() != null && poorHouseholdsInformationParam.getEndLandarea() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getLandarea,poorHouseholdsInformationParam.getStartLandarea());
        }
        if(poorHouseholdsInformationParam.getStartLandarea() == null && poorHouseholdsInformationParam.getEndLandarea() != null){
            queryWrapper.le(PoorHouseholdsInformation::getLandarea,poorHouseholdsInformationParam.getEndLandarea());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartWoodlandarea() != null && poorHouseholdsInformationParam.getEndWoodlandarea() != null){
            queryWrapper.between(PoorHouseholdsInformation::getWoodlandarea, poorHouseholdsInformationParam.getStartWoodlandarea(), poorHouseholdsInformationParam.getEndWoodlandarea());
        }
        if(poorHouseholdsInformationParam.getStartWoodlandarea() != null && poorHouseholdsInformationParam.getEndWoodlandarea() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getWoodlandarea,poorHouseholdsInformationParam.getStartWoodlandarea());
        }
        if(poorHouseholdsInformationParam.getStartWoodlandarea() == null && poorHouseholdsInformationParam.getEndWoodlandarea() != null){
            queryWrapper.le(PoorHouseholdsInformation::getWoodlandarea,poorHouseholdsInformationParam.getEndWoodlandarea());
        }
        //????????????????????????
        if(poorHouseholdsInformationParam.getStartIncome() != null && poorHouseholdsInformationParam.getEndIncome() != null){
            queryWrapper.between(PoorHouseholdsInformation::getIncome, poorHouseholdsInformationParam.getStartIncome(), poorHouseholdsInformationParam.getEndIncome());
        }
        if(poorHouseholdsInformationParam.getStartIncome() != null && poorHouseholdsInformationParam.getEndIncome() == null){
            queryWrapper.ge(PoorHouseholdsInformation::getIncome,poorHouseholdsInformationParam.getStartIncome());
        }
        if(poorHouseholdsInformationParam.getStartIncome() == null && poorHouseholdsInformationParam.getEndIncome() != null){
            queryWrapper.le(PoorHouseholdsInformation::getIncome,poorHouseholdsInformationParam.getEndIncome());
        }
        List<PoorHouseholdsInformation> poorHouseholdsInformations = poorHouseholdsInformationMapper.selectList(queryWrapper);
        List<PoorHouseholdsInformationDto> poorHouseholdsInformationDtos = PoorHouseholdsInformationConvertor.INSTANCE.entityListToDtoList(poorHouseholdsInformations);
        return poorHouseholdsInformationDtos;
    }

    @Override
    public PoorHouseholdsInformationDto seleById(String id) {
        PoorHouseholdsInformation poorHouseholdsInformation = poorHouseholdsInformationMapper.selectById(id);
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = PoorHouseholdsInformationConvertor.INSTANCE.entityToDto(poorHouseholdsInformation);
        return poorHouseholdsInformationDto;
    }

    @Override
    public PoorHouseholdsInformationDto selectPoor(String idcard, String name) {
        LambdaQueryWrapper<PoorHouseholdsInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PoorHouseholdsInformation::getIdCard,idcard);
        PoorHouseholdsInformation poorHouseholdsInformation = poorHouseholdsInformationMapper.selectOne(queryWrapper);
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = PoorHouseholdsInformationConvertor.INSTANCE.entityToDto(poorHouseholdsInformation);
        return poorHouseholdsInformationDto;
    }

    @Override
    public Map<String, String> selePoorByIdCard(List<String> level) {
        return poorHouseholdsInformationMapper.selePoorByIdCard(level);
    }

    @Override
    public void updateImageByIdCard(String idCard, Object imageBase64) {
        LambdaQueryWrapper<PoorHouseholdsInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PoorHouseholdsInformation::getIdCard,idCard);
        PoorHouseholdsInformation poorHouseholdsInformation = new PoorHouseholdsInformation();
        poorHouseholdsInformation.setImage(imageBase64);
        poorHouseholdsInformationMapper.update(poorHouseholdsInformation,queryWrapper);
    }
}
