package com.rjhc.credit.information.service.start.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.model.dto.OperationLogDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.api.PoorHouseholdsInformationInterface;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.param.PoorHouseholdsInformationParam;
import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import com.rjhc.credit.information.service.server.middleware.pdf.PdtUtils;
import com.rjhc.credit.information.service.server.service.OperationLogService;
import com.rjhc.credit.information.service.server.service.PoorHouseholdsInformationService;
import com.rjhc.credit.information.service.server.service.StatisticalAnalysisService;
import com.rjhc.credit.information.service.server.service.UserService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import com.rjhc.matrix.framework.core.util.DateUtils;
import com.rjhc.matrix.framework.core.util.RequestUtil;
import com.rjhc.matrix.framework.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @ClassName PoorHouseholdsInformationController
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/14
 * @Version V1.0
 **/
@RestController
@Slf4j
public class PoorHouseholdsInformationController implements PoorHouseholdsInformationInterface {
    @Autowired
    private PoorHouseholdsInformationService poorHouseholdsInformationService;
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private StatisticalAnalysisService statisticalAnalysisService;
    @Autowired
    private UserService userService;
    @Autowired
    private GetXiAnData xiAnData;
    @Autowired
    private RequestUtil requestUtil;
    @OptionalLog(module = "????????????",operations = "?????????????????????")
    @Override
    public RestfulApiResponse<Page<PoorHouseholdsInformationDto>> queryAuditRecords(PoorHouseholdsInformationParam poorHouseholdsInformationParam) {
        Page<PoorHouseholdsInformation> poorHouseholdsInformationPage = poorHouseholdsInformationService.selectPage(poorHouseholdsInformationParam);
        return RestfulApiResponse.success(poorHouseholdsInformationPage);
    }
    @OptionalLog(module = "????????????",operations = "?????????????????????")
    @Override
    public RestfulApiResponse<List<PoorHouseholdsInformationDto>> exportExcelDate(PoorHouseholdsInformationParam poorHouseholdsInformationParam, HttpServletResponse response, HttpServletRequest request) {
        log.info("??????????????????");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");//????????????
        response.setCharacterEncoding("utf-8");//????????????
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        List<PoorHouseholdsInformationDto> poorHouseholdsInformationDtos = poorHouseholdsInformationService.slectAll(poorHouseholdsInformationParam);
        return RestfulApiResponse.success(poorHouseholdsInformationDtos);
    }
    @OptionalLog(module = "????????????",operations = "??????????????????1")
    @Override
    public void getProductById(String id,String name, HttpServletResponse response, HttpServletRequest request) throws Exception {
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = poorHouseholdsInformationService.seleById(id);
        log.info("????????????PDF??????");
        String toKen = request.getHeader("toKen");
        //?????????????????????
        String userIdHeader = requestUtil.getUserIdHeader();
        //???????????????????????????
        Map<String, Object> role = xiAnData.getUserandOrg(userIdHeader);
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //??????pdf
        String fileName=date+"???????????????.pdf";
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // ???IE?????????????????????
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        //???????????????????????? ?????????????????????
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = statisticalAnalysisService.seleByIdCard(poorHouseholdsInformationDto.getIdCard());
        ArrayList<StatisticalAnalysisDto> list = new ArrayList<>();
        statisticalAnalysisDtos.forEach(statisticalAnalysisDto -> {
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getCreditAccountLogo())){
                if("1".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("???");
                }
                if("0".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("???");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getLoanQuality())){
                if("1".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("2".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("3".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("4".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("5".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getGuaranteeMethod())){
                if("1".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("2".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("3".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("4".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("5".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("?????????");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getDiscountLoanSign())){
                if("1".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("???");
                }
                if("0".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("???");
                }
            }
            list.add(statisticalAnalysisDto);
        });

        //  ?????????????????????????????????????????????PDF??????
        List<OperationLogDto> operationLogDtoList = operationLogService.selByIDCardAndOpName(poorHouseholdsInformationDto.getIdCard(), "??????????????????",role.get("pbcCode").toString());
        new PdtUtils().generatePDF(outputStream,"????????????????????????????????????????????????", poorHouseholdsInformationDto,list,operationLogDtoList);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" +  fileName);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    @OptionalLog(module = "????????????",operations = "??????????????????")
    public RestfulApiResponse<PoorHouseholdsInformationDto> selectPoor(String id) {
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = poorHouseholdsInformationService.selectPoor(id, null);
        return RestfulApiResponse.success(poorHouseholdsInformationDto);
    }
    @Override
    public void selectPoorPDF(String id, String token, String pdfName, HttpServletResponse response, HttpServletRequest request) throws IOException, Exception {
        //?????????????????????
        String userIdHeader = requestUtil.getUserIdHeader();
        //???????????????????????????
        Map<String, Object> role = xiAnData.getUserandOrg(userIdHeader);
        log.info("????????????PDF??????");
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = poorHouseholdsInformationService.selectPoor(id, null);
        if(poorHouseholdsInformationDto == null){
            log.error("????????????????????????????????????????????????");
            return;
        }
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //??????pdf
        String fileName=date+"???????????????.pdf";//??????????????????
        //???????????????????????????????????????
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // ???IE?????????????????????
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        //???????????????????????? ?????????????????????
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = statisticalAnalysisService.seleByIdCard(poorHouseholdsInformationDto.getIdCard());
        ArrayList<StatisticalAnalysisDto> list = new ArrayList<>();
        statisticalAnalysisDtos.forEach(statisticalAnalysisDto -> {
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getCreditAccountLogo())){
                if("1".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("???");
                }
                if("0".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("???");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getLoanQuality())){
                if("1".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("2".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("3".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("4".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
                if("5".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("??????");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getGuaranteeMethod())){
                if("1".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("2".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("3".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("4".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("??????");
                }
                if("5".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("?????????");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getDiscountLoanSign())){
                if("1".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("???");
                }
                if("0".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("???");
                }
            }
            list.add(statisticalAnalysisDto);
        });

        //  ?????????????????????????????????????????????PDF??????
        String orgId=null;
        if(role != null && role.size() >0){
            orgId = role.get("pbcCode").toString();
        }
        List<OperationLogDto> operationLogDtoList = operationLogService.selByIDCardAndOpName(poorHouseholdsInformationDto.getIdCard(), "??????????????????",orgId);
        new PdtUtils().generatePDF(outputStream,"????????????????????????????????????????????????", poorHouseholdsInformationDto,list,operationLogDtoList);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" +  fileName);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public RestfulApiResponse<Map<String, String>> selePoorByIdCard(List<String> creditLevels) {
        /**
         * TODO ????????????
         */
        creditLevels.add("A");
        creditLevels.add("B");
        Map<String, String> stringStringMap = poorHouseholdsInformationService.selePoorByIdCard(creditLevels);
        return RestfulApiResponse.success(stringStringMap);
    }

    @Override
    public RestfulApiResponse updateImageByIdCard(MultipartFile file, String idCard,HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("utf-8");
        String encode="";
        if(!file.isEmpty()){
            BASE64Encoder encoder = new BASE64Encoder();
             encode = encoder.encode(file.getBytes());
        }

        poorHouseholdsInformationService.updateImageByIdCard(idCard,encode);
        return RestfulApiResponse.success();
    }


}
