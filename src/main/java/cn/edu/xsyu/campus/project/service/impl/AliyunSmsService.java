package cn.edu.xsyu.campus.project.service.impl;

import cn.edu.xsyu.campus.project.entity.AliyunSmsProperties;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliyunSmsService {
    @Autowired
    private AliyunSmsProperties properties;

    public SendSmsResponse sendSms(String phoneNumbers, String templateParam) {
        DefaultProfile profile = DefaultProfile.getProfile(properties.getRegionId(), properties.getAccessKeyId(),
                properties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSignName(properties.getSignName());
        request.setTemplateCode(properties.getTemplateCode());
        request.setPhoneNumbers(phoneNumbers);
        request.setTemplateParam(templateParam);

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}