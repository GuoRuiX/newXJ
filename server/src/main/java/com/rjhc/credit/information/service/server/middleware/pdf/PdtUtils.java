package com.rjhc.credit.information.service.server.middleware.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.rjhc.credit.information.service.api.model.dto.OperationLogDto;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.matrix.framework.core.util.DateUtils;
import com.rjhc.matrix.framework.core.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName PdfUtiles
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/19
 * @Version V1.0
 **/
public class PdtUtils {
    //定义全局的字体静态变量
    private static Font titleFont;
    private static Font headFont;
    private static Font keyFont;
    private static Font textFont;
    private static Font textFonts;
    private static Font titleHeadFont;
    //最大宽度
    private static int maxWidth=520;
    static {
        try {
            //不同字体(这里定义为同一种字体：包含不同字号，不同style)
            BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            titleFont = new Font(font, 22, Font.BOLD);
            headFont = new Font(font, 20, Font.BOLD);
            titleHeadFont = new Font(font, 14, Font.BOLD);
            keyFont = new Font(font, 10,Font.BOLD);
            textFont = new Font(font, 10);
            textFonts = new Font(font, 12);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：
     * 〈生成PDF〉
     * @Author: grx   人民银行乌鲁木齐中心支行信用报告
     * @Date: 1:38 下午 2020/8/19
     * @param
     * @return: void
     */
    public void generatePDF(BufferedOutputStream outputStream, String title, PoorHouseholdsInformationDto poorHouseholdsInformationDto, List<StatisticalAnalysisDto> list,List<OperationLogDto> operationLogDtoList) throws Exception {
        //新建document对象
        Document document = new Document(PageSize.A4);
        //格式化日期
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
       /* File file = new File("/Users/grx/rjhc/test.pdf");
        file.createNewFile();*/
        PdfWriter writer = PdfWriter.getInstance(document, outputStream );
        // writer.setPageEvent();//水印
        writer.setPageEvent(new MyHeaderFooter());//页眉/页脚


        document.open();//打开文档
        document.addTitle("人民银行乌鲁木齐中心支行信用报告");
       //设置标题 总标题
        textStyle(document," ",titleFont,0,0,0,0,0,0,30);
        textStyle(document,"人民银行乌鲁木齐中心支行",titleFont,1,12,12,24,20,5,25);
        textStyle(document,"信用报告",titleFont,1,12,12,24,20,5,300);




        //设置报告编码和日期
        //表格

        PdfPTable pdfCode = createTable1(2,new float[]{240, 280});
        pdfCode.addCell(createCell2("报告编码",textFont,30));
        pdfCode.addCell(createCell2(poorHouseholdsInformationDto.getId(),textFont,30));
        pdfCode.addCell(createCell2("报告日期", textFont,30));
        pdfCode.addCell(createCell2(date, textFont,30));
        document.add(pdfCode);


        //设置标题
        textStyle(document," ",titleFont,0,0,0,0,0,0,30);
        textStyle(document,"人民银行乌鲁木齐中心支行",titleFont,1,12,12,24,20,5,0);

        //换到新的一页
        document.newPage();
        //设置标题
        textStyle(document," ",titleFont,0,0,0,0,0,0,10);
        textStyle(document,"报告说明",titleFont,1,12,12,24,20,5,0);
        //设置报告说明内容
        String text="本报告内容是人民银行乌鲁木齐中心支行接受您的委托，通过查询公开信息所得结果。报告中使用的各项信息来源合法，报告只对数据源的数据进行客观收集、整理，人民银行乌鲁木齐中心支行在信息汇总、加工、整合的全过程中处于客观、中立的第三方地位，人民银行乌鲁木齐中心支行不对信息源提供的信息进行真实性、准确性进行一一核实，不对该查询结果的全面、准确、真实性负责，亦对数据源的数据内容本身不承担任何法律责任，本机构依此出具的信用报告，供使用者参考。";
        textStyle(document," ",textFonts,0,0,0,0,0,0,25);
        textStyle(document,text,textFonts,0,12,12,24,20,5,0);





        //换到新的一页
        document.newPage();

        //设置标题
        textStyle(document," ",headFont,1,0,0,0,0,0,10);
        textStyle(document,"贫困户信息",headFont,1,0,0,0,20,0,0);
        textStyle(document," ",headFont,1,0,0,0,0,0,0);


        //贫困户信息报告生成
        createPoor(document,poorHouseholdsInformationDto);
        //
        //循环拼接文档 借贷信息报告生成
        if(list.size() != 0 && list != null){
            textStyle(document," ",headFont,1,0,0,0,0,0,10);
            textStyle(document,"借贷信息",headFont,1,0,0,0,20,0,0);
            textStyle(document," ",headFont,1,0,0,0,0,0,0);
            for (StatisticalAnalysisDto statisticalAnalysisDto : list) {
                createStat(document,statisticalAnalysisDto);
            }
        }
        //生成 浏览记录的PDF
        createLog(document,operationLogDtoList);
        //关闭文档
        document.close();
    }

    /**
     * 功能描述：
     * 〈公共标题样式〉
     * @Author: grx
     * @Date: 10:37 上午 2020/8/31
     * @param document PDF文档
     * @param title  标题
     * @param font  字体
     * @param textFormat 设置文字居中 0靠左 1居中 2靠右
     * @param left 设置左缩进
     * @param right 靠右缩进
     * @param textIndent 首行缩进
     * @param rowSpacing 行间距
     * @param blankOnParagraph 设置段落上空白
     * @param spaceUnderParagraph 设置段落下空白
     * @return: void
     */
    public void textStyle(Document document,String title,Font font,Integer textFormat,float left,float right,float textIndent,float rowSpacing,float blankOnParagraph,float spaceUnderParagraph) throws Exception {
        Paragraph paragraph = new Paragraph(title, font);
        paragraph.setAlignment(textFormat);
        paragraph.setIndentationLeft(left);
        paragraph.setIndentationRight(right);
        paragraph.setFirstLineIndent(textIndent);
        paragraph.setLeading(rowSpacing);
        paragraph.setSpacingBefore(blankOnParagraph);
        paragraph.setSpacingAfter(spaceUnderParagraph);
        document.add(paragraph);
    }

    /**
     * 功能描述：
     * 〈贫困户信息报告生成〉
     * @Author: grx
     * @Date: 11:01 上午 2020/8/31
     * @param document
     * @param poorHouseholdsInformationDto 贫困户信息
     * @return: void
     */
    public void createPoor(Document document,PoorHouseholdsInformationDto poorHouseholdsInformationDto) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        //设置贫困户信息的第一行表格标题
        PdfPTable pdf1 = createTable1(2,new float[]{320,200});
        pdf1.addCell(createCell("数据来源：新疆维吾尔自治区扶贫开发办公室", keyFont, Element.ALIGN_LEFT, 1, false));
        pdf1.addCell(createCell("更新时间: "+DateUtils.formatDate(poorHouseholdsInformationDto.getSynchronizationDate() , "yyyy-MM-dd HH:mm:ss"), keyFont, Element.ALIGN_RIGHT, 1, false));
        document.add(pdf1);//设置标题文字样式
        PdfPTable pdf6 = createTable1(1,new float[]{520});
        pdf6.addCell(createCell2("基础信息",titleHeadFont,30));
        document.add(pdf6);//设置标题文字样式
        PdfPTable pdfCode2 = createTable1(4,new float[]{120,140,120,140});
        pdfCode2.addCell(createCell("户主姓名",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getHouseholderName(),textFont,20));
        pdfCode2.addCell(createCell("性别",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getGender(),textFont,20));
        pdfCode2.addCell(createCell("民族",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getNation(),textFont,20));
        pdfCode2.addCell(createCell("年龄",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getAge().toString(),textFont,20));
        pdfCode2.addCell(createCell("证件类型",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getIdCardType(),textFont,20));
        pdfCode2.addCell(createCell("证件号码",keyFont,20));
        //证件号码脱敏处理
        String concat = null;
        if(!StringUtil.isEmpty(poorHouseholdsInformationDto.getIdCard())){
            concat = StringUtils.left(poorHouseholdsInformationDto.getIdCard(), 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(poorHouseholdsInformationDto.getIdCard(), 3), StringUtils.length(poorHouseholdsInformationDto.getIdCard()), "*"), "******"));
        }
        pdfCode2.addCell(createCell(concat,textFont,20));
        pdfCode2.addCell(createCell("地州",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getPrefecture(),textFont,20));
        pdfCode2.addCell(createCell("县市",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getCounties(),textFont,20));
        pdfCode2.addCell(createCell("乡镇",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getTownship(),textFont,20));
        pdfCode2.addCell(createCell("行政村",keyFont,20));
        pdfCode2.addCell(createCell(poorHouseholdsInformationDto.getAdministrativeVillage(),textFont,20));
        document.add(pdfCode2);//设置标题文字样式
        PdfPTable pdfCode3 = createTable1(2,new float[]{120,400});
        pdfCode3.addCell(createCell("家庭住址",keyFont,20));
        pdfCode3.addCell(createCell(poorHouseholdsInformationDto.getAddress(),textFont,20));
        document.add(pdfCode3);//设置标题文字样式
        PdfPTable pdfCode4 = createTable1(4,new float[]{120,140,120,140});
        pdfCode4.addCell(createCell("贫困户编号",keyFont,20));
        pdfCode4.addCell(createCell(poorHouseholdsInformationDto.getPoorHouseholdsCode(),textFont,20));
        pdfCode4.addCell(createCell("贫困人口编号",keyFont,20));
        pdfCode4.addCell(createCell(poorHouseholdsInformationDto.getPopulationCode(),textFont,20));
        pdfCode4.addCell(createCell("贫困户属性",keyFont,20));
        pdfCode4.addCell(createCell(poorHouseholdsInformationDto.getPoorHouseholdsAttribute(),textFont,20));
        pdfCode4.addCell(createCell("与户主关系",keyFont,20));
        pdfCode4.addCell(createCell(poorHouseholdsInformationDto.getRelationShipName(),textFont,20));
        pdfCode4.addCell(createCell("脱贫标志",keyFont,20));
        pdfCode4.addCell(createCell(poorHouseholdsInformationDto.getPovertySign(),textFont,20));
        pdfCode4.addCell(createCell("家庭人口数",keyFont,20));
        pdfCode4.addCell(createCell(poorHouseholdsInformationDto.getFamilySize().toString(),textFont,20));
        document.add(pdfCode4);//设置标题文字样式
        /*PdfPTable pdfCode5 = createTable1(2,new float[]{120,400});
        pdfCode5.addCell(createCell("健康状况",keyFont,20));
        pdfCode5.addCell(createCell(poorHouseholdsInformationDto.getHealth(),textFont,20));
        document.add(pdfCode5);//设置标题文字样式*/
        PdfPTable pdf7 = createTable1(1,new float[]{520});
        pdf7.addCell(createCell2("生产生活条件",titleHeadFont,30));
        document.add(pdf7);//设置标题文字样式
        PdfPTable pdf8 = createTable1(4,new float[]{120,140,120,140});
        pdf8.addCell(createCell("耕地面积（亩）",keyFont,30));
        pdf8.addCell(createCell(decimalFormat.format(poorHouseholdsInformationDto.getLandarea()),textFont,30));
        pdf8.addCell(createCell("林地面积（亩）",keyFont,30));
        pdf8.addCell(createCell(decimalFormat.format(poorHouseholdsInformationDto.getWoodlandarea()),textFont,30));
        document.add(pdf8);
        PdfPTable pdf9 = createTable1(2,new float[]{120,400});
        pdf9.addCell(createCell("人均纯收入 (元）",keyFont,30));
        pdf9.addCell(createCell(decimalFormat.format(poorHouseholdsInformationDto.getIncome()),textFont,30));
        document.add(pdf9);
    }

    /**
     * 功能描述：
     * 〈报告查询日志的表格生成〉
     * @Author: grx
     * @Date: 10:59 上午 2020/8/31
     * @param document
     * @param list 日志数据
     * @return: void
     */
    public void createLog(Document document,List<OperationLogDto> list) throws Exception {
        textStyle(document," ",headFont,1,0,0,0,0,0,10);
        textStyle(document,"报告查询日志",headFont,1,0,0,0,20,0,0);
        textStyle(document," ",headFont,1,0,0,0,0,0,20);

        PdfPTable pdfCode = createTable1(4,new float[]{100,220,100,100});
        pdfCode.addCell(createCell1("查询人员",keyFont));
        pdfCode.addCell(createCell1("查询机构",keyFont));
        pdfCode.addCell(createCell1("操作名称",keyFont));
        pdfCode.addCell(createCell1("查询日期",keyFont));
        list.forEach(operationLogDto -> {
            pdfCode.addCell(createCell1(operationLogDto.getUserName(),textFont));
            //根据当亲机构id查询当前机构名称
            pdfCode.addCell(createCell1(operationLogDto.getBanekName(),textFont));
            pdfCode.addCell(createCell1(operationLogDto.getOperationName(),textFont));
            pdfCode.addCell(createCell1(DateUtils.formatDate(operationLogDto.getOperationDate(),"yyyy-MM-dd HH:mm:ss"),textFont));
        });
        document.add(pdfCode);
    }

    /**
     * 功能描述：
     * 〈生成借贷信息表格〉
     * @Author: grx
     * @Date: 11:09 上午 2020/8/25
     * @param statisticalAnalysisDto 借贷信息数据
     * @return: void
     */
    public void createStat(Document document,StatisticalAnalysisDto statisticalAnalysisDto) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        PdfPTable pdf1 = createTable1(2,new float[]{320,200});
        pdf1.addCell(createCell("数据来源："+statisticalAnalysisDto.getBankName(), keyFont, Element.ALIGN_LEFT, 1, false));
        pdf1.addCell(createCell("更新时间："+DateUtils.formatDate(statisticalAnalysisDto.getSynchronizationDate() , "yyyy-MM-dd HH:mm:ss"), keyFont, Element.ALIGN_RIGHT, 1, false));
        document.add(pdf1);//设置标题文字样式
        PdfPTable pdfCode2 = createTable1(4,new float[]{120,140,120,140});
        pdfCode2.addCell(createCell("贷款银行",keyFont,20));
        pdfCode2.addCell(createCell(statisticalAnalysisDto.getBankName(),textFont,20));
        pdfCode2.addCell(createCell("机构编码",keyFont,20));
        pdfCode2.addCell(createCell(statisticalAnalysisDto.getBankCode(),textFont,20));
        document.add(pdfCode2);//设置标题文字样式
        PdfPTable pdf9 = createTable1(2,new float[]{120,400});
        pdf9.addCell(createCell("金融机构地区",keyFont,20));
        pdf9.addCell(createCell(statisticalAnalysisDto.getBankAddress(),textFont,20));
        document.add(pdf9);//设置标题文字样式
        PdfPTable pdfCode3 = createTable1(4,new float[]{120,140,120,140});
        pdfCode3.addCell(createCell("贫困户户籍号码",keyFont,20));
        pdfCode3.addCell(createCell(statisticalAnalysisDto.getPoorCode(),textFont,20));
        pdfCode3.addCell(createCell("客户编号",keyFont,20));
        pdfCode3.addCell(createCell(statisticalAnalysisDto.getCustomerCode(),textFont,20));
        pdfCode3.addCell(createCell("借款人姓名",keyFont,20));
        pdfCode3.addCell(createCell(statisticalAnalysisDto.getCustomerName(),textFont,20));
        pdfCode3.addCell(createCell("证件号码",keyFont,20));
        //证件号码脱敏处理
        String concat = null;
        if(!StringUtil.isEmpty(statisticalAnalysisDto.getCustomerIdCard())){
             concat = StringUtils.left(statisticalAnalysisDto.getCustomerIdCard(), 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(statisticalAnalysisDto.getCustomerIdCard(), 3), StringUtils.length(statisticalAnalysisDto.getCustomerIdCard()), "*"), "******"));
        }
        pdfCode3.addCell(createCell(concat,textFont,20));
        document.add(pdfCode3);//设置标题文字样式
        PdfPTable pdf13 = createTable1(2,new float[]{120,400});
        pdf13.addCell(createCell("内部信用评级等级",keyFont,20));
        pdf13.addCell(createCell(statisticalAnalysisDto.getCreditLevel(),textFont,20));
        document.add(pdf13);//设置标题文字样式



        PdfPTable pdf14 = createTable1(4,new float[]{120,140,120,140});
        pdf14.addCell(createCell("信用户标志",keyFont,20));
        pdf14.addCell(createCell(statisticalAnalysisDto.getCreditAccountLogoName(),textFont,20));
        pdf14.addCell(createCell("贷款合同编码",keyFont,20));
        pdf14.addCell(createCell(statisticalAnalysisDto.getContractCode(),textFont,20));
        pdf14.addCell(createCell("贷款合同借据编码",keyFont,20));
        pdf14.addCell(createCell(statisticalAnalysisDto.getReceiptCode(),textFont,20));
        pdf14.addCell(createCell("贷款合同金额(万元)",keyFont,20));
        pdf14.addCell(createCell(decimalFormat.format(statisticalAnalysisDto.getContractaMount()),textFont,20));
        pdf14.addCell(createCell("借据金额(万元）",keyFont,20));
        pdf14.addCell(createCell(decimalFormat.format(statisticalAnalysisDto.getLoanAmount()),textFont,20));
        pdf14.addCell(createCell("借据余额(万元）",keyFont,20));
        pdf14.addCell(createCell(decimalFormat.format(statisticalAnalysisDto.getLoanBalance()),textFont,20));
        pdf14.addCell(createCell("贷款利率（%）",keyFont,20));
        pdf14.addCell(createCell(decimalFormat.format(statisticalAnalysisDto.getLendingrate()),textFont,20));
        pdf14.addCell(createCell("贷款发放日期",keyFont,20));
        pdf14.addCell(createCell(DateUtils.formatDate(statisticalAnalysisDto.getLoanDate(), "yyyy-MM-dd"),textFont,20));
        pdf14.addCell(createCell("贷款到期日期",keyFont,20));
        pdf14.addCell(createCell(DateUtils.formatDate(statisticalAnalysisDto.getMaturityDate(), "yyyy-MM-dd"),textFont,20));
        pdf14.addCell(createCell("贷款质量",keyFont,20));
        pdf14.addCell(createCell(statisticalAnalysisDto.getLoanQualityName(),textFont,20));
        document.add(pdf14);//设置标题文字样式

        PdfPTable pdf10 = createTable1(2,new float[]{120,400});
        pdf10.addCell(createCell("贷款用途",keyFont,20));
        pdf10.addCell(createCell(statisticalAnalysisDto.getLoanPurpose(),textFont,20));
        document.add(pdf10);//设置标题文字样式
        PdfPTable pdfCode4 = createTable1(4,new float[]{120,140,120,140});
        pdfCode4.addCell(createCell("贷款品种",keyFont,20));
        pdfCode4.addCell(createCell(statisticalAnalysisDto.getLoanVarietie(),textFont,20));
        pdfCode4.addCell(createCell("担保方式",keyFont,20));
        pdfCode4.addCell(createCell(statisticalAnalysisDto.getGuaranteeMethodName(),textFont,20));
        pdfCode4.addCell(createCell("贴息贷款标志",keyFont,20));
        pdfCode4.addCell(createCell(statisticalAnalysisDto.getDiscountLoanSignName(),textFont,20));
        pdfCode4.addCell(createCell("贴息比例",keyFont,20));
        pdfCode4.addCell(createCell(statisticalAnalysisDto.getDiscountRatio().toString(),textFont,20));
        document.add(pdfCode4);//设置标题文字样式
    }

    public PdfPTable createTable1(Integer num,float[] widths) throws Exception {
        PdfPTable table = new PdfPTable(num);
        table.setWidths(widths);
        table.setTotalWidth(maxWidth);
        table.setLockedWidth(false);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(1);
        return table;
    }
    /**
     * 创建单元格(指定字体) 文本格式为垂直居中，水平靠左
     * @param value 内容
     * @param font 字体
     * @param height 高度 单位是行
     * @return
     */
    public PdfPCell createCell(String value, Font font, Integer height) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);//水平靠左
        cell.setFixedHeight(height);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 功能描述：
     * 〈创建cell表格时格式为垂直居中，水平居中，并且不设置高度 超过长度会自动换行〉
     * @Author: grx
     * @Date: 11:07 上午 2020/8/31
     * @param value 内容
     * @param font 字体
     * @return: com.itextpdf.text.pdf.PdfPCell
     */
    public PdfPCell createCell1(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 功能描述：
     * 〈设置高度，垂直居中，水平居中，并且设置高度换行只会换设置的高度行，超过将不显示〉
     * @Author: grx
     * @Date: 11:09 上午 2020/8/31
     * @param value
     * @param font
     * @param height
     * @return: com.itextpdf.text.pdf.PdfPCell
     */
    public PdfPCell createCell2(String value, Font font, Integer height) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cell.setFixedHeight(height);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 功能描述：
     * 〈设置表格上面的标题，取消表格边框线〉
     * @Author: grx
     * @Date: 11:10 上午 2020/8/31
     * @param value
     * @param font
     * @param align
     * @param colspan 表示占用表格一行的多少个框
     * @param boderFlag 表示是否设置上边距和下边距
     * @return: com.itextpdf.text.pdf.PdfPCell
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;


    }

}
