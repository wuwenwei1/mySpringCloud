package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * data_remittance_file
 * @author 
 */
@Data
public class DataRemittanceFile {
    private Long id;

    /**
     * 汇交数据Id
     */
    private Long remittanceId;

    /**
     * 文件名称
     */
    private String remittanceFileName;

    /**
     * 文件url路径
     */
    private String remittanceFileUrl;


}