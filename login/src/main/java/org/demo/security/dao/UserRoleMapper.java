package org.demo.security.dao;

import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.UserRole;

public interface UserRoleMapper {

    void addUserRole(@Param("userRole") UserRole userRole);


}