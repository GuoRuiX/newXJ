package com.rjhc.credit.information.service.server.middleware;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.server.common.convertor.PoorHouseholdsInformationConvertor;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;
import com.rjhc.credit.information.service.server.dao.mapper.WaringPoorMapper;
import com.rjhc.credit.information.service.server.service.StatisticalAnalysisService;
import com.rjhc.credit.information.service.server.service.WaringPoorService;
import com.rjhc.matrix.framework.core.util.DateUtils;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName scheduledDateAsync
 * @Description: TODO
 * @Author grx
 * @Date 2020/10/13
 * @Version V1.0
 **/
@Slf4j
@Component
@EnableAsync
public class scheduledDateAsync {
    @Autowired
    private StatisticalAnalysisService statisticalAnalysisService;
    @Resource
    private WaringPoorService waringPoorService;



    @Value("${filePath}")
    private String filePath;
    @Async
    public void scheduledDate() throws Exception{
        //格式化时间格式
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<String> errorList = new ArrayList<>();
        File path = new File(filePath);
        //判断当前文件夹不存在，接着跳出
        if(!path.exists()){
            log.error("当前文件夹不存在");
            return;
        }
        //接受存储完库的数据
        List<StatisticalAnalysisDto> staList= new ArrayList<>();
        int countFile=0;
        if(path!= null){
            String[] list = path.list();
            for (String s : list) {
                countFile ++;
                //判断文件后缀获取文件数据
                String substring = s.substring(s.lastIndexOf(".") + 1);
                log.info("获取到的文件名"+ s.substring(s.lastIndexOf("\\")+1));
                //获取文件名称的机构编码
                String substring1 = s.substring(0, s.lastIndexOf("."));
                String[] s1 = substring1.split("_");
                File file = new File(path+"/"+s);
                FileInputStream fileInputStream = new FileInputStream(file);
                //判断当前文件类型为excel表格
                if(substring.equals("xlsx") || substring.equals("xls")){
                    /**
                     * TODO
                     */

                    //开始处理表格数据
                    List<Object> read = EasyExcelFactory.read(new BufferedInputStream(fileInputStream), new Sheet(1, 1, StatisticalAnalysisDto.class));
                    ArrayList<StatisticalAnalysisDto> statisticalAnalysisDtos = new ArrayList<>();
                    int i=0;
                    int errorI=0;
                    //获取当前文件的月份
                    HashMap<String, String> mapMonth = new HashMap<>();
                    mapMonth.put("uploadBank",s1[1]);
                    for (Object o : read) {
                        i++;
//                        try {
                            if (o instanceof StatisticalAnalysisDto) {
                                HashMap<String, String> map = new HashMap<>();
                                StatisticalAnalysisDto statisticalAnalysisDto = (StatisticalAnalysisDto)o;

/*                                if(statisticalAnalysisDto.getReceiveloanDate() != null){
                                    statisticalAnalysisDto.setLoanDate(format1.parse(statisticalAnalysisDto.getReceiveloanDate().replaceAll("/","-")));
                                }
                                if(statisticalAnalysisDto.getReceivematurityDate() != null){
                                    statisticalAnalysisDto.setMaturityDate(format1.parse(statisticalAnalysisDto.getReceivematurityDate().replaceAll("/","-")));
                                }*/
                                   statisticalAnalysisDto.setUploadOrganization(s1[1]);
                                map.put("","");
                                if (validExcel(i,statisticalAnalysisDto,errorList)){
                                    statisticalAnalysisDtos.add(statisticalAnalysisDto);
                                    if(StringUtils.isEmpty(mapMonth.get("month"))){
                                        mapMonth.put("month",statisticalAnalysisDto.getMonth());
                                        //并删除数据
                                        statisticalAnalysisService.delByUploadBankAndMonth(mapMonth.get("uploadBank"),statisticalAnalysisDto.getMonth());
                                    }

                                } else {
                                    errorI++;
                                }
                            }
                       /* }catch (Exception e){
                            errorI++;
                            log.error("["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 当前excel文件有误，请修改重新导入："+file.getPath()+"：第"+i+"行");
                            String error="["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 当前excel文件有误，请修改重新导入："+file.getPath()+"：第"+i+"行";
                            errorList.add(error);
                            continue;
                        }*/
                    }
                    //判断当前有报错信息，不处理之前数据
                    if(errorI ==0){
                        List<StatisticalAnalysisDto> statisticalAnalysisDtos1 = statisticalAnalysisService.dataStatistics(statisticalAnalysisDtos);
                        statisticalAnalysisService.errorPoor(statisticalAnalysisDtos,errorI);
                        //删除文件
                        boolean delete = file.delete();
                    }else {
                        statisticalAnalysisService.delByUploadBankAndMonth(mapMonth.get("uploadBank"),mapMonth.get("month"));
                        log.error("当前有报错信息，不做处理："+errorI);
                    }

                    //判断当前文件为txt文件
                }else if(substring.equals("txt") && !substring1.equals("weeor")){
                    List<StatisticalAnalysisDto> statisticalAnalysisDtos = null;
                    /**
                     * TODO
                     */
                    //开始处理txt文档
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    log.info("开始读取文件:"+s);
                    String read=null;
                    int line=1;
                    ArrayList<String> strings = new ArrayList<>();
                    ArrayList<StatisticalAnalysisDto> list1 = new ArrayList<>();
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    HashMap<String, String> mapMonth = new HashMap<>();
                    mapMonth.put("uploadBank",s1[1]);
                    int errorI=0;
                        try {

                            while ((read=bufferedReader.readLine()) != null){
                                StatisticalAnalysisDto dto = new StatisticalAnalysisDto();
                                //使用分隔符分割数据
                                String s2 = read.replaceAll("\t", "");
                                String[] split = s2.split("\\|");

                                if(split.length >=22){
                                    dto.setCreditLevel(split[11]);
                                }
                                dto.setUploadOrganization(s1[1]);
                                dto.setYears(split[0]);
                                dto.setMonth(split[1]);
                                if(StringUtils.isEmpty(mapMonth.get("month"))){
                                    mapMonth.put("month",dto.getMonth());
                                    //并删除数据
                                    statisticalAnalysisService.delByUploadBankAndMonth(mapMonth.get("uploadBank"),dto.getMonth());
                                }
                                dto.setBankCode(split[2]);
                                dto.setBankName(split[3]);
                                dto.setBankAddressCode(split[4]);
                                dto.setBankAddress(split[5]);
                                dto.setPoorCode(split[6]);
                                dto.setCustomerCode(split[7]);
                                dto.setCustomerName(split[8]);
                                dto.setCustomerIdCard(split[9]);
                                dto.setCreditAccountLogo(split[10]);
                                dto.setContractCode(split[12]);
                                dto.setReceiptCode(split[13]);
                                dto.setContractaMount(new BigDecimal(split[14]));
                                dto.setLoanAmount(new BigDecimal(split[15]));
                                dto.setLoanBalance(new BigDecimal(split[16]));
                                dto.setLendingrate(new BigDecimal(split[17]));
                                dto.setLoanDate(format.parse(split[18]));
                                dto.setMaturityDate(format.parse(split[19]));
                                dto.setLoanPurpose(split[20]);
                                dto.setLoanVarietie(split[21]);
                                dto.setLoanQuality(split[22]);
                                dto.setGuaranteeMethod(split[23]);
                                dto.setDiscountLoanSign(split[24]);
                                dto.setDiscountRatio(new BigDecimal(split[25]));
                                dto.setType(String.valueOf(countFile));
                                /*dto.setUploadOrganization(s3[1]);*/
                                if (validExcel(line,dto,errorList)){
                                    list1.add(dto);
                                } else {
                                    errorI++;
                                    log.error("当前文件有错误，停止循环");
                                    list1.clear();
                                    break;
                                }
                                if(line % 50000 == 0){
                                    statisticalAnalysisDtos = statisticalAnalysisService.dataStatistics(list1);
                                    System.out.println(line);
                                    statisticalAnalysisService.errorPoor(statisticalAnalysisDtos,errorI);
                                    list1.clear();
                                }
                                line++;
                            }
                            if(list1.size() > 0 ){
                                statisticalAnalysisDtos = statisticalAnalysisService.dataStatistics(list1);
                                statisticalAnalysisService.errorPoor(statisticalAnalysisDtos,errorI);
                            }
                            log.error("错误数量为"+errorI);
                        } catch (Exception e){
                            String error="["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 当前"+s+"文件格式错误，请及时处理："+file.getPath()+"第"+line+"行";
                            errorList.add(error);
                            statisticalAnalysisService.delByUploadBankAndMonth(mapMonth.get("uploadBank"),mapMonth.get("month"));
                            log.error("当前"+s+"文件有误，跳出当前文件的循环");
                            continue;
                        }

                        if(errorI >0){
                            statisticalAnalysisService.delByUploadBankAndMonth(mapMonth.get("uploadBank"),mapMonth.get("month"));
                            System.out.println("当前有错误信息，跳出当前文件循环");
                            continue;
                        }else {
                            log.info("当前文件信息存储成功，删除文件");
                            file.delete();
                        }

                }else {
//                   boolean delete = file.delete();
                    log.error("当前文件格式不正确无法读取："+s);
                }
                //实现错误日志输出
                FileWriter writer = new FileWriter(filePath + "/error.log", true);
                for (String err : errorList) {
                    writer.write(err+"\n");
                }
                writer.close();



            }
        }
    }

    /**
     * 校验Excel数据
     *
     * @param line 当前行
     * @param analysisDto 当前行对象
     * @return
     */
    public boolean validExcel(int line, StatisticalAnalysisDto analysisDto,ArrayList<String> errorList){
        boolean flag = true;
        if (analysisDto == null){
            return false;
        }
        if (StringUtils.isBlank(analysisDto.getYears())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 年份不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getMonth())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 月份不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getBankCode())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 机构编码不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getBankName())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款银行详细名称不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getBankAddressCode())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 金融机构地区编码不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getBankAddress())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 金融机构地区不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getCustomerCode())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 客户编号不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getCustomerName())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 借款人姓名不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getCustomerIdCard())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 证件号码不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getReceiptCode())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款合同借据编码不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (analysisDto.getContractaMount() == null){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款合同金额不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (analysisDto.getLoanAmount() == null){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 借据金额不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (analysisDto.getLoanBalance() == null){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 借据余额不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (analysisDto.getLendingrate() == null){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款利率不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (analysisDto.getLoanDate() == null){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款发放日期不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (analysisDto.getMaturityDate() == null){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款到期日期不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getLoanPurpose())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款用途不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getLoanVarietie())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款品种不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getLoanQuality())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贷款质量不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getGuaranteeMethod())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 担保方式不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        if (StringUtils.isBlank(analysisDto.getDiscountLoanSign())){
            String msg = "["+ DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") +"] 贴息贷款标志不能为空！第"+line+"行";
            log.error(msg);
            errorList.add(msg);
            flag = false;
        }
        return flag;
    }
}
