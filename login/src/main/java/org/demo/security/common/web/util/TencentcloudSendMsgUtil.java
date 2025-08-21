package org.demo.security.common.web.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import java.io.IOException;

/**
 * 腾讯云短信服务
 */
public class TencentcloudSendMsgUtil {

    public static void main(String[] args) {
        // 腾讯云>>短信>>应用管理>>应用列表>>SDKAppID
        int appID = 1400917338;
        // 腾讯云>>短信>>应用管理>>应用列表>>AppKey
        String appKey = "a50129336a580e7f70bf887bc0e4f0f0";
        // 腾讯云>>短信>>正文模板管理>>选择一个模板
        int templateID = 2267599;
        // 腾讯云>>短信>>签名管理>>签名内容
        String SMSSign = "厦门新舰桥数智研究院";
        try {
            String[] params = {"2345"};
            // 发送一条短信
            SmsSingleSender smsSingleSender = new SmsSingleSender(appID, appKey);
            // 发送短信返回的结果,(86为中国代码,接收短信的手机号码,模板ID,要发送的参数,短信签名)
            SmsSingleSenderResult smsSingleSenderResult = smsSingleSender.sendWithParam("86", "13285928948", templateID, params, SMSSign, "", "");
            System.out.println("dfgd");
        } catch (Exception e) {
            System.out.println("异常了");
        }
    }



    /**
     *
     * @param phoneNumber   接收短信的手机号码
     * @param appID         腾讯云>>短信>>应用管理>>应用列表>>SDKAppID
     * @param appKey        腾讯云>>短信>>应用管理>>应用列表>>AppKey
     * @param templateID    腾讯云>>短信>>正文模板管理>>选择一个模板
     * @param SMSSign       腾讯云>>短信>>签名管理>>签名内容
     * @param params        需要发送的参数
     */
    public static SmsSingleSenderResult   sendMsgRegistrationCode(String phoneNumber,int appID,String appKey,int templateID,String SMSSign,String[] params) throws HTTPException, IOException {
        // 发送一条短信
        SmsSingleSender smsSingleSender = new SmsSingleSender(appID, appKey);
        // 发送短信返回的结果,(86为中国代码,接收短信的手机号码,模板ID,要发送的参数,短信签名)
        return smsSingleSender.sendWithParam("86", phoneNumber, templateID, params, SMSSign, "", "");
    }


}
