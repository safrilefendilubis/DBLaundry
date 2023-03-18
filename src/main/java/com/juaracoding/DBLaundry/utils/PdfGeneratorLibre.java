package com.juaracoding.DBLaundry.utils;/*
IntelliJ IDEA 2022.3.2 (Ultimate Edition)
Build #IU-223.8617.56, built on January 26, 2023
@Author User a.k.a. Safril Efendi Lubis
Java Developer
Created on 17/03/2023 19:01
@Last Modified 17/03/2023 19:01
Version 1.1
*/
import javax.servlet.http.HttpServletResponse;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;

public class PdfGeneratorLibre {

    private String [] strExceptionArr = new String[2];

    public PdfGeneratorLibre() {
        strExceptionArr[0] = "PdfGeneratorLibre";
    }

    public void generate(String strTitle,String[] strHeader, String[][] strBody , HttpServletResponse response)  {
        try
        {
            Document document = new Document(PageSize.A4);
            document.setPageSize(PageSize.A4.rotate());
            Rectangle footer = new Rectangle(30f, 30f, PageSize.A4.getRight(30f), 140f);
            footer.setBorder(Rectangle.BOX);
            footer.setBorderColor(Color.black);
            footer.setBorderWidth(2f);

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Image image = Image.getInstance("https://i.ibb.co/wBPtZFj/logo.png");
            image.scaleAbsolute(235f,70f);
            document.add(image);

            Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fontTiltle.setSize(20);
            Paragraph paragraph = new Paragraph(strTitle, fontTiltle);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraph);
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100f);
            table.setWidths(new int[] {1,1,2,2,2,2,2,2,2});
            table.setSpacingBefore(5);
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(CMYKColor.BLACK);// INI DIGANTI BIAR GAK SAMA
            cell.setPadding(5);
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            font.setColor(CMYKColor.WHITE);
            for(int i=0;i<strHeader.length;i++)
            {
                cell.setPhrase(new Phrase(strHeader[i], font));
//                cell.setBackgroundColor(Color.LIGHT_GRAY);// INI DIGANTI BIAR GAK SAMA
                cell.setBackgroundColor(Color.GRAY);// INI DIGANTI BIAR GAK SAMA
                table.addCell(cell);
            }

            // Iterating the list of students
            for(int i=0;i<strBody.length;i++)
            {
                for(int j=0;j<strBody[i].length;j++)
                {
                    table.addCell(strBody[i][j]);
                }
            }

            document.add(table);
            document.close();
        }
        catch(Exception e)
        {
            strExceptionArr[1] = "generate(String[] strHeader,String[][] strBody ,HttpServletResponse response) --- LINE 59";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
        }
    }
}
