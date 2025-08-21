package org.demo.security.dao;

import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.LegalPerson;

public interface LegalPersonMapper {

    void addLegalPerson(@Param("legalPerson") LegalPerson legalPerson);
}