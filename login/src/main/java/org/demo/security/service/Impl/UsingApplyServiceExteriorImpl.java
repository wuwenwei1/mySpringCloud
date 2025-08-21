package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import io.minio.*;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.demo.security.authentication.config.MinioProperties;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.common.web.util.UUID;
import org.demo.security.dao.DataRemittanceMapper;
import org.demo.security.dao.UsingApplyFileMapper;
import org.demo.security.dao.UsingApplyMapper;
import org.demo.security.entity.*;
import org.demo.security.service.UsingApplyServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.List;

@Service
public class UsingApplyServiceExteriorImpl implements UsingApplyServiceExterior {
    @Autowired
    private UsingApplyMapper usingApplyMapper;
    @Autowired
    private DataRemittanceMapper dataRemittanceMapper;
    @Autowired
    private MinioProperties minioProperties;
    @Resource
    private MinioClient minioclient;

    @Autowired
    private UsingApplyFileMapper usingApplyFileMapper;

    /**
     * 查看数据使用申请的审核数据
     *
     * @param id
     * @return
     */
    @Override
    public Result getUsingApplyReviewData(Long id) {
        JSONObject reviewData = usingApplyMapper.getUsingApplyReviewDataById(id);
        return ResultBuilder.aResult().code("2000").data(reviewData).build();
    }


