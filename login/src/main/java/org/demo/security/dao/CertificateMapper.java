package org.demo.security.dao;


import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.Certificate;

public interface CertificateMapper {

    void addCertificate(@Param("certificate") Certificate certificate);
}