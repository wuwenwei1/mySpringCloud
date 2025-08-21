package org.demo.security.dao;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface RoleUserTypeMapper {

    void delByRoleId(@Param("roleId") Long roleId);

    void addRoleUserType(@Param("roleId")Long roleId,@Param("userTypeIds") List<Long> userTypeIds);

    void delByUserTypeId(@Param("userTypeId")Long userTypeId);
}