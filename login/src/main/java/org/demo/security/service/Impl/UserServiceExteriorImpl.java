package org.demo.security.service.Impl;

import io.minio.*;
import jakarta.annotation.Resource;
import org.demo.security.authentication.config.MinioProperties;
import org.demo.security.authentication.config.RedisCache;
import org.demo.security.common.constant.TencentCloudConstant;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.*;
import org.demo.security.dao.*;
import org.demo.security.entity.*;
import org.demo.security.service.UserServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class UserServiceExteriorImpl implements UserServiceExterior {
    @Autowired
    private UcertificateMapper ucertificateMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CertificateMapper certificateMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private LegalPersonMapper legalPersonMapper;
    @Autowired
    private UlegalPersonMapper ulegalPersonMapper;

    @Autowired
    private UserTypeMapper userTypeMapper;

    @Autowired
    private RedisCache redisCache;
    @Resource
    private MinioClient minioclient;
    @Autowired
    private MinioProperties minioProperties;



    /**
     * 后台添加账号
     *
     * @param userName
     * @param phone
     * @param password
     * @param roleId
     * @return
     */
    @Transactional
    @Override
    public Result addUser(String userName, String phone, String password, Long roleId,Long userTypeId) {
        Long userId = SecurityUtils.getUserId();
        User user = new User();
        user.setUserName(userName);
        user.setPhone(phone);
        user.setPassword(password);
        user.setUserImage("");
        user.setLoginDate("");
        user.setStatus("0");
        user.setCreateBy(userId);
        user.setCreateTime(DataConversionUtil.getTodayAllStr(new Date()));
        user.setUpdateTime("");
        user.setDelFlag("0");
        user.setUserTypeId(userTypeId);
        Role role = roleMapper.getRoleByRoleId(roleId);
        if(null==role || "1".equals(role.getDelFlag())){
            ExceptionTool.throwException("该角色信息不存在!","7000");
        }
        UserType userType=userTypeMapper.getUserTypeByUserTypeId(userTypeId);
        if(null==userType||!"0".equals(userType.getDelFlag())){
            ExceptionTool.throwException("该用户类型不存在!","7000");
        }
        if(!"0".equals(userType.getStatus())){
            ExceptionTool.throwException("该用户类型已被禁用!!","7000");
        }

        try {
            userMapper.addUser(user);
            userMapper.updateCreateByByUserId(userId);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(roleId);
            userRoleMapper.addUserRole(userRole);
        } catch (DuplicateKeyException e) {
            User userDuplicate = userMapper.getUserByUserName(userName);
            if(null!=userDuplicate){
                ExceptionTool.throwException("姓名已被其他账号注册!", "7000");
            }
            ExceptionTool.throwException("手机号已被其他账号注册!", "7000");
        }
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();
    }



    /**
     * 法人注册
     *
     * @param userName
     * @param phone
     * @param password
     * @param country
     * @param idNumber
     * @param certificateStartDate
     * @param certificateEndDate
     * @param certificateTypeId
     * @param licenseNumber
     * @param creditCode
     * @param companyName
     * @param legalPersonTypeId
     * @return
     */
    @Transactional
    @Override
    public Result legalPersonRegist(String userName,
                                    String phone,
                                    String password,
                                    String country,
                                    String idNumber,
                                    String certificateStartDate,
                                    String certificateEndDate,
                                    Integer certificateTypeId,
                                    String licenseNumber,
                                    String creditCode,
                                    String companyName,
                                    Integer legalPersonTypeId) {
        User user = new User();
        user.setUserName(userName);
        user.setPhone(phone);
        user.setPassword(password);
        user.setCreateBy(null);
        user.setCreateTime(DataConversionUtil.getTodayAllStr(new Date()));
        user.setUpdateTime("");
        user.setUserImage("");
        user.setLoginDate("");
        user.setStatus("0");
        user.setDelFlag("0");
        user.setUserTypeId(3L);
        try {
            userMapper.addUser(user);
            userMapper.updateCreateByByUserId(user.getUserId());
        } catch (DuplicateKeyException e) {
            User userDuplicate = userMapper.getUserByUserName(userName);
            if(null!=userDuplicate){
                ExceptionTool.throwException("姓名已被其他账号注册!", "7000");
            }
            ExceptionTool.throwException("手机号已被其他账号注册!", "7000");
        }


        Certificate certificate = new Certificate();
        certificate.setCountry(country);
        certificate.setIdNumber(idNumber);
        certificate.setCertificateStartDate(certificateStartDate);
        certificate.setCertificateEndDate(certificateEndDate);
        certificate.setCertificateTypeId(certificateTypeId);
        try {
            certificateMapper.addCertificate(certificate);
        } catch (DuplicateKeyException e) {
            ExceptionTool.throwException("证件号码已被其他账号注册!", "7000");
        }


        Ucertificate ucertificate = new Ucertificate();
        ucertificate.setUserId(user.getUserId());
        ucertificate.setCertificateId(certificate.getCertificateId());
        ucertificateMapper.addUcertificate(ucertificate);



        UserRole userRole = new UserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(3L);
        userRoleMapper.addUserRole(userRole);

        LegalPerson legalPerson = new LegalPerson();
        legalPerson.setCompanyName(companyName);
        legalPerson.setCreditCode(creditCode);
        legalPerson.setLicenseNumber(licenseNumber);
        legalPerson.setLegalPersonName(userName);
        legalPerson.setLegalPersonTypeId(legalPersonTypeId);

        try {
            legalPersonMapper.addLegalPerson(legalPerson);
        } catch (DuplicateKeyException e) {
            ExceptionTool.throwException("企业信息已被其他账号注册!", "7000");
        }


        UlegalPerson ulegalPerson = new UlegalPerson();
        ulegalPerson.setLegalPersonId(legalPerson.getLegalPersonId());
        ulegalPerson.setUserId(user.getUserId());
        ulegalPersonMapper.addUlegalPerson(ulegalPerson);


        //redisCache.deleteObject(TencentCloudConstant.REGISTRATION_PRE + phone);
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();
    }



    /**
     * 通过手机号,修改密码
     *
     * @param phone
     * @param newPwd
     * @return
     */
    @Override
    public Result updatePwd(String phone, String newPwd) {
        User user = userMapper.getUserByPhone(phone);
        if (null == user) {
            ExceptionTool.throwException("手机号未注册!", "7000");
        } else {
            try {
                userMapper.updatePwdByPhone(phone, newPwd);
                redisCache.deleteObject(TencentCloudConstant.FORGET_PRE + phone);
            } catch (Exception e) {
                ExceptionTool.throwException("系统错误!", "7000");
            }


        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }

    // 创建存储桶策略配置，允许匿名用户读取存储桶中的对象
    private String createBucketPolicyConfig(String bucketName) {
        // 使用String.format方法格式化存储桶策略配置字符串
        return "{\"Version\": \"2012-10-17\",\"Statement\": [{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
    }

    /**
     * 上传头像
     *
     * @param userImage
     * @return
     */
    @Override
    public Result updateUserImage(MultipartFile userImage) {
        Long userId = SecurityUtils.getUserId();
        String userImagePathName = null;
        try {
            // 检查指定的存储桶是否存在
            boolean isBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getUserImageBucketName()).build());
            if (!isBucketExists) {// 如果存储桶不存在，则创建存储桶
                minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getUserImageBucketName()).build());
                // 设置存储桶策略，允许匿名用户读取对象
                minioclient.setBucketPolicy(SetBucketPolicyArgs.
                        builder().
                        bucket(minioProperties.getUserImageBucketName()). // 指定存储桶名称
                                config(createBucketPolicyConfig(minioProperties.getUserImageBucketName())). // 指定存储桶策略配置
                                build());
            }

            // 构建文件名，包含日期和UUID，确保文件名的唯一性
            userImagePathName = String.valueOf(userId)+"/" + UUID.randomUUID() + "-" + userImage.getOriginalFilename();
            minioclient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getUserImageBucketName())    //把文件上传到名为myfile的存储桶中
                    .object(userImagePathName) //上传之后的文件的新名称
                    .contentType(userImage.getContentType())
                    .stream(userImage.getInputStream(), userImage.getSize(), -1)
                    .build());
            String newImagePath = minioProperties.getEndpoint() + minioProperties.getUserImageBucketName() + "/" + userImagePathName;
            userMapper.updateUserImageById(userId,newImagePath);
        } catch (Exception e) {
            ExceptionTool.throwException("头像上传失败!", "7000");
        }


        return ResultBuilder.aResult().msg("上传成功").code("2000").build();
    }





    /**
     * 个人注册
     *
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
    @Transactional
    @Override
    public Result individualRegist(String userName,
                                   String phone,
                                   String password,
                                   String country,
                                   String idNumber,
                                   String certificateStartDate,
                                   String certificateEndDate,
                                   Integer certificateTypeId) {
        User user = new User();
        user.setUserName(userName);
        user.setPhone(phone);
        user.setPassword(password);

        user.setCreateTime(DataConversionUtil.getTodayAllStr(new Date()));
        user.setUpdateTime("");
        user.setUserImage("");
        user.setLoginDate("");
        user.setStatus("0");
        user.setDelFlag("0");
        user.setUserTypeId(2L);

        try {
            userMapper.addUser(user);
            userMapper.updateCreateByByUserId(user.getUserId());
        } catch (DuplicateKeyException e) {
            User userDuplicate = userMapper.getUserByUserName(userName);
            if(null!=userDuplicate){
                ExceptionTool.throwException("姓名已被其他账号注册!", "7000");
            }
            ExceptionTool.throwException("手机号已被其他账号注册!", "7000");
        }


        Certificate certificate = new Certificate();
        certificate.setCountry(country);
        certificate.setIdNumber(idNumber);
        certificate.setCertificateStartDate(certificateStartDate);
        certificate.setCertificateEndDate(certificateEndDate);
        certificate.setCertificateTypeId(certificateTypeId);
        try {
            certificateMapper.addCertificate(certificate);
        } catch (DuplicateKeyException e) {
            ExceptionTool.throwException("证件号码已被其他账号注册!", "7000");
        }


        Ucertificate ucertificate = new Ucertificate();
        ucertificate.setUserId(user.getUserId());
        ucertificate.setCertificateId(certificate.getCertificateId());
        ucertificateMapper.addUcertificate(ucertificate);

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(2L);
        userRoleMapper.addUserRole(userRole);
        //redisCache.deleteObject(TencentCloudConstant.REGISTRATION_PRE + phone);
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();
    }



}
