package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * data_remittance_review_file
 * @author 
 */
@Data
public class DataRemittanceReviewFile {
    private Long id;

    /**
     * 汇交审核数据Id
     */
    private Long remittanceReviewId;

    /**
     * 文件名称
     */
    private String remittanceReviewFileName;

    /**
     * 文件url路径
     */
    private String remittanceReviewFileUrl;


}