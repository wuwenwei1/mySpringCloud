package org.demo.security.dao;

import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.UlegalPerson;

public interface UlegalPersonMapper {

    void addUlegalPerson(@Param("ulegalPerson") UlegalPerson ulegalPerson);
}