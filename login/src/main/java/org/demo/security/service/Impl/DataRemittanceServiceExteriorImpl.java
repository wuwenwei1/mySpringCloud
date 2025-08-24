package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import io.minio.*;
import jakarta.annotation.Resource;
import org.demo.security.authentication.config.MinioProperties;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.common.web.util.UUID;
import org.demo.security.dao.*;
import org.demo.security.entity.*;
import org.demo.security.service.DataRemittanceServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static java.awt.SystemColor.menu;

@Service
public class DataRemittanceServiceExteriorImpl implements DataRemittanceServiceExterior {
    @Autowired
    private DataRemittanceMapper dataRemittanceMapper;
    @Autowired
    private DataRemittanceFileMapper dataRemittanceFileMapper;
    @Autowired
    private DataRemittanceReviewMapper dataRemittanceReviewMapper;
    @Autowired
    private DataRemittanceReviewFileMapper dataRemittanceReviewFileMapper;
    @Autowired
    private RemittanceVsReviewMapper remittanceVsReviewMapper;
    @Autowired
    private ReviewAuditRecordMapper reviewAuditRecordMapper;
    @Resource
    private MinioClient minioclient;
    @Autowired
    private MinioProperties minioProperties;

    /**
     * 修改数据共享
     * @param id
     * @param isShare
     * @return
     */
    @Transactional
    @Override
    public Result updateShareById(Long id, Integer isShare) {
        DataRemittance oldDataRemittance = dataRemittanceMapper.getDataRemittanceObjectById(id);
        if(isShare!=oldDataRemittance.getIsShare()){
            Long userId = SecurityUtils.getUserId();
            String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
            dataRemittanceMapper.updateIsShareById(id,isShare,userId,todayAllStr);
        }
        return ResultBuilder.aResult().msg("设置成功!").code("2000").build();
    }


    /**
     * 汇交数据管理列表
     * @param dataName
     * @param industryTypeId
     * @param subjectAreaId
     * @param dataOpenTypeId
     * @param reviewTypeId
     * @param createStartTime
     * @param createEndTime
     * @param starIndex
     * @param pageSize
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceList(String dataName, Long industryTypeId, Long subjectAreaId, Long dataOpenTypeId, Long reviewTypeId, String createStartTime, String createEndTime, Integer starIndex,Integer pageSize) {
        List<JSONObject> dataRemittanceList=dataRemittanceMapper.getDataRemittanceList(dataName,industryTypeId,subjectAreaId,dataOpenTypeId,reviewTypeId,createStartTime,createEndTime,starIndex,pageSize);
        Long total=dataRemittanceMapper.getDataRemittanceTotal(dataName,industryTypeId,subjectAreaId,dataOpenTypeId,reviewTypeId,createStartTime,createEndTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",dataRemittanceList);
        jsonObject.put("total",total);
        return ResultBuilder.aResult().data(jsonObject).code("2000").build();
    }

    /**
     * 更新提交数据汇交
     *
     * @param dataName
     * @param dataName
     * @param dataOpenTypeId
     * @param subjectAreaId
     * @param industryTypeId
     * @param dataType
     * @param dataFormat
     * @param remark
     * @return
     */
    @Transactional
    @Override
    public Result updateDataRemittance(Long id, String dataName, Long dataOpenTypeId, Long subjectAreaId, Long industryTypeId, String dataType, String dataFormat, String remark, List<MultipartFile> files) {
        Long userId = SecurityUtils.getUserId();
        DataRemittance oldDataRemittance = dataRemittanceMapper.getDataRemittanceByIdAndLockIn(id);
        if (null == oldDataRemittance) {
            ExceptionTool.throwException("数据不存在!", "7000");
        } else {
            if (3 != oldDataRemittance.getReviewTypeId()) {
                ExceptionTool.throwException("不允许修改!", "7000");
            } else {
                String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
                DataRemittance dataRemittance = new DataRemittance();
                dataRemittance.setId(id);
                dataRemittance.setDataName(dataName);
                dataRemittance.setDataOpenTypeId(dataOpenTypeId);
                dataRemittance.setSubjectAreaId(subjectAreaId);
                dataRemittance.setIndustryTypeId(industryTypeId);
                dataRemittance.setReviewTypeId(1L);
                dataRemittance.setRemittanceTypeId(2L);
                dataRemittance.setIsShare(2);
                dataRemittance.setUpdateBy(userId);
                dataRemittance.setUpdateTime(todayAllStr);
                dataRemittance.setDataType(dataType);
                dataRemittance.setDataFormat(dataFormat);
                dataRemittance.setRemark(remark);
                dataRemittanceMapper.updateDataRemittanceById(dataRemittance);

                DataRemittanceReview dataRemittanceReview = new DataRemittanceReview();
                dataRemittanceReview.setRemittanceId(id);
                dataRemittanceReview.setDataName(dataName);
                dataRemittanceReview.setIndustryTypeId(industryTypeId);
                dataRemittanceReview.setSubjectAreaId(subjectAreaId);
                dataRemittanceReview.setDataOpenTypeId(dataOpenTypeId);
                dataRemittanceReview.setReviewTypeId(1L);

                dataRemittanceReview.setCreateBy(oldDataRemittance.getCreateBy());
                dataRemittanceReview.setCreateTime(oldDataRemittance.getCreateTime());
                dataRemittanceReview.setUpdateBy(userId);
                dataRemittanceReview.setUpdateTime(todayAllStr);
                dataRemittanceReview.setDataType(dataType);
                dataRemittanceReview.setDataFormat(dataFormat);
                dataRemittanceReview.setRemark(remark);
                dataRemittanceReview.setRemittanceTypeId(2L);

                dataRemittanceReviewMapper.addDataRemittanceReview(dataRemittanceReview);



                remittanceVsReviewMapper.updateRemittanceVsReviewByRemittanceId(dataRemittance.getId(),dataRemittanceReview.getId());

                ReviewAuditRecord reviewAuditRecord=new ReviewAuditRecord();
                reviewAuditRecord.setReviewId(dataRemittanceReview.getId());
                reviewAuditRecordMapper.addReviewAuditRecord(reviewAuditRecord);



                if (null != files && files.size() > 0) {
                    try {
                        // 检查指定的存储桶是否存在
                        boolean dataRemittanceBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getDataRemittanceBucketName()).build());
                        if (!dataRemittanceBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                            minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getDataRemittanceBucketName()).build());
                            // 设置存储桶策略，允许匿名用户读取对象
                            minioclient.setBucketPolicy(SetBucketPolicyArgs.
                                    builder().
                                    bucket(minioProperties.getDataRemittanceBucketName()). // 指定存储桶名称
                                            config(createBucketPolicyConfig(minioProperties.getDataRemittanceBucketName())). // 指定存储桶策略配置
                                            build());
                        }

                        boolean dataRemittanceReviewBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getDataRemittanceReviewBucketName()).build());
                        if (!dataRemittanceReviewBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                            minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getDataRemittanceReviewBucketName()).build());
                            // 设置存储桶策略，允许匿名用户读取对象
                            minioclient.setBucketPolicy(SetBucketPolicyArgs.
                                    builder().
                                    bucket(minioProperties.getDataRemittanceReviewBucketName()). // 指定存储桶名称
                                            config(createBucketPolicyConfig(minioProperties.getDataRemittanceReviewBucketName())). // 指定存储桶策略配置
                                            build());
                        }


                        Long dataRemittanceFileMaxId = dataRemittanceFileMapper.getMaxIdByRemittanceId(id);


                        for (MultipartFile m : files) {
                            DataRemittanceFile dataRemittanceFile = new DataRemittanceFile();
                            dataRemittanceFile.setRemittanceId(dataRemittance.getId());
                            dataRemittanceFileMapper.addDataRemittanceFile(dataRemittanceFile);
                            // 构建文件名，包含日期和UUID，确保文件名的唯一性
                            String dataRemittanceFileName = String.valueOf(dataRemittance.getId()) + "/" + String.valueOf(dataRemittanceFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                            minioclient.putObject(PutObjectArgs.builder()
                                    .bucket(minioProperties.getDataRemittanceBucketName())    //把文件上传到名为myfile的存储桶中
                                    .object(dataRemittanceFileName) //上传之后的文件的新名称
                                    .contentType(m.getContentType())
                                    .stream(m.getInputStream(), m.getSize(), -1)
                                    .build());
                            String dataRemittanceFileUrl = minioProperties.getEndpoint() + minioProperties.getDataRemittanceBucketName() + "/" + dataRemittanceFileName;
                            dataRemittanceFile.setRemittanceFileName(m.getOriginalFilename());
                            dataRemittanceFile.setRemittanceFileUrl(dataRemittanceFileUrl);
                            dataRemittanceFileMapper.updateDataRemittanceFileById(dataRemittanceFile);


                            DataRemittanceReviewFile dataRemittanceReviewFile = new DataRemittanceReviewFile();
                            dataRemittanceReviewFile.setRemittanceReviewId(dataRemittanceReview.getId());
                            dataRemittanceReviewFileMapper.addDataRemittanceReviewFile(dataRemittanceReviewFile);
                            // 构建文件名，包含日期和UUID，确保文件名的唯一性
                            String dataRemittanceReviewFileName = String.valueOf(dataRemittance.getId()) + "/" + String.valueOf(dataRemittanceReview.getId()) + "/" + String.valueOf(dataRemittanceReviewFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                            minioclient.putObject(PutObjectArgs.builder()
                                    .bucket(minioProperties.getDataRemittanceReviewBucketName())    //把文件上传到名为myfile的存储桶中
                                    .object(dataRemittanceReviewFileName) //上传之后的文件的新名称
                                    .contentType(m.getContentType())
                                    .stream(m.getInputStream(), m.getSize(), -1)
                                    .build());
                            String dataRemittanceReviewFileUrl = minioProperties.getEndpoint() + minioProperties.getDataRemittanceReviewBucketName() + "/" + dataRemittanceReviewFileName;
                            dataRemittanceReviewFile.setRemittanceReviewFileName(m.getOriginalFilename());
                            dataRemittanceReviewFile.setRemittanceReviewFileUrl(dataRemittanceReviewFileUrl);
                            dataRemittanceReviewFileMapper.updateDataRemittanceReviewFileById(dataRemittanceReviewFile);
                        }
                        if (null != dataRemittanceFileMaxId && 0 != dataRemittanceFileMaxId) {
                            dataRemittanceFileMapper.delByIdAndRemittanceId(dataRemittanceFileMaxId, id);
                        }
                    } catch (Exception e) {
                        ExceptionTool.throwException("文件上传失败!", "7000");
                    }
                }
            }
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }


    /**
     * 重新提交数据汇交
     *
     * @param dataName
     * @param dataName
     * @param dataOpenTypeId
     * @param subjectAreaId
     * @param industryTypeId
     * @param dataType
     * @param dataFormat
     * @param remark
     * @return
     */
    @Transactional
    @Override
    public Result afreshAddDataRemittance(Long id, String dataName, Long dataOpenTypeId, Long subjectAreaId, Long industryTypeId, String dataType, String dataFormat, String remark, List<MultipartFile> files) {
        Long userId = SecurityUtils.getUserId();
        DataRemittance oldDataRemittance = dataRemittanceMapper.getDataRemittanceByIdAndLockIn(id);
        if (null == oldDataRemittance) {
            ExceptionTool.throwException("数据不存在!", "7000");
        } else {
            if (2 != oldDataRemittance.getReviewTypeId()) {
                ExceptionTool.throwException("不允许重新提交!", "7000");
            } else {
                String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
                DataRemittance dataRemittance = new DataRemittance();
                dataRemittance.setId(id);
                dataRemittance.setDataName(dataName);
                dataRemittance.setDataOpenTypeId(dataOpenTypeId);
                dataRemittance.setSubjectAreaId(subjectAreaId);
                dataRemittance.setIndustryTypeId(industryTypeId);
                dataRemittance.setReviewTypeId(2L);
                dataRemittance.setRemittanceTypeId(3L);
                dataRemittance.setIsShare(2);
                dataRemittance.setUpdateBy(userId);
                dataRemittance.setUpdateTime(todayAllStr);
                dataRemittance.setDataType(dataType);
                dataRemittance.setDataFormat(dataFormat);
                dataRemittance.setRemark(remark);
                dataRemittanceMapper.updateDataRemittanceById(dataRemittance);

                DataRemittanceReview dataRemittanceReview = new DataRemittanceReview();
                dataRemittanceReview.setRemittanceId(id);
                dataRemittanceReview.setDataName(dataName);
                dataRemittanceReview.setIndustryTypeId(industryTypeId);
                dataRemittanceReview.setSubjectAreaId(subjectAreaId);
                dataRemittanceReview.setDataOpenTypeId(dataOpenTypeId);
                dataRemittanceReview.setReviewTypeId(1L);
                dataRemittanceReview.setCreateBy(oldDataRemittance.getCreateBy());
                dataRemittanceReview.setCreateTime(oldDataRemittance.getCreateTime());
                dataRemittanceReview.setUpdateBy(userId);
                dataRemittanceReview.setUpdateTime(todayAllStr);
                dataRemittanceReview.setDataType(dataType);
                dataRemittanceReview.setDataFormat(dataFormat);
                dataRemittanceReview.setRemark(remark);
                dataRemittanceReview.setRemittanceTypeId(3L);
                dataRemittanceReviewMapper.addDataRemittanceReview(dataRemittanceReview);

                remittanceVsReviewMapper.updateRemittanceVsReviewByRemittanceId(dataRemittance.getId(),dataRemittanceReview.getId());

                ReviewAuditRecord reviewAuditRecord=new ReviewAuditRecord();
                reviewAuditRecord.setReviewId(dataRemittanceReview.getId());
                reviewAuditRecordMapper.addReviewAuditRecord(reviewAuditRecord);

                if (null != files && files.size() > 0) {
                    try {
                        // 检查指定的存储桶是否存在
                        boolean dataRemittanceBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getDataRemittanceBucketName()).build());
                        if (!dataRemittanceBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                            minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getDataRemittanceBucketName()).build());
                            // 设置存储桶策略，允许匿名用户读取对象
                            minioclient.setBucketPolicy(SetBucketPolicyArgs.
                                    builder().
                                    bucket(minioProperties.getDataRemittanceBucketName()). // 指定存储桶名称
                                            config(createBucketPolicyConfig(minioProperties.getDataRemittanceBucketName())). // 指定存储桶策略配置
                                            build());
                        }

                        boolean dataRemittanceReviewBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getDataRemittanceReviewBucketName()).build());
                        if (!dataRemittanceReviewBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                            minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getDataRemittanceReviewBucketName()).build());
                            // 设置存储桶策略，允许匿名用户读取对象
                            minioclient.setBucketPolicy(SetBucketPolicyArgs.
                                    builder().
                                    bucket(minioProperties.getDataRemittanceReviewBucketName()). // 指定存储桶名称
                                            config(createBucketPolicyConfig(minioProperties.getDataRemittanceReviewBucketName())). // 指定存储桶策略配置
                                            build());
                        }
                        Long dataRemittanceFileMaxId = dataRemittanceFileMapper.getMaxIdByRemittanceId(id);
                        for (MultipartFile m : files) {
                            DataRemittanceFile dataRemittanceFile = new DataRemittanceFile();
                            dataRemittanceFile.setRemittanceId(dataRemittance.getId());
                            dataRemittanceFileMapper.addDataRemittanceFile(dataRemittanceFile);
                            // 构建文件名，包含日期和UUID，确保文件名的唯一性
                            String dataRemittanceFileName = String.valueOf(dataRemittance.getId()) + "/" + String.valueOf(dataRemittanceFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                            minioclient.putObject(PutObjectArgs.builder()
                                    .bucket(minioProperties.getDataRemittanceBucketName())    //把文件上传到名为myfile的存储桶中
                                    .object(dataRemittanceFileName) //上传之后的文件的新名称
                                    .contentType(m.getContentType())
                                    .stream(m.getInputStream(), m.getSize(), -1)
                                    .build());
                            String dataRemittanceFileUrl = minioProperties.getEndpoint() + minioProperties.getDataRemittanceBucketName() + "/" + dataRemittanceFileName;
                            dataRemittanceFile.setRemittanceFileName(m.getOriginalFilename());
                            dataRemittanceFile.setRemittanceFileUrl(dataRemittanceFileUrl);
                            dataRemittanceFileMapper.updateDataRemittanceFileById(dataRemittanceFile);


                            DataRemittanceReviewFile dataRemittanceReviewFile = new DataRemittanceReviewFile();
                            dataRemittanceReviewFile.setRemittanceReviewId(dataRemittanceReview.getId());
                            dataRemittanceReviewFileMapper.addDataRemittanceReviewFile(dataRemittanceReviewFile);
                            // 构建文件名，包含日期和UUID，确保文件名的唯一性
                            String dataRemittanceReviewFileName = String.valueOf(dataRemittance.getId()) + "/" + String.valueOf(dataRemittanceReview.getId()) + "/" + String.valueOf(dataRemittanceReviewFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                            minioclient.putObject(PutObjectArgs.builder()
                                    .bucket(minioProperties.getDataRemittanceReviewBucketName())    //把文件上传到名为myfile的存储桶中
                                    .object(dataRemittanceReviewFileName) //上传之后的文件的新名称
                                    .contentType(m.getContentType())
                                    .stream(m.getInputStream(), m.getSize(), -1)
                                    .build());
                            String dataRemittanceReviewFileUrl = minioProperties.getEndpoint() + minioProperties.getDataRemittanceReviewBucketName() + "/" + dataRemittanceReviewFileName;
                            dataRemittanceReviewFile.setRemittanceReviewFileName(m.getOriginalFilename());
                            dataRemittanceReviewFile.setRemittanceReviewFileUrl(dataRemittanceReviewFileUrl);
                            dataRemittanceReviewFileMapper.updateDataRemittanceReviewFileById(dataRemittanceReviewFile);
                        }
                        if (null != dataRemittanceFileMaxId && 0 != dataRemittanceFileMaxId) {
                            dataRemittanceFileMapper.delByIdAndRemittanceId(dataRemittanceFileMaxId, id);
                        }
                    } catch (Exception e) {
                        ExceptionTool.throwException("文件上传失败!", "7000");
                    }
                }
            }
        }


        return ResultBuilder.aResult().msg("重提成功").code("2000").build();
    }


    /**
     * 添加数据汇交
     *
     * @param dataName
     * @param dataOpenTypeId
     * @param subjectAreaId
     * @param industryTypeId
     * @param dataType
     * @param dataFormat
     * @param remark
     * @return
     */
    @Transactional
    @Override
    public Result addDataRemittance(String dataName, Long dataOpenTypeId, Long subjectAreaId, Long industryTypeId, String dataType, String dataFormat, String remark, List<MultipartFile> files) {
        Long userId = SecurityUtils.getUserId();
        String todayAllStr = DataConversionUtil.getTodayAllStr(new Date());
        DataRemittance dataRemittance = new DataRemittance();
        dataRemittance.setDataName(dataName);
        dataRemittance.setDataOpenTypeId(dataOpenTypeId);
        dataRemittance.setSubjectAreaId(subjectAreaId);
        dataRemittance.setIndustryTypeId(industryTypeId);
        dataRemittance.setReviewTypeId(1L);
        dataRemittance.setRemittanceTypeId(1L);
        dataRemittance.setIsShare(2);
        dataRemittance.setCreateBy(userId);
        dataRemittance.setCreateTime(todayAllStr);
        dataRemittance.setUpdateBy(userId);
        dataRemittance.setUpdateTime(todayAllStr);
        dataRemittance.setDataType(dataType);
        dataRemittance.setDataFormat(dataFormat);
        dataRemittance.setRemark(remark);
        dataRemittanceMapper.addDataRemittance(dataRemittance);

        DataRemittanceReview dataRemittanceReview = new DataRemittanceReview();
        dataRemittanceReview.setRemittanceId(dataRemittance.getId());
        dataRemittanceReview.setDataName(dataName);
        dataRemittanceReview.setIndustryTypeId(industryTypeId);
        dataRemittanceReview.setSubjectAreaId(subjectAreaId);
        dataRemittanceReview.setDataOpenTypeId(dataOpenTypeId);
        dataRemittanceReview.setReviewTypeId(1L);

        dataRemittanceReview.setCreateBy(userId);
        dataRemittanceReview.setCreateTime(todayAllStr);
        dataRemittanceReview.setUpdateBy(userId);
        dataRemittanceReview.setUpdateTime(todayAllStr);
        dataRemittanceReview.setDataType(dataType);
        dataRemittanceReview.setDataFormat(dataFormat);
        dataRemittanceReview.setRemark(remark);
        dataRemittanceReview.setRemittanceTypeId(1L);
        dataRemittanceReviewMapper.addDataRemittanceReview(dataRemittanceReview);

        RemittanceVsReview remittanceVsReview = new RemittanceVsReview();
        remittanceVsReview.setRemittanceId(dataRemittance.getId());
        remittanceVsReview.setReviewId(dataRemittanceReview.getId());
        remittanceVsReviewMapper.addRemittanceVsReview(remittanceVsReview);


        ReviewAuditRecord reviewAuditRecord=new ReviewAuditRecord();
        reviewAuditRecord.setReviewId(dataRemittanceReview.getId());
        reviewAuditRecordMapper.addReviewAuditRecord(reviewAuditRecord);

        if (null != files && files.size() > 0) {
            try {
                // 检查指定的存储桶是否存在
                boolean dataRemittanceBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getDataRemittanceBucketName()).build());
                if (!dataRemittanceBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                    minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getDataRemittanceBucketName()).build());
                    // 设置存储桶策略，允许匿名用户读取对象
                    minioclient.setBucketPolicy(SetBucketPolicyArgs.
                            builder().
                            bucket(minioProperties.getDataRemittanceBucketName()). // 指定存储桶名称
                                    config(createBucketPolicyConfig(minioProperties.getDataRemittanceBucketName())). // 指定存储桶策略配置
                                    build());
                }

                boolean dataRemittanceReviewBucketNameIsBucketExists = minioclient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getDataRemittanceReviewBucketName()).build());
                if (!dataRemittanceReviewBucketNameIsBucketExists) {// 如果存储桶不存在，则创建存储桶
                    minioclient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getDataRemittanceReviewBucketName()).build());
                    // 设置存储桶策略，允许匿名用户读取对象
                    minioclient.setBucketPolicy(SetBucketPolicyArgs.
                            builder().
                            bucket(minioProperties.getDataRemittanceReviewBucketName()). // 指定存储桶名称
                                    config(createBucketPolicyConfig(minioProperties.getDataRemittanceReviewBucketName())). // 指定存储桶策略配置
                                    build());
                }

                for (MultipartFile m : files) {
                    DataRemittanceFile dataRemittanceFile = new DataRemittanceFile();
                    dataRemittanceFile.setRemittanceId(dataRemittance.getId());
                    dataRemittanceFileMapper.addDataRemittanceFile(dataRemittanceFile);
                    // 构建文件名，包含日期和UUID，确保文件名的唯一性
                    String dataRemittanceFileName = String.valueOf(dataRemittance.getId()) + "/" + String.valueOf(dataRemittanceFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                    minioclient.putObject(PutObjectArgs.builder()
                            .bucket(minioProperties.getDataRemittanceBucketName())    //把文件上传到名为myfile的存储桶中
                            .object(dataRemittanceFileName) //上传之后的文件的新名称
                            .contentType(m.getContentType())
                            .stream(m.getInputStream(), m.getSize(), -1)
                            .build());
                    String dataRemittanceFileUrl = minioProperties.getEndpoint() + minioProperties.getDataRemittanceBucketName() + "/" + dataRemittanceFileName;
                    dataRemittanceFile.setRemittanceFileName(m.getOriginalFilename());
                    dataRemittanceFile.setRemittanceFileUrl(dataRemittanceFileUrl);
                    dataRemittanceFileMapper.updateDataRemittanceFileById(dataRemittanceFile);


                    DataRemittanceReviewFile dataRemittanceReviewFile = new DataRemittanceReviewFile();
                    dataRemittanceReviewFile.setRemittanceReviewId(dataRemittanceReview.getId());
                    dataRemittanceReviewFileMapper.addDataRemittanceReviewFile(dataRemittanceReviewFile);

                    // 构建文件名，包含日期和UUID，确保文件名的唯一性
                    String dataRemittanceReviewFileName = String.valueOf(dataRemittance.getId()) + "/" + String.valueOf(dataRemittanceReview.getId()) + "/" + String.valueOf(dataRemittanceReviewFile.getId()) + "/" + UUID.randomUUID() + "-" + m.getOriginalFilename();
                    minioclient.putObject(PutObjectArgs.builder()
                            .bucket(minioProperties.getDataRemittanceReviewBucketName())    //把文件上传到名为myfile的存储桶中
                            .object(dataRemittanceReviewFileName) //上传之后的文件的新名称
                            .contentType(m.getContentType())
                            .stream(m.getInputStream(), m.getSize(), -1)
                            .build());
                    String dataRemittanceReviewFileUrl = minioProperties.getEndpoint() + minioProperties.getDataRemittanceReviewBucketName() + "/" + dataRemittanceReviewFileName;

                    dataRemittanceReviewFile.setRemittanceReviewFileName(m.getOriginalFilename());
                    dataRemittanceReviewFile.setRemittanceReviewFileUrl(dataRemittanceReviewFileUrl);
                    dataRemittanceReviewFileMapper.updateDataRemittanceReviewFileById(dataRemittanceReviewFile);
                }
            } catch (Exception e) {
                ExceptionTool.throwException("文件上传失败!", "7000");
            }
        }
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();
    }





    /**
     * 查看汇交数据的审核信息
     * @param remittanceId
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittanceReviewData(Long remittanceId) {
        JSONObject reviewData=remittanceVsReviewMapper.getDataRemittanceReviewDataByRemittanceId(remittanceId);
        return ResultBuilder.aResult().code("2000").data(reviewData).build();
    }

    // 创建存储桶策略配置，允许匿名用户读取存储桶中的对象
    private String createBucketPolicyConfig(String bucketName) {
        // 使用String.format方法格式化存储桶策略配置字符串
        return "{\"Version\": \"2012-10-17\",\"Statement\": [{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
    }

    /**
     * 查看/更新时单个数据汇交回显
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result getDataRemittance(Long id) {
        JSONObject dataRemittance = dataRemittanceMapper.getDataRemittanceById(id);
        return ResultBuilder.aResult().code("2000").data(dataRemittance).build();
    }
//////////////////////////////////////////////




}
