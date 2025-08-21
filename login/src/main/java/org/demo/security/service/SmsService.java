package org.demo.security.service;

import com.github.qcloudsms.httpclient.HTTPException;
import org.demo.security.common.web.model.Result;

import java.io.IOException;

public interface SmsService {
    Result getRegistationCaptcha(String phone) ;

    Result getSmsLoginCaptcha(String phone);

    Result getForgetCaptcha(String phone);

    /*Result sendUpdatePhoneCode(String phoneNumber);*/
}
