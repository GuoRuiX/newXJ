package com.rjhc.credit.information.service.start.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjhc.credit.information.service.api.StatisticalAnalysisInterface;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.api.model.param.StatisticalAnalysisParam;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.credit.information.service.server.middleware.log.OptionalLog;
import com.rjhc.credit.information.service.server.service.StatisticalAnalysisService;
import com.rjhc.credit.information.service.server.service.UserService;
import com.rjhc.matrix.framework.core.response.RestfulApiResponse;
import com.rjhc.matrix.framework.core.util.DateUtils;
import com.rjhc.matrix.framework.core.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName StatisticalAnalysisController
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@RestController
@Slf4j
public class StatisticalAnalysisController implements StatisticalAnalysisInterface {
    @Autowired
    private StatisticalAnalysisService statisticalAnalysisService;
    @Autowired
    private GetXiAnData getXiAnData;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestUtil requestUtil;
    @Value("${filePath}")
    private String filePath;

    @OptionalLog(module = "????????????",operations = "??????????????????")
    @Override
    public RestfulApiResponse<List<StatisticalAnalysisDto>> selectFuzzy(StatisticalAnalysisParam statisticalAnalysisParam) throws Exception {
        Page<StatisticalAnalysisDto> statisticalAnalysisDtoPage = statisticalAnalysisService.selectFuzzy(statisticalAnalysisParam);
        return RestfulApiResponse.success(statisticalAnalysisDtoPage);
    }
    @OptionalLog(module = "????????????",operations = "????????????????????????")
    @Override
    public RestfulApiResponse<List<StatisticalAnalysisDto>> exportExcelStatistical(StatisticalAnalysisParam statisticalAnalysisParam, HttpServletResponse response, HttpServletRequest request) {
        log.info("??????????????????????????????");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");//????????????
        response.setCharacterEncoding("utf-8");//????????????
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            //????????????*/
            List<StatisticalAnalysisDto> statisticalAnalysisDtos = statisticalAnalysisService.selectAll(statisticalAnalysisParam);
        return RestfulApiResponse.success(statisticalAnalysisDtos);
    }
    @OptionalLog(module = "????????????",operations = "????????????????????????")
    @Override
    public RestfulApiResponse<List<PoorHouseholdsInformationDto>> importExcel(MultipartFile excelFile,HttpServletResponse response, HttpServletRequest request) throws IOException {
//        File file = new File("/Users/grx/rjhc/????????????2020-08-14.xlsx");
        InputStream inputStream = excelFile.getInputStream();
//        FileInputStream fileInputStream = new FileInputStream(excelFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        List<Object> read = EasyExcelFactory.read(bufferedInputStream, new Sheet(1, 1,StatisticalAnalysisDto.class));
        ArrayList<StatisticalAnalysisDto> statisticalAnalysisDtos = new ArrayList<>();
        read.forEach(object->{
            if (object instanceof StatisticalAnalysisDto) {
                StatisticalAnalysisDto statisticalAnalysisDto = (StatisticalAnalysisDto)object;
                statisticalAnalysisDtos.add(statisticalAnalysisDto);
            }
        });
        List<PoorHouseholdsInformationDto> poorHouseholdsInformationDtos = null;
        try {
            List<StatisticalAnalysisDto> statisticalAnalysisDtos1 = statisticalAnalysisService.dataStatistics(statisticalAnalysisDtos);
            statisticalAnalysisService.errorPoor(statisticalAnalysisDtos1,0);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestfulApiResponse.success(poorHouseholdsInformationDtos);
    }


    @OptionalLog(module = "????????????",operations = "????????????????????????")
    @Override
    public void downTemplate(HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");//????????????
        response.setHeader("content-type", "application/octet-stream");
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //            ??????????????????
        String path = this.getClass().getClassLoader().getResource("templates/????????????.xlsx").getPath();
        path = URLDecoder.decode(path, "UTF-8");


        //            ???????????????????????????????????????
        String agent = request.getHeader("User-Agent");
//??????????????????????????????????????????
        String realFilename = "";
        if (agent.contains("MSIE")) {
            // IE?????????
            realFilename = java.net.URLEncoder.encode("????????????.xlsx", "utf-8");
            realFilename = realFilename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // ??????????????????????????????java8
            realFilename = "=?utf-8?B?" + Base64.getEncoder().encodeToString(("????????????.xlsx").getBytes("utf-8")) + "?=";
        } else {
            // ???????????????
            realFilename = java.net.URLEncoder.encode("????????????.xlsx", "utf-8");
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + realFilename);
        FileInputStream excelFileInputStream = new FileInputStream(path);//??????????????????
        //            ??????Excel
        XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
        ServletOutputStream stream = response.getOutputStream();
        workbook.write(stream);
        excelFileInputStream.close();
        stream.flush();
        stream.close();


    }

    @Override
    public RestfulApiResponse<String> testTxt() throws Exception {
        File file = new File("/Users/grx/rjhc/agricultural_organizations.txt");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String read=null;
        int line=1;
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<StatisticalAnalysisDto> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((read=bufferedReader.readLine()) != null){
            StatisticalAnalysisDto dto = new StatisticalAnalysisDto();
            String s ="?????????"+line+":"+read;
            System.out.println("?????????"+line+":"+read);
            //???????????????????????????
            String[] split = read.split(",");
            if(split.length >=22){
                dto.setCreditLevel(split[10]);
            }
            dto.setBankCode(split[2]);
            dto.setBankName(split[3]);
            dto.setBankAddress(split[4]);
            dto.setPoorCode(split[5]);
            dto.setCustomerCode(split[6]);
            dto.setCustomerName(split[7]);
            dto.setCustomerIdCard(split[8]);
            dto.setCreditAccountLogo(split[9]);
            dto.setContractCode(split[11]);
            dto.setReceiptCode(split[12]);
            dto.setContractaMount(new BigDecimal(split[13]));
            dto.setLoanAmount(new BigDecimal(split[14]));
            dto.setLoanBalance(new BigDecimal(split[15]));
            dto.setLendingrate(new BigDecimal(split[16]));
            dto.setLoanDate(format.parse(split[17]));
            dto.setMaturityDate(format.parse(split[18]));
            dto.setLoanPurpose(split[19]);
            dto.setLoanVarietie(split[20]);
            dto.setLoanQuality(split[21]);
            dto.setGuaranteeMethod(split[22]);
            dto.setDiscountLoanSign(split[23]);
            dto.setDiscountRatio(new BigDecimal(split[24]));
            list.add(dto);
            strings.add(s);
            line++;
        }
        List<StatisticalAnalysisDto> statisticalAnalysisDtos = statisticalAnalysisService.dataStatistics(list);
        statisticalAnalysisService.errorPoor(statisticalAnalysisDtos,0);
        return RestfulApiResponse.success();
    }
    @OptionalLog(module = "????????????",operations = "??????????????????")
    @Override
    public RestfulApiResponse<String> testImport(MultipartFile excelFile, HttpServletResponse response, HttpServletRequest request) throws Exception {
        //????????????????????????
        File file = new File(filePath);
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //?????????????????????????????????????????????
        if(!file.exists()){
            file.mkdir();
            log.info("????????????????????????"+file.getPath());
        }
        //???????????????????????????
        InputStream inputStream = excelFile.getInputStream();

        //????????????????????????
        String originalFilename = excelFile.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        /**
         *  ????????????????????????????????????ID
         */
        //????????????
        InputStream inputStream1 = excelFile.getInputStream();
        String url = ex( date, substring,request.getHeader("toKen"));
        FileOutputStream fos = new FileOutputStream(url);
        byte[] buffer = new byte[1024 * 1024];
        int len=0;
        while ((len = inputStream1.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
        inputStream.close();
        return RestfulApiResponse.success();
    }

    @Override
    public RestfulApiResponse<List<Map<String, String>>> selByLevel() {
        return RestfulApiResponse.success(statisticalAnalysisService.selByLevel());
    }


    public String ex(String date,String substring,String token) throws Exception {
        //???????????????????????????
        int i = RandomUtil.randomInt(1, 100000);
        String orgId=null;
        //???????????????????????????????????????id
        String userIdHeader = requestUtil.getUserIdHeader();
        Map<String, Object> userandOrg = getXiAnData.getUserandOrg(userIdHeader);
        if(userandOrg != null && userandOrg.size() > 0){
            orgId=userandOrg.get("pbcCode").toString();
        }else {
            orgId=String.valueOf(i);
        }
        String url= filePath+"/"+System.currentTimeMillis()+"_"+orgId+"."+substring;
        return  url;
    }


}
