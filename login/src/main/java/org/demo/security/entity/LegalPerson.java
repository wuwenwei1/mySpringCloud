package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * legalperson
 * @author 
 */
@Data
public class LegalPerson {
    private Long legalPersonId;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 许可证编号
     */
    private String licenseNumber;

    /**
     * 法人姓名
     */
    private String legalPersonName;

    /**
     * 法人类型Id
     */
    private Integer legalPersonTypeId;


}