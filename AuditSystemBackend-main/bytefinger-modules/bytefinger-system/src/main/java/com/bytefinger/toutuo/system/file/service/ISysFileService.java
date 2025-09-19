package com.bytefinger.toutuo.system.file.service;

import com.bytefinger.toutuo.api.system.file.domain.SysFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件上传接口
 *
 * @author patrick
 */
public interface ISysFileService {

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    String uploadFile(String modelName, MultipartFile file) throws Exception;

    InputStream downloadFile(String ossPath) throws IOException;

    String generatePresignedUrl(String ossPath);

    void downForZip(List<String> keyList, String fileName);

    void watermark(InputStream inputStream, OutputStream outputStream);
}
