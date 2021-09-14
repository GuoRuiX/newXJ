package com.rjhc.credit.information.service.server.middleware.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @ClassName MyHeaderFooter
 * @Description: 页眉设置
 * @Author grx
 * @Date 2020/8/19
 * @Version V1.0
 **/
@Slf4j
public class MyHeaderFooter extends PdfPageEventHelper {
    PdfTemplate totalPage;//总页数
    Font hfFont;
    {
        try {
            hfFont = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 8, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：
     * 〈打开文档是创建一个总页数的模版〉
     * @Author: grx
     * @Date: 1:16 下午 2020/8/19
     * @param writer
     * @param document
     * @return: void
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document){
        log.info("打开文档时创建一个总页数的模版");
        PdfContentByte cb = writer.getDirectContent();
        totalPage = cb.createTemplate(30, 16);
    }
    /**
     * 功能描述：
     * 〈一页加载完成出发，写入页眉和页脚〉
     * @Author: grx
     * @Date: 1:18 下午 2020/8/19
     * @param writer
     * @param document
     * @return: void
     */
    @SneakyThrows
    @Override
    public void onEndPage(PdfWriter writer,Document document){
        log.info("一页加载完成出发，写入页眉和页脚");
        int pageNumber = document.getPageNumber();
        if(pageNumber == 1){
            return;
        }
        PdfPTable table = new PdfPTable(3);
        table.setTotalWidth(PageSize.A4.getWidth()-100);
        table.setWidths(new int[]{24,24,3});
        table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(-10);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
        table.addCell(new Paragraph("人民银行乌鲁木齐中心支行",hfFont));//以直接使用addCell(str)，不过不能指定字体，中文无法显示
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(new Paragraph("第"+(writer.getPageNumber()-1)+"页/",hfFont));
        //总页数
        PdfPCell cell = new PdfPCell(Image.getInstance(totalPage));
        cell.setBorder(Rectangle.BOTTOM);
        table.addCell(cell);
        //将页眉写到document中，位置可以指定，指定到下面就是页脚
        table.writeSelectedRows(0,-1,50,PageSize.A4.getHeight()-20,writer.getDirectContent());
    }
    /**
     * 功能描述：
     * 〈全部完成后将总页数的pdf模版写到指定位置〉
     * @Author: grx
     * @Date: 1:27 下午 2020/8/19
     * @param writer
     * @param document
     * @return: void
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document){
        String text="总"+(writer.getPageNumber()-1)+"页";
        ColumnText.showTextAligned(totalPage,Element.ALIGN_LEFT,new Paragraph(text,hfFont),2,2,1);
    }




}
