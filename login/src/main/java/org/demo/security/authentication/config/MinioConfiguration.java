package org.demo.security.authentication.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//MinioClient 客户端没有线程安全问题
// 声明这是一个配置类
@Configuration
// 启用配置属性，这里指的是MinioProperties
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {

    // 自动注入Minio属性配置
    @Autowired
    private MinioProperties properties;

    @Bean
    public MinioClient minioClient() {
        // 使用MinioClient的构建器模式来创建客户端实例
        return MinioClient
                .builder()
                // 设置Minio服务的端点URL
                .endpoint(properties.getEndpoint())
                // 设置访问Minio服务的认证信息
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                // 构建并返回MinioClient实例
                .build();
    }
}
