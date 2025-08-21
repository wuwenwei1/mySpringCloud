package org.demo.security.authentication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String userImageBucketName;
    private String dataRemittanceBucketName;
    private String dataRemittanceReviewBucketName;
    private String usingApplyBucketName;
}
