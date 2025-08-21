package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * remittance_vs_review
 * @author 
 */
@Data
public class RemittanceVsReview  {
    private Long id;

    /**
     * 汇交数据Id
     */
    private Long remittanceId;

    /**
     * 汇交数据的副本Id
     */
    private Long reviewId;


}