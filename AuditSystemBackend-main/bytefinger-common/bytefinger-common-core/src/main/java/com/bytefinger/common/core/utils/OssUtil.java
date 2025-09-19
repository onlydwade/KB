package com.bytefinger.common.core.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.Date;

/***
 * @Param:
 * @Return:
 * @Author: Jone Ying
 * @Date: 2023/2/3
**/
public class OssUtil {
    private static String endpoint;
    private static String url;
    private static String accessKeyId;
    private static String accessKeySecret;
    public static String bucketName;

    public static String prefix;

    public static void init(String _accessKeyId, String _accessKeySecret, String _endpoint, String _url, String _prefix, String _bucketName){
        url = _url;
        prefix = _prefix;
        endpoint = _endpoint;
        bucketName = _bucketName;
        accessKeyId = _accessKeyId;
        accessKeySecret = _accessKeySecret;
    }

    public static String uploadObject(InputStream is, String filePath) {
        OSSClient ossClient = getOSSClient();
        filePath = prefix + "/" + filePath;
        ossClient.putObject(bucketName, filePath, is);
        if (is != null) {
            try {
                is.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
        shutdown(ossClient);
        return filePath;
    }

    public static String uploadObject(InputStream is, String filePath, ObjectMetadata metadata) {
        OSSClient ossClient = getOSSClient();
        filePath = prefix + "/" + filePath;
        ossClient.putObject(bucketName, filePath, is, metadata);
        if (is != null) {
            try {
                is.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
        shutdown(ossClient);
        return filePath;
    }

    public static String uploadObject(String serverFilePath, String filePath) {
        OSSClient ossClient = getOSSClient();
        filePath = prefix + "/" + filePath;
        ossClient.putObject(bucketName, filePath, new File(serverFilePath));
        shutdown(ossClient);
        return filePath;
    }

    public static String uploadObject(File file, String filePath, ObjectMetadata metadata) {
        OSSClient ossClient = getOSSClient();
        filePath = prefix + "/" + filePath;
        ossClient.putObject(bucketName, filePath, file, metadata);
        shutdown(ossClient);
        return filePath;
    }

    public static String uploadObject(File file, String filePath) {
        OSSClient ossClient = getOSSClient();
        filePath = prefix + "/" + filePath;
        ossClient.putObject(bucketName, filePath, file);
        shutdown(ossClient);
        return filePath;
    }

    public static InputStream downloadObject(String fileName) throws IOException {
        OSSClient ossClient = getOSSClient();
        OSSObject ossObject = null;
        ossObject = ossClient.getObject(bucketName, fileName);
        return ossObject.getObjectContent();
    }

    public OssUtil() {}

    public static OSSClient getOSSClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public static void shutdown(OSSClient ossClient) {
        ossClient.shutdown();
    }

    public static String generatePresignedUrl(String ossPath) {
        OSSClient ossClient = null;
        URL signedUrl = null;
        try {
            // 设置样式，样式中包含文档预览参数。
            String style = "imm/previewdoc,copy_1";
            // 指定签名URL过期时间为10分钟。
            Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10 );
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, ossPath, HttpMethod.GET);
            req.setExpiration(expiration);
//            req.setProcess(style);
            ossClient = getOSSClient();
            signedUrl = ossClient.generatePresignedUrl(req);
        } catch (OSSException oe) {
           oe.printStackTrace();
        } catch (ClientException ce) {
          ce.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            return null == signedUrl? "" : signedUrl.toString();
        }
    }
}