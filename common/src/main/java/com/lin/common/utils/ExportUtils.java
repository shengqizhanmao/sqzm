package com.lin.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExportUtils {
    public static <T> void fillExcel(HttpServletResponse response, String title, List<T> list,String fileName) throws IOException {
        //获取流
        ServletOutputStream out = response.getOutputStream();
        //定义导出类型
        response.setContentType("multipart/form-data");
        //定义字符集
        response.setCharacterEncoding("UTF-8");
        //文件名称
        fileName=fileName+".xlsx";
        //导出excel的文件
        response.setHeader("Content-disposition","attachment;filename="+fileName);
        //文件模块输入流
        InputStream inputStream = new ClassPathResource("").getInputStream();
        //定义写入excel对象
        ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(inputStream).build();
        //定义工作表对象
        WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
        //开启自动换行,每次写入一条List数据都会重新生存
        FillConfig fillConfig = FillConfig.builder().forceNewRow(true).build();
        //填充标题
        excelWriter.fill(title,writeSheet);
        //填充数据
        excelWriter.fill(list,fillConfig,writeSheet);
        //完成
        excelWriter.finish();
        out.close();

    }
}
