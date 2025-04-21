package com.linyi.pig.config;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: linyi
 * @Date: 2025/2/26
 * @ClassName: MinioComponent
 * @Version: 1.0
 * @Description: Minio组件
 */
@Component
public class MinioComponent {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfiguration configuration;


    /**
     * 获取预签名的POST策略表单数据
     * 该方法用于生成一个带有预签名的POST策略的映射，以便客户端可以使用这个映射来上传文件到指定的存储桶
     *
     * @param fileName 文件名，用于指定上传到存储桶后的文件键值
     * @param time 时间，用于指定策略的有效期
     * @return 返回一个包含预签名POST策略表单数据的映射，如果生成失败则返回null
     */
    public Map getPolicy(String fileName, ZonedDateTime time) {
        // 创建一个PostPolicy对象，指定存储桶名称和策略有效期
        PostPolicy postPolicy = new PostPolicy(configuration.getBucketName(), time);
        // 添加一个等于条件到策略中，确保上传的文件键值与指定的文件名匹配
        postPolicy.addEqualsCondition("key", fileName);
        try {
            // 使用Minio客户端获取预签名的POST表单数据
            Map<String, String> map = minioClient.getPresignedPostFormData(postPolicy);
            // 创建一个新的HashMap来存储处理后的表单数据
            HashMap<String, String> map1 = new HashMap<>();
            // 遍历原始表单数据，移除所有键中的连字符
            map.forEach((k,v)->{
               map1.put(k.replaceAll("-",""),v);
            });
            // 添加主机URL到表单数据中，用于指定上传的目标URL
            map1.put("host",configuration.getUrl()+"/"+configuration.getBucketName());
            // 返回处理后的表单数据映射
            return map1;
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        // 如果获取预签名POST策略失败，则返回null
        return null;
    }


    /**
     * 获取预签名的URL以访问指定对象
     * 此方法用于生成一个带有签名的URL，该URL可以在一段时间内提供对指定对象的访问权限
     * 它常用于提供临时访问权限，而无需暴露密钥或进行身份验证
     *
     * @param objectName 对象名称，即要访问的对象在存储桶中的唯一标识符
     * @param method HTTP方法，定义了URL的访问权限（例如，GET表示读取权限）
     * @param time 时间限制，指定了URL在多长时间内有效
     * @param timeUnit 时间单位，与时间参数一起使用，指定时间的度量单位
     * @return 返回预签名的URL字符串，如果操作失败则返回null
     */
    public String getPolicyUrl(String objectName, Method method, int time, TimeUnit timeUnit) {
        try {
            // 使用MinIO Java SDK构建并发送请求以获取预签名的URL
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(method)
                    .bucket(configuration.getBucketName())
                    .object(objectName)
                    .expiry(time, timeUnit).build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将一个MultipartFile对象上传到MinIO服务器，并指定文件名
     * 此方法负责处理文件上传过程，包括读取文件流、设置内容类型等
     *
     * @param file 要上传的MultipartFile对象，通常来自HTTP请求
     * @param fileName 指定上传后文件的名称
     */
    public void upload(MultipartFile file, String fileName) {
        // 使用putObject上传一个文件到存储桶中。
        try {
            // 获取文件输入流
            InputStream inputStream = file.getInputStream();
            // 构建上传对象的参数，包括存储桶名称、对象名称、文件流、文件大小和内容类型
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定对象的预签名URL
     *
     * @param objectName 对象名称，用于指定MinIO中存储对象的名称
     * @param time 时间长度，与timeUnit一起使用，用于设置预签名URL的有效期
     * @param timeUnit 时间单位，用于指定time参数的时间单位
     * @return 返回预签名URL，如果生成失败则返回null
     *
     * 此方法用于生成一个带有预签名的URL，通过这个URL可以在限定时间内访问MinIO中的指定对象
     * 预签名URL的主要作用是提供临时访问权限，而不需要暴露MinIO服务的密钥信息
     *
     * 注意：此方法仅捕获异常并打印堆栈跟踪，不进行重试或其他异常处理逻辑
     * 这是因为异常处理策略可能需要根据具体应用场景进行定制，例如，可能需要记录日志、通知管理员或者重试请求
     */
    public String getUrl(String objectName, int time, TimeUnit timeUnit) {
        String url = null;
        try {
            // 构建获取预签名对象URL的请求参数
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET) // 使用GET方法，以便直接通过URL访问对象
                    .bucket(configuration.getBucketName()) // 设置存储桶名称，从配置中获取
                    .object(objectName) // 设置对象名称
                    .expiry(time, timeUnit) // 设置URL的有效期
                    .build();

            // 调用MinIO客户端方法，获取预签名URL
            url = minioClient.getPresignedObjectUrl(args);
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return url;
    }

}
