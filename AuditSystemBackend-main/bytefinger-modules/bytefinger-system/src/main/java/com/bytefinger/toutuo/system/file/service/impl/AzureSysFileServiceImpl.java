package com.bytefinger.toutuo.system.file.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.bytefinger.common.core.utils.AzureUtil;
import com.bytefinger.common.core.utils.OssUtil;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.toutuo.system.file.service.ISysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * OSS文件存储
 *
 * @author patrick
 */
@Primary
@Service("azureSysFileService")
@Slf4j
public class AzureSysFileServiceImpl implements ISysFileService {

    private static int interval = -5;

    /**
     * OSS 上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(String modelName, MultipartFile file) throws Exception {
        log.info("进入文件上传");
        String fileName = modelName + "/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        String filename = file.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        metadata.setContentType("application/" + fileType);
        //判断文件大小是否超过50M，是就启动分片上传OSS
        long size = file.getSize();
        long max = 50 * 1024 * 1024;
        return AzureUtil.uploadObject(file.getInputStream(), fileName);

    }

    /**
     * 大文件分片上传处理，超过50M自动此处处理*
     */
    private static final ExecutorService batchDownloadFileExecutor = Executors.newFixedThreadPool(20);
    public String uploadBigFile(MultipartFile file, String fileName, ObjectMetadata metadata) throws Exception{
        //log.info("进入大文件上传，上传时间：{}，文件为：{}", DateUtil.dateNew(new Date()),fileName);
        OSSClient ossClient = OssUtil.getOSSClient();
        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(OssUtil.bucketName, fileName);
        request.setObjectMetadata(metadata);

        // 初始化分片。
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();

        // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
        final long partSize = 10 * 1024 * 1024L;   //10 MB。

        // 根据上传的数据大小计算分片数。以本地文件为例，说明如何通过File.length()获取上传数据的大小。
        long fileLength = file.getSize();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        int finalPartCount = partCount;
        CompletableFuture<List<PartETag>> future = CompletableFuture.supplyAsync(() -> {
            List<PartETag> partETagsa = new ArrayList<PartETag>();
            for (int i = 0; i < finalPartCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == finalPartCount) ? (fileLength - startPos) : partSize;
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(OssUtil.bucketName);
                uploadPartRequest.setKey(fileName);
                uploadPartRequest.setUploadId(uploadId);
                // 设置上传的分片流。
                // 以本地文件为例说明如何创建FIleInputstream，并通过InputStream.skip()方法跳过指定数据。
                try {
                    InputStream instream  = file.getInputStream();
                    instream.skip(startPos);
                    uploadPartRequest.setInputStream(instream);
                    // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
                    uploadPartRequest.setPartSize(curPartSize);
                    // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
                    uploadPartRequest.setPartNumber(i + 1);
                    // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                    UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                    // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
                    partETagsa.add(uploadPartResult.getPartETag());
                    instream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return partETagsa;
        }, batchDownloadFileExecutor);

        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(OssUtil.bucketName, fileName, uploadId, future.join());
        // 完成分片上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        log.info("结束文件上传，结束时间：{}", DateUtil.dateNew(new Date()));
        return fileName;
    }

    @Override
    public InputStream downloadFile(String ossPath) throws IOException {
        return AzureUtil.downloadObject(ossPath);
    }

    @Override
    public String generatePresignedUrl(String ossPath) {
        return OssUtil.generatePresignedUrl(ossPath);
    }

    @Override
    public void downForZip(List<String> keyList, String fileName) {
        HttpServletResponse response = ServletUtils.getResponse();
        // 创建临时文件
        File zipFile = null;
        try {
            //临时文件名称
            zipFile = File.createTempFile(System.currentTimeMillis() + "", ".zip");
            FileOutputStream f = new FileOutputStream(zipFile);
            CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
            ZipOutputStream zos = new ZipOutputStream(csum);
            for (String ossFile : keyList) {
                InputStream inputStream = this.downloadFile(ossFile);
                String name = ossFile.substring(ossFile.lastIndexOf("/") + 1);
                zos.putNextEntry(new ZipEntry(name));

                int pos = ossFile.lastIndexOf('.');
                //获取文件后缀
                String suffix = ossFile.substring(pos + 1);
                //if ("pdf".equals(suffix.toLowerCase())) {
                //    File pdfFile = File.createTempFile(System.currentTimeMillis() + "", ".pdf");
                //    FileOutputStream pdf = new FileOutputStream(pdfFile);
                //    watermark(inputStream, pdf);
                //    inputStream = new FileInputStream(pdfFile);
                //    pdfFile.delete();
                //}
                int bytesRead = 0;
                while ((bytesRead = inputStream.read()) != -1) {
                    zos.write(bytesRead);
                }
                inputStream.close();
                zos.closeEntry();
            }
            zos.close();
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Location", fileName);
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            FileInputStream fis = new FileInputStream(zipFile);
            BufferedInputStream buff = new BufferedInputStream(fis);
            OutputStream out = new BufferedOutputStream(response.getOutputStream());
            byte[] car = new byte[1024];
            int l = 0;
            while (l < zipFile.length()) {
                int j = buff.read(car, 0, 1024);
                l += j;
                out.write(car, 0, j);
            }

            fis.close();
            buff.close();
            out.flush();
            out.close();
            zipFile.delete();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void watermark(InputStream inputStream, OutputStream outputStream) {
        try {
            // 添加水印的时候,就已经在outputStream写入了
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
//            label.setText(SecurityUtils.getUsername());
//            metrics = label.getFontMetrics(label.getFont());
//            textH = metrics.getHeight();
//            textw = metrics.stringWidth(label.getText());
//            for (int i = 1; i < total; i++) {
//                Rectangle cropBox = reader.getCropBox(i);
//                // 在内容上方加水印
//                content = stamper.getOverContent(i);
//                gs.setFillOpacity(0.2f);
//                gs.setStrokeOpacity(0.1f);
//                content.setGState(gs);
//                content.beginText();
//                content.setColorFill(com.itextpdf.text.BaseColor.LIGHT_GRAY);
//                content.setFontAndSize(base, 13);
//                // 获取PDF页面大小
//                float pageHeight = cropBox.getHeight();
//                float pageWidth = cropBox.getWidth();
//                //将文字显示在pdf页面中
//                String text = SecurityUtils.getUsername() + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
//                for (int height = interval + textH; height < pageHeight; height = height + textH * 3) {
//                    for (int width = interval + textw; width < pageWidth + textw; width = width + textw * 2) {
//                        content.showTextAligned(Element.ALIGN_LEFT, text, width - textw, height - textH, 30);
//                    }
//                }
//                //设置文字颜色
//                content.setColorFill(com.itextpdf.text.BaseColor.BLACK);
//                //设置文字大小
//                content.setFontAndSize(base, 6);
//                content.endText();
//            }
//            stamper.close();
        } catch (Exception e) {

        }
    }
}