    /**
     * 查看数据申请详情的回显
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result getUsingApply(Long id) {
        UsingApply usingApply = usingApplyMapper.getUsingApplyById(id);
        return ResultBuilder.aResult().code("2000").data(usingApply).build();
    }


    // 创建存储桶策略配置，允许匿名用户读取存储桶中的对象
    private String createBucketPolicyConfig(String bucketName) {
        // 使用String.format方法格式化存储桶策略配置字符串
        return "{\"Version\": \"2012-10-17\",\"Statement\": [{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
    }


    /**
     * 重新提交数据使用申请
     *
     * @param remittanceId
     * @param contactNumber
     * @param email
     * @param contactAddress
     * @param usageScenarios
     * @param files
     * @return
     */
    @Override
    public Result afreshAddUsingApply(Long id, Long remittanceId, String contactNumber, String email, String contactAddress, String usageScenarios, List<MultipartFile> files) {
        Long userId = SecurityUtils.getUserId();
        UsingApply oldUsingApply = usingApplyMapper.getUsingApplyById(id);
        if (null == oldUsingApply) {
            return ResultBuilder.aResult().msg("上次申请不存在,重提失败!").code("7000").build();
        } else {
            DataRemittance oldDataRemittance = dataRemittanceMapper.getDataRemittanceByIdAndLockIn(remittanceId);
            if (null == oldDataRemittance) {
                return ResultBuilder.aResult().msg("汇交数据不存在,重提失败!").code("7000").build();
            } else {
                if (oldUsingApply.getRemittanceId() != remittanceId) {
                    return ResultBuilder.aResult().msg("与上次申请的汇交数据不同,重提失败!").code("7000").build();
                } else {
                    if (1L == oldUsingApply.getReviewTypeId()) {
                        return ResultBuilder.aResult().msg("数据还未审核,无法重新提交!").code("7000").build();
                    } else if (3L == oldUsingApply.getReviewTypeId()) {
                        return ResultBuilder.aResult().msg("数据已审核通过,无法重新提交!").code("7000").build();
                    } else if (2L == oldUsingApply.getReviewTypeId()) {
                        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
                        UsingApply usingApply = new UsingApply();
                        usingApply.setRemittanceId(remittanceId);
                        usingApply.setApplyBy(userId);
                        usingApply.setApplyTime(todayAllStr);
                        usingApply.setContactNumber(contactNumber);
                        usingApply.setEmail(email);
                        usingApply.setUsageScenarios(usageScenarios);
                        usingApply.setContactAddress(contactAddress);
                        usingApply.setReviewTypeId(1L);
                        usingApply.setReviewTime("");
                        usingApply.setReviewIdea("");
                        usingApplyMapper.addUsingApply(usingApply);
                        if (null != files && files.size() > 0) {
                            try {
                                // 检查指定的存储桶是否存在
                                boolean usingApplyBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getUsingApplyBucketName()).build());
                                if (!usingApplyBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                                    minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getUsingApplyBucketName()).build());
                                    // 设置存储桶策略，允许匿名用户读取对象
                                    minioclient.setBucketPolicy(SetBucketPolicyArgs.
                                            builder().
                                            bucket(minioProperties.getUsingApplyBucketName()). // 指定存储桶名称
                                                    config(createBucketPolicyConfig(minioProperties.getUsingApplyBucketName())). // 指定存储桶策略配置
                                                    build());
                                }

                                for (MultipartFile m : files) {
                                    UsingApplyFile usingApplyFile = new UsingApplyFile();
                                    usingApplyFile.setUsingApplyId(usingApply.getId());
                                    usingApplyFileMapper.addUsingApplyFile(usingApplyFile);
                                    // 构建文件名，包含日期和UUID，确保文件名的唯一性
                                    String usingApplyFileName = String.valueOf(usingApply.getId()) + "/" + String.valueOf(usingApplyFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                                    minioclient.putObject(PutObjectArgs.builder()
                                            .bucket(minioProperties.getUsingApplyBucketName())    //把文件上传到名为myfile的存储桶中
                                            .object(usingApplyFileName) //上传之后的文件的新名称
                                            .contentType(m.getContentType())
                                            .stream(m.getInputStream(), m.getSize(), -1)
                                            .build());
                                    String usingApplyFileUrl = minioProperties.getEndpoint() + minioProperties.getUsingApplyBucketName() + "/" + usingApplyFileName;
                                    usingApplyFile.setUsingApplyFileName(m.getOriginalFilename());
                                    usingApplyFile.setUsingApplyFileUrl(usingApplyFileUrl);
                                    usingApplyFileMapper.updateUsingApplyFileById(usingApplyFile);
                                }
                            } catch (Exception e) {
                                ExceptionTool.throwException("文件上传失败!", "7000");
                            }
                        }
                        return ResultBuilder.aResult().msg("重提申请成功!").code("2000").build();
                    } else {
                        return ResultBuilder.aResult().msg("重提错误!").code("7000").build();
                    }
                }
            }
        }
    }

    /**
     * 对使用数据申请进行审核
     * @param id
     * @param reviewIdea
     * @param reviewTypeId
     * @return
     */
    @Transactional
    @Override
    public Result usingApplyReview(Long id, String reviewIdea, Long reviewTypeId) {
        UsingApply oldUsingApply=usingApplyMapper.getUsingApplyById(id);
        if(null!=oldUsingApply){
            if(1L!=oldUsingApply.getReviewTypeId()){
                return ResultBuilder.aResult().msg("数据已审核过,请不要重复审核!").code("7000").build();
            }
            Long userId = SecurityUtils.getUserId();
            UsingApply usingApply=new UsingApply();
            usingApply.setId(id);
            usingApply.setReviewTypeId(reviewTypeId);
            usingApply.setReviewBy(userId);
            usingApply.setReviewTime(DataConversionUtil.getTodayAllStr(new Date()));
            usingApply.setReviewIdea(reviewIdea);
            usingApplyMapper.usingApplyReview(usingApply);
            return ResultBuilder.aResult().msg("审核成功").code("2000").build();
        }else {
            return ResultBuilder.aResult().msg("申请的数据不存在!").code("7000").build();
        }
    }


    /**
     * 添加数据使用申请
     *
     * @param contactNumber
     * @param email
     * @param contactAddress
     * @param usageScenarios
     * @return
     */
    @Transactional
    @Override
    public Result addUsingApply(Long remittanceId, String contactNumber, String email, String contactAddress, String usageScenarios, List<MultipartFile> files) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        UsingApply usingApply = new UsingApply();
        usingApply.setRemittanceId(remittanceId);
        usingApply.setApplyBy(userId);
        usingApply.setApplyTime(todayAllStr);
        usingApply.setContactNumber(contactNumber);
        usingApply.setEmail(email);
        usingApply.setUsageScenarios(usageScenarios);
        usingApply.setContactAddress(contactAddress);
        usingApply.setReviewTypeId(1L);
        usingApply.setReviewTime("");
        usingApply.setReviewIdea("");
        usingApplyMapper.addUsingApply(usingApply);


        if (null != files && files.size() > 0) {
            try {
                // 检查指定的存储桶是否存在
                boolean usingApplyBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getUsingApplyBucketName()).build());
                if (!usingApplyBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                    minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getUsingApplyBucketName()).build());
                    // 设置存储桶策略，允许匿名用户读取对象
                    minioclient.setBucketPolicy(SetBucketPolicyArgs.
                            builder().
                            bucket(minioProperties.getUsingApplyBucketName()). // 指定存储桶名称
                                    config(createBucketPolicyConfig(minioProperties.getUsingApplyBucketName())). // 指定存储桶策略配置
                                    build());
                }


                for (MultipartFile m : files) {

                    UsingApplyFile usingApplyFile = new UsingApplyFile();
                    usingApplyFile.setUsingApplyId(usingApply.getId());
                    usingApplyFileMapper.addUsingApplyFile(usingApplyFile);

                    // 构建文件名，包含日期和UUID，确保文件名的唯一性
                    String usingApplyFileName = String.valueOf(usingApply.getId()) + "/" + String.valueOf(usingApplyFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                    minioclient.putObject(PutObjectArgs.builder()
                            .bucket(minioProperties.getUsingApplyBucketName())    //把文件上传到名为myfile的存储桶中
                            .object(usingApplyFileName) //上传之后的文件的新名称
                            .contentType(m.getContentType())
                            .stream(m.getInputStream(), m.getSize(), -1)
                            .build());
                    String usingApplyFileUrl = minioProperties.getEndpoint() + minioProperties.getUsingApplyBucketName() + "/" + usingApplyFileName;
                    usingApplyFile.setUsingApplyFileName(m.getOriginalFilename());
                    usingApplyFile.setUsingApplyFileUrl(usingApplyFileUrl);
                    usingApplyFileMapper.updateUsingApplyFileById(usingApplyFile);

                }
            } catch (Exception e) {
                ExceptionTool.throwException("文件上传失败!", "7000");
            }
        }
        return ResultBuilder.aResult().msg("申请成功").code("2000").build();
    }


}
