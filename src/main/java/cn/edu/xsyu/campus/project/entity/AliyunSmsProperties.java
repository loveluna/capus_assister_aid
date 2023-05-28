package cn.edu.xsyu.campus.project.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsProperties {
    private String regionId;
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;
    private String maintainUserPhone;
}