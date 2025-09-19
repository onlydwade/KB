package com.bytefinger.common.core.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.azure.core.http.HttpClient;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.core.http.netty.NettyAsyncHttpClientProvider;
import com.azure.core.util.BinaryData;
import com.azure.core.util.ClientOptions;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobProperties;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.resources.ConnectionProvider;

import java.io.*;
import java.net.URL;
import java.util.Date;

/***
 * @Param:
 * @Return:
 * @Author: Jone Ying
 * @Date: 2023/2/3
**/
public class AzureUtil {

    private static String connectionString;
    // Blob容器名称
    private static String containerName;

    private static String prefix;

    public static void init(String connectionString, String containerName, String prefix){
        AzureUtil.connectionString = connectionString;
        AzureUtil.containerName = containerName;
        AzureUtil.prefix = prefix;
    }


    public static BlobServiceClient getBlobContainer()
    {
        try
        {

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            //BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

//            BlobServiceClientBuilder builder = new BlobServiceClientBuilder()
//            .clientOptions(new ClientOptions().setApplicationId("bfstoragedev"));
//            return builder.connectionString(connectStr).buildClient();
            return blobServiceClient;
        }
        catch(Exception e)
        {
            // 加载上传文件启动异常
            return null;
        }
    }


    public static String uploadObject(InputStream is, String filePath) {
        // 获取Blob客户端 (使用文件名作为Blob名称)
        BlobServiceClient blobContainer = getBlobContainer();

        BlobContainerClient blobContainer1 = blobContainer.getBlobContainerClient(containerName);
        //blobContainer1.createIfNotExists();
        // 设置Blob的HTTP头信息（可选）
        //BlobHttpHeaders headers = new BlobHttpHeaders()
        //        .setContentType(Files.probeContentType(file.toPath()));

        // 上传文件
        BlobClient test = blobContainer1.getBlobClient(filePath);
        test.upload(is, true);
        // 设置HTTP头（如果需要）
        //blobClient.setHttpHeaders(headers);

        return filePath;
    }

    public static String uploadObject(InputStream is, String filePath, String containerName) {
        filePath = prefix + "/" + filePath;
        uploadObject(is, filePath);
        if (is != null) {
            try {
                is.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
        return filePath;
    }


    public AzureUtil() {}

    public static InputStream downloadObject(String filePath) {
        BlobServiceClient blobContainer = getBlobContainer();
        BlobContainerClient blobContainer1 = blobContainer.getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainer1.getBlobClient(filePath);
        BinaryData binaryData = blobClient.downloadContent();
        return binaryData.toStream();
    }
}