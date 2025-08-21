package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * certificate
 * @author 
 */
@Data
public class Certificate  {
    private Long certificateId;

    /**
     * 国家/地区
     */
    private String country;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 证件有效期开始日期(yyyy.MM.dd)
     */
    private String certificateStartDate;

    /**
     * 证件有效期结束日期(yyyy.MM.dd)
     */
    private String certificateEndDate;

    /**
     * 证件类型ID
     */
    private Integer certificateTypeId;


}