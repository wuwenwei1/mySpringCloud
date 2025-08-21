package org.demo.security.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ulegalperson
 * @author 
 */
@Data
public class UlegalPerson {
    private Long ulegalPersonId;

    private Long userId;

    private Long legalPersonId;
}