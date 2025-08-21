package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * using_apply_file
 * @author 
 */
@Data
public class UsingApplyFile {
    private Long id;

    /**
     * 申请Id
     */
    private Long usingApplyId;

    /**
     * 文件名称
     */
    private String usingApplyFileName;

    /**
     * 文件url路径
     */
    private String usingApplyFileUrl;


}