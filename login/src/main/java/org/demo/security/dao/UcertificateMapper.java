package org.demo.security.dao;

import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.Ucertificate;

public interface UcertificateMapper {

    void addUcertificate(@Param("ucertificate") Ucertificate ucertificate);
}