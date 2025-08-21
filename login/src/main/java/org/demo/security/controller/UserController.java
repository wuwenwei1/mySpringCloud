package org.demo.security.controller;

import org.demo.security.authentication.config.RedisCache;
import org.demo.security.common.constant.TencentCloudConstant;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.MyPatternUtil;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.UserServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserServiceExterior userServiceExterior;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisCache redisCache;


    /**
     * 后台添加账号
     * @param userName
     * @param phone
     * @param password
     * @param roleId
     * @return
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestParam String userName,
                                   @RequestParam String phone,
                                   @RequestParam String password,
                                   @RequestParam Long  roleId,
                          @RequestParam Long userTypeId
                                   ){
        if(null==userTypeId){
            return ResultBuilder.aResult().msg("请选择部门!").code("7000").build();
        }else {
            if(2L==userTypeId||3L==userTypeId){
                return ResultBuilder.aResult().msg("不允许在该部门下添加用户!").code("7000").build();
            }
        }
        if(null==phone|| StringUtils.isEmpty(phone.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写手机号!").code("7000").build();
        }else {
            phone=phone.replace(" ","");
        }
        if(null==userName|| StringUtils.isEmpty(userName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写姓名!").code("7000").build();
        }else {
            userName=userName.replace(" ","");
        }



        if(null==password|| StringUtils.isEmpty(password.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写密码!").code("7000").build();
        }else {
            password=passwordEncoder.encode(password.replace(" ",""));
        }

        if(null==roleId){
            return ResultBuilder.aResult().msg("请选择角色!").code("7000").build();
        }


        Result result=userServiceExterior.addUser(userName,phone,password,roleId,userTypeId);
        return result;
    }





    /**
     * 法人注册
     * @param userName
     * @param phone
     * @param password
     * @param country
     * @param idNumber
     * @param certificateStartDate
     * @param certificateEndDate
     * @param certificateTypeId
     * @return
     */
    @PostMapping("/legalPersonRegist")
    public Result legalPersonRegist(@RequestParam String userName,
                                   @RequestParam String phone,
                                   @RequestParam Long registrationCaptcha,
                                   @RequestParam String password,
                                   @RequestParam String country,
                                   @RequestParam String idNumber,
                                   @RequestParam String certificateStartDate,
                                   @RequestParam String certificateEndDate,
                                   @RequestParam Integer certificateTypeId,
                                    @RequestParam String licenseNumber,
                                    @RequestParam String creditCode,
                                    @RequestParam String companyName,
                                    @RequestParam Integer legalPersonTypeId){

        if(null==phone|| StringUtils.isEmpty(phone.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写手机号!").code("7000").build();
        }else {
            phone=phone.replace(" ","");
            if(!MyPatternUtil.validatePhoneNumber(phone)){
                return ResultBuilder.aResult().msg("请正确填写手机号!").code("7000").build();
            }else {
                if(null==registrationCaptcha){
                    return ResultBuilder.aResult().msg("请填写验证码!").code("7000").build();
                }else {
                    if (!(registrationCaptcha >= 10000 && registrationCaptcha < 100000)) {
                        return ResultBuilder.aResult().msg("注册码无效!").code("7000").build();
                    } else {
                        try {
                            boolean b = redisCache.hasKey(TencentCloudConstant.REGISTRATION_PRE + phone);
                            if (!b) {
                                return ResultBuilder.aResult().msg("注册码过期!").code("7000").build();
                            } else {
                                Long cacheCode = redisCache.getCacheObject(TencentCloudConstant.REGISTRATION_PRE + phone);
                                if (!(cacheCode.equals(registrationCaptcha))) {
                                    return ResultBuilder.aResult().msg("注册码过期!").code("7000").build();
                                }
                            }
                        }catch (Exception e){
                            return ResultBuilder.aResult().msg("系统错误!").code("7000").build();
                        }
                    }
                }
            }
        }

        if(null==legalPersonTypeId){
            return ResultBuilder.aResult().msg("请填写法人类型!").code("7000").build();
        }
        if(null==companyName|| StringUtils.isEmpty(companyName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写单位名称!").code("7000").build();
        }else {
            companyName=companyName.replace(" ","");
        }
        if(null==creditCode|| StringUtils.isEmpty(creditCode.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写统一社会信用代码!").code("7000").build();
        }else {
            creditCode=creditCode.replace(" ","");
        }
        if(null==licenseNumber|| StringUtils.isEmpty(licenseNumber.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写许可证编号!").code("7000").build();
        }else {
            licenseNumber=licenseNumber.replace(" ","");
        }


        if(null==userName|| StringUtils.isEmpty(userName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写法人姓名!").code("7000").build();
        }else {
            userName=userName.replace(" ","");
        }

        if(null==certificateTypeId){
            return ResultBuilder.aResult().msg("请填写证件类型!").code("7000").build();
        }

        if(null==country|| StringUtils.isEmpty(country.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写国家/地区!").code("7000").build();
        }else {
            country=country.replace(" ","");
        }
        if(null==idNumber|| StringUtils.isEmpty(idNumber.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写证件号码!").code("7000").build();
        }else {
            idNumber=idNumber.replace(" ","");
        }
        if(null==certificateStartDate|| StringUtils.isEmpty(certificateStartDate.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写证件有效期开始日期!").code("7000").build();
        }else {
            certificateStartDate=certificateStartDate.replace(" ","");
        }
        if(null==certificateEndDate|| StringUtils.isEmpty(certificateEndDate.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写证件有效期结束日期!").code("7000").build();
        }else {
            certificateEndDate=certificateEndDate.replace(" ","");
        }
        if(null==password|| StringUtils.isEmpty(password.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写密码!").code("7000").build();
        }else {
            password=passwordEncoder.encode(password.replace(" ",""));
        }
        Result result=userServiceExterior.legalPersonRegist(userName,phone,password,country,idNumber,certificateStartDate,certificateEndDate,certificateTypeId,licenseNumber,creditCode,companyName,legalPersonTypeId);
        return result;
    }

    /**
     * 个人注册
     * @param userName
     * @param phone
     * @param password
     * @param country
     * @param idNumber
     * @param certificateStartDate
     * @param certificateEndDate
     * @param certificateTypeId
     * @return
     */
    @PostMapping("/individualRegist")
    public Result individualRegist(@RequestParam String userName,
                          @RequestParam String phone,
                          @RequestParam Long registrationCaptcha,
                          @RequestParam String password,
                          @RequestParam String country,
                          @RequestParam String idNumber,
                          @RequestParam String certificateStartDate,
                          @RequestParam String certificateEndDate,
                          @RequestParam Integer certificateTypeId){

        if(null==phone|| StringUtils.isEmpty(phone.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写手机号!").code("7000").build();
        }else {
            phone=phone.replace(" ","");
            if(!MyPatternUtil.validatePhoneNumber(phone)){
                return ResultBuilder.aResult().msg("请正确填写手机号!").code("7000").build();
            }else {
                if(null==registrationCaptcha){
                    return ResultBuilder.aResult().msg("请填写验证码!").code("7000").build();
                }else {
                    if (!(registrationCaptcha >= 10000 && registrationCaptcha < 100000)) {
                        return ResultBuilder.aResult().msg("注册码无效!").code("7000").build();
                    } else {
                        try {
                            boolean b = redisCache.hasKey(TencentCloudConstant.REGISTRATION_PRE + phone);
                            if (!b) {
                                return ResultBuilder.aResult().msg("注册码过期!").code("7000").build();
                            } else {
                                Long cacheCode = redisCache.getCacheObject(TencentCloudConstant.REGISTRATION_PRE + phone);
                                System.out.println("sdfs");
                                if (!(cacheCode.equals(registrationCaptcha))) {
                                    return ResultBuilder.aResult().msg("注册码过期!").code("7000").build();
                                }
                            }
                        }catch (Exception e){
                            return ResultBuilder.aResult().msg("系统错误!").code("7000").build();
                        }
                    }
                }
            }
        }
        if(null==userName|| StringUtils.isEmpty(userName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写姓名!").code("7000").build();
        }else {
            userName=userName.replace(" ","");
        }

        if(null==certificateTypeId){
            return ResultBuilder.aResult().msg("请填写证件类型!").code("7000").build();
        }

        if(null==country|| StringUtils.isEmpty(country.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写国家/地区!").code("7000").build();
        }else {
            country=country.replace(" ","");
        }
        if(null==idNumber|| StringUtils.isEmpty(idNumber.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写证件号码!").code("7000").build();
        }else {
            idNumber=idNumber.replace(" ","");
        }
        if(null==certificateStartDate|| StringUtils.isEmpty(certificateStartDate.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写证件有效期开始日期!").code("7000").build();
        }else {
            certificateStartDate=certificateStartDate.replace(" ","");
        }
        if(null==certificateEndDate|| StringUtils.isEmpty(certificateEndDate.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写证件有效期结束日期!").code("7000").build();
        }else {
            certificateEndDate=certificateEndDate.replace(" ","");
        }
        if(null==password|| StringUtils.isEmpty(password.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写密码!").code("7000").build();
        }else {
            password=passwordEncoder.encode(password.replace(" ",""));
        }
        Result result=userServiceExterior.individualRegist(userName,phone,password,country,idNumber,certificateStartDate,certificateEndDate,certificateTypeId);
        return result;
    }










    /**
     * 上传头像
     * @param userImage
     * @return
     */
    @PostMapping("/updateUserImage")
    public Result updateUserImage(@RequestParam MultipartFile userImage) {
        return userServiceExterior.updateUserImage(userImage);
    }

    /**
     * 通过手机号,修改密码
     * @param phone
     * @param updatePwdCaptcha
     * @param newPwd
     * @return
     */
    @PostMapping("/updatePwd")
    public Result updatePwd(@RequestParam String phone,
                            @RequestParam Long updatePwdCaptcha,
                            @RequestParam String newPwd){
        if(null==phone|| StringUtils.isEmpty(phone.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写手机号!").code("7000").build();
        }else {
            phone=phone.replace(" ","");
            if(!MyPatternUtil.validatePhoneNumber(phone)){
                return ResultBuilder.aResult().msg("请正确填写手机号!").code("7000").build();
            }else {
                if(null==updatePwdCaptcha){
                    return ResultBuilder.aResult().msg("请填写验证码!").code("7000").build();
                }else {
                    if (!(updatePwdCaptcha >= 10000 && updatePwdCaptcha < 100000)) {
                        return ResultBuilder.aResult().msg("验证码无效!").code("7000").build();
                    } else {
                        try {
                            boolean b = redisCache.hasKey(TencentCloudConstant.FORGET_PRE + phone);
                            if (!b) {
                                return ResultBuilder.aResult().msg("注册码过期!").code("7000").build();
                            } else {
                                Long cacheCode = redisCache.getCacheObject(TencentCloudConstant.FORGET_PRE + phone);
                                if (!(cacheCode.equals(updatePwdCaptcha))) {
                                    return ResultBuilder.aResult().msg("注册码过期!").code("7000").build();
                                }
                            }
                        }catch (Exception e){
                            return ResultBuilder.aResult().msg("系统错误!").code("7000").build();
                        }
                    }
                }
            }
        }

        if(null==newPwd|| StringUtils.isEmpty(newPwd.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写新密码!").code("7000").build();
        }else {
            newPwd=passwordEncoder.encode(newPwd.replace(" ",""));
        }
        return userServiceExterior.updatePwd(phone,newPwd);
    }

}
