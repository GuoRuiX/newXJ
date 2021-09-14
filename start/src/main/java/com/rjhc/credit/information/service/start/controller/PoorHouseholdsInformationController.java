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
    @OptionalLog(module = "统计分析",operations = "贫困户信息查询")
    @Override
    public RestfulApiResponse<Page<PoorHouseholdsInformationDto>> queryAuditRecords(PoorHouseholdsInformationParam poorHouseholdsInformationParam) {
        Page<PoorHouseholdsInformation> poorHouseholdsInformationPage = poorHouseholdsInformationService.selectPage(poorHouseholdsInformationParam);
        return RestfulApiResponse.success(poorHouseholdsInformationPage);
    }
    @OptionalLog(module = "统计分析",operations = "贫困户信息导出")
    @Override
    public RestfulApiResponse<List<PoorHouseholdsInformationDto>> exportExcelDate(PoorHouseholdsInformationParam poorHouseholdsInformationParam, HttpServletResponse response, HttpServletRequest request) {
        log.info("数据导出开始");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");//固定格式
        response.setCharacterEncoding("utf-8");//固定格式
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        List<PoorHouseholdsInformationDto> poorHouseholdsInformationDtos = poorHouseholdsInformationService.slectAll(poorHouseholdsInformationParam);
        return RestfulApiResponse.success(poorHouseholdsInformationDtos);
    }
    @OptionalLog(module = "统计分析",operations = "信用报告查询1")
    @Override
    public void getProductById(String id,String name, HttpServletResponse response, HttpServletRequest request) throws Exception {
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = poorHouseholdsInformationService.seleById(id);
        log.info("数据生成PDF开始");
        String toKen = request.getHeader("toKen");
        //获取当前登陆人
        String userIdHeader = requestUtil.getUserIdHeader();
        //获取当前登陆人机构
        Map<String, Object> role = xiAnData.getUserandOrg(userIdHeader);
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //生成pdf
        String fileName=date+"贫困户信息.pdf";
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        //查询当前信贷信息 借款人证件号码
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = statisticalAnalysisService.seleByIdCard(poorHouseholdsInformationDto.getIdCard());
        ArrayList<StatisticalAnalysisDto> list = new ArrayList<>();
        statisticalAnalysisDtos.forEach(statisticalAnalysisDto -> {
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getCreditAccountLogo())){
                if("1".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("是");
                }
                if("0".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("否");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getLoanQuality())){
                if("1".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("正常");
                }
                if("2".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("关注");
                }
                if("3".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("次级");
                }
                if("4".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("可疑");
                }
                if("5".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("损失");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getGuaranteeMethod())){
                if("1".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("信用");
                }
                if("2".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("保证");
                }
                if("3".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("质押");
                }
                if("4".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("抵押");
                }
                if("5".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("保证金");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getDiscountLoanSign())){
                if("1".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("是");
                }
                if("0".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("否");
                }
            }
            list.add(statisticalAnalysisDto);
        });

        //  根据证件号码和模块查询用户浏览PDF记录
        List<OperationLogDto> operationLogDtoList = operationLogService.selByIDCardAndOpName(poorHouseholdsInformationDto.getIdCard(), "信用报告查询",role.get("pbcCode").toString());
        new PdtUtils().generatePDF(outputStream,"人民银行乌鲁木齐中心支行信用报告", poorHouseholdsInformationDto,list,operationLogDtoList);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" +  fileName);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    @OptionalLog(module = "统计分析",operations = "信用报告查询")
    public RestfulApiResponse<PoorHouseholdsInformationDto> selectPoor(String id) {
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = poorHouseholdsInformationService.selectPoor(id, null);
        return RestfulApiResponse.success(poorHouseholdsInformationDto);
    }
    @Override
    public void selectPoorPDF(String id, String token, String pdfName, HttpServletResponse response, HttpServletRequest request) throws IOException, Exception {
        //获取当前登陆人
        String userIdHeader = requestUtil.getUserIdHeader();
        //获取当前登陆人机构
        Map<String, Object> role = xiAnData.getUserandOrg(userIdHeader);
        log.info("数据生成PDF开始");
        PoorHouseholdsInformationDto poorHouseholdsInformationDto = poorHouseholdsInformationService.selectPoor(id, null);
        if(poorHouseholdsInformationDto == null){
            log.error("当前贫困户信息为空，不能生成报告");
            return;
        }
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //生成pdf
        String fileName=date+"贫困户信息.pdf";//报告下载名称
        //处理各个浏览器下载乱码问题
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        //查询当前信贷信息 借款人证件号码
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = statisticalAnalysisService.seleByIdCard(poorHouseholdsInformationDto.getIdCard());
        ArrayList<StatisticalAnalysisDto> list = new ArrayList<>();
        statisticalAnalysisDtos.forEach(statisticalAnalysisDto -> {
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getCreditAccountLogo())){
                if("1".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("是");
                }
                if("0".equals(statisticalAnalysisDto.getCreditAccountLogo())){
                    statisticalAnalysisDto.setCreditAccountLogoName("否");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getLoanQuality())){
                if("1".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("正常");
                }
                if("2".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("关注");
                }
                if("3".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("次级");
                }
                if("4".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("可疑");
                }
                if("5".equals(statisticalAnalysisDto.getLoanQuality())){
                    statisticalAnalysisDto.setLoanQualityName("损失");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getGuaranteeMethod())){
                if("1".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("信用");
                }
                if("2".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("保证");
                }
                if("3".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("质押");
                }
                if("4".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("抵押");
                }
                if("5".equals(statisticalAnalysisDto.getGuaranteeMethod())){
                    statisticalAnalysisDto.setGuaranteeMethodName("保证金");
                }
            }
            if(!StringUtil.isEmpty(statisticalAnalysisDto.getDiscountLoanSign())){
                if("1".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("是");
                }
                if("0".equals(statisticalAnalysisDto.getDiscountLoanSign())){
                    statisticalAnalysisDto.setDiscountLoanSignName("否");
                }
            }
            list.add(statisticalAnalysisDto);
        });

        //  根据证件号码和模块查询用户浏览PDF记录
        String orgId=null;
        if(role != null && role.size() >0){
            orgId = role.get("pbcCode").toString();
        }
        List<OperationLogDto> operationLogDtoList = operationLogService.selByIDCardAndOpName(poorHouseholdsInformationDto.getIdCard(), "信用报告查询",orgId);
        new PdtUtils().generatePDF(outputStream,"人民银行乌鲁木齐中心支行信用报告", poorHouseholdsInformationDto,list,operationLogDtoList);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" +  fileName);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public RestfulApiResponse<Map<String, String>> selePoorByIdCard(List<String> creditLevels) {
        /**
         * TODO 虚拟数据
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
