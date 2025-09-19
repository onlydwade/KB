package com.bytefinger.toutuo.system.file.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.system.file.domain.SysFile;
import com.bytefinger.toutuo.system.file.service.ISysFileService;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.Matrix;
import feign.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件处理
 *
 * @author patrick
 */
@Api(tags = "文件处理")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SysFileController {

    private final ISysFileService sysFileService;

    /**
     * 资源映射路径 前缀
     */
    @Value("${oss.url}")
    public String ossUrl;

    @ApiOperation(value = "文件下载请求")
    @PostMapping("/download")
    public R<Object> download(@RequestBody SysFile file) {
        HttpServletResponse response = null;
        try {
            response = ServletUtils.getResponse();
            response.reset();
            // 清空response
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            InputStream inputStream = sysFileService.downloadFile(file.getOssPath());
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) toClient.write(buffer, 0, b);
            }

            inputStream.close();
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok("下载成功");
    }

    @ApiOperation(value = "文件下载请求")
    @PostMapping("/download2")
    public void download2(@RequestBody SysFile file) {
        HttpServletResponse response = null;
        try {
            response = ServletUtils.getResponse();
            response.reset();
            // 清空response
//            response.setContentType("application/octet-stream");
            response.setContentType("multipart/form-data");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            String path = file.getOssPath();
            InputStream inputStream = sysFileService.downloadFile(path);
            int pos = path.lastIndexOf('.');
            //获取文件后缀
            String suffix = path.substring(pos + 1);
            //判断是否为pdf文件
//            if ("pdf".equals(suffix.toLowerCase())) {
////                response.setContentType("application/pdf");
////                sysFileService.watermark(inputStream, toClient);
//            }else{
//                int b = 0;
//                byte[] buffer = new byte[1000000];
//                while (b != -1) {
//                    b = inputStream.read(buffer);
//                    if (b != -1) toClient.write(buffer, 0, b);
//                }
//            }
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) toClient.write(buffer, 0, b);
            }
            inputStream.close();
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "文件下载get请求")
    @GetMapping("/download3")
    public void download3(@RequestParam("name")  String name,@RequestParam("ossPath")  String ossPath) {
        HttpServletResponse response = null;
        try {
            int pos = ossPath.lastIndexOf('.');
            String fileName=name;
            //获取文件后缀
            String suffix = ossPath.substring(pos + 1);
            response = ServletUtils.getResponse();
            response.reset();
            // 清空response
//            response.setContentType("application/octet-stream");
            response.setContentType("multipart/form-data");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            InputStream inputStream = sysFileService.downloadFile(ossPath);
            ////判断是否为pdf文件
            //if ("pdf".equals(suffix.toLowerCase())) {
            //    response.setContentType("application/pdf");
            //    sysFileService.watermark(inputStream, toClient);
            //}else{
                int b = 0;
                byte[] buffer = new byte[1000000];
                while (b != -1) {
                    b = inputStream.read(buffer);
                    if (b != -1) toClient.write(buffer, 0, b);
                }
            //}
            inputStream.close();
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void watermark(InputStream inputStream, OutputStream outputStream){
        try {
//            // 添加水印的时候,就已经在outputStream写入了
//            PdfDocument document = new PdfDocument();
//            PdfReader reader = new PdfReader(inputStream);
//            PdfStamper stamper = new PdfStamper(reader, outputStream);
//            int total = reader.getNumberOfPages() + 1;
//            PdfContentByte content;
//            BaseFont base = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            PdfGState gs = new PdfGState();
//            for (int i = 1; i < total; i++) {
//                content = stamper.getOverContent(i);// 在内容上方加水印
//                //content = stamper.getUnderContent(i);//在内容下方加水印
//                gs.setFillOpacity(0.2f);
//                content.setGState(gs);
//                content.beginText();
//                content.setColorFill(com.itextpdf.text.BaseColor.LIGHT_GRAY);
//                content.setFontAndSize(base, 50);
//                content.setTextMatrix(70, 200);
//                content.showTextAligned(Element.ALIGN_CENTER, SecurityUtils.getUsername(), 300,320, 55);
//                //设置文字颜色
//                content.setColorFill(com.itextpdf.text.BaseColor.BLACK);
//                //设置文字大小
//                content.setFontAndSize(base, 6);
//                //将内容显示在pdf底部
//                String waterMarkName = DateUtils.dateTime(new Date());
//                content.showTextAligned(Element.ALIGN_CENTER, "下载时间：" + waterMarkName + "", 300, 10, 0);
//                content.endText();
//            }
//            stamper.close();
////            outputStream.close();
        }catch (Exception e){

        }
    }


//    public void watermark2(InputStream inputStream, OutputStream outputStream){
//        try {
//            // 添加水印的时候,就已经在outputStream写入了
//            PdfDocument document = new PdfDocument();
//            PdfReader reader = new PdfReader(inputStream);
//            PdfStamper stamper = new PdfStamper(reader, outputStream);
//            int total = reader.getNumberOfPages() + 1;
//            PdfContentByte content;
//            BaseFont base = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            PdfGState gs = new PdfGState();
//            int textH = 0;
//            int textw = 0;
//            JLabel label = new JLabel();
//            FontMetrics metrics;
//            label.setText(SecurityUtils.getUsername()) ;
//            metrics = label.getFontMetrics(label.getFont());textH = metrics.getHeight() ;
//            textw = metrics.stringWidth(label.getText());
//            for (int i = 1; i < total; i++) {
//                Rectangle cropBox = reader.getCropBox(i);
//
//                content = stamper.getOverContent(i);// 在内容上方加水印
//                //content = stamper.getUnderContent(i);//在内容下方加水印
//                gs.setFillOpacity(0.2f);
//                gs.setStrokeOpacity(0.1f);
//                content.setGState(gs);
//                content.beginText();
//                content.setColorFill(com.itextpdf.text.BaseColor.LIGHT_GRAY);
//                content.setFontAndSize(base, 13);
////                content.setTextMatrix(70, 200);
//                // 获取PDF页面大小
//                float pageHeight = cropBox.getHeight();
//                float pageWidth = cropBox.getWidth();
//                //将文字显示在pdf页面中
//                for (int height = interval + textH; height < pageHeight; height = height + textH * 3) {
//                    for (int width = interval + textw; width < pageWidth + textw; width = width + textw * 2) {
//                        content.showTextAligned(Element.ALIGN_LEFT, SecurityUtils.getUsername(), width - textw, height - textH, 30);
//                    }
//                }
//                //设置文字颜色
//                content.setColorFill(com.itextpdf.text.BaseColor.BLACK);
//                //设置文字大小
//                content.setFontAndSize(base, 6);
//                //将内容显示在pdf底部
//                String waterMarkName = DateUtils.dateTime(new Date());
//                //content.showTextAligned(Element.ALIGN_CENTER, "下载时间：" + waterMarkName + "", 300, 10, 0);
//                content.endText();
//            }
//            stamper.close();
////            outputStream.close();
//        }catch (Exception e){
//
//        }
//    }

    @ApiOperation(value = "文件下载请求")
    @PostMapping("/downForZip")
    public void downForZip(@RequestBody List<SysFile> files) {
        if(CollectionUtils.isEmpty(files)){
            return;
//            return R.fail("文件不能为空!");
        }
        List<String> keys = files.stream().map(v -> v.getOssPath()).collect(Collectors.toList());
        sysFileService.downForZip(keys, System.currentTimeMillis() + ".zip");
//        return R.ok("下载成功");
    }

    @ApiOperation(value = "生成临时访问链接")
    @PostMapping("/generatePresignedUrl")
    public R<Object> generatePresignedUrl(@RequestBody SysFile file) {
        String signUrl = sysFileService.generatePresignedUrl(file.getOssPath());
        return R.ok(signUrl);
    }

    @ApiOperation(value = "文件上传请求")
    @PostMapping("/upload/{modelName}")
    public R<SysFile> upload(@PathVariable("modelName") String modelName, MultipartFile file) {
        // 判断文件上传类型
        if (!checkFileType(file)) {
            throw new ServiceException("文件格式不正确！");
        }
        if (!checkFileSize(file)) {
            throw new ServiceException("文件大小不得超过1G！");
        }

        try {
            // 上传并返回访问地址

            String ossPath = sysFileService.uploadFile(modelName, file);
            SysFile sysFile = new SysFile();
            sysFile.setName(file.getOriginalFilename());

            sysFile.setOssPath(ossPath);
            sysFile.setUrl(ossUrl + "/" + ossPath);
            return R.ok(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail("上传文件失败，请稍后重试");
        }
    }

    private static final String FILE_TYPES = "jpeg、rar、doc、docx、pptx、zip、xls、xlsx、xlsm、webp、bmp、dib、pcp、dif、wmf、gif、jpg、tif、eps、psd、cdr、iff、tga、pcd、mpt、png、pdf、txt";

    /**
     * 检查数据
     *
     * @param file
     * @return
     */
    public boolean checkFileType(MultipartFile file) {
        if (null == file) {
            return false;
        }
        String filename = file.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        //先判断后缀名
        if (FILE_TYPES.contains(fileType)) {
            return true;
        }
        return false;
    }

    public boolean checkFileSize(MultipartFile file) {
        long size = file.getSize();
        long max = 1024 * 1024 * 1024;

        if (size > max) {
            return false;
        }
        return true;
    }




}
