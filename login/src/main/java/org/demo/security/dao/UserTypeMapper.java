package org.demo.security.dao;

import io.lettuce.core.dynamic.annotation.Param;
import org.demo.security.entity.UserType;

import java.util.List;

public interface UserTypeMapper {
    Boolean getHasProgenyByUserTypeId(@Param("userTypeId")Long userTypeId);

    void delUserTypeByUserTypeId(@Param("userTypeId")Long userTypeId);

    List<UserType> getUserTypeByParentIdAndOrderNum(@Param("parentId")Long parentId, @Param("orderNum") Integer orderNum);

    void increaseUserTypeOrderNumByParentId(@Param("parentId")Long parentId,@Param("orderNum") Integer orderNum);

    void increaseUserTypeOrderNumByParentId2(@Param("parentId") Long parentId,@Param("newOrderNum") Integer newOrderNum,@Param("tarOrderNum") Integer tarOrderNum);



    void addUserType(@Param("userType") UserType userType);

    UserType getUserTypeByUserTypeId(@Param("userTypeId")Long userTypeId);


    void updateUserType(@Param("UserType")UserType UserType);
}