package org.demo.security.dao;

import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.User;

public interface UserMapper {
    Boolean getHasUserBindingByUserTypeId(@Param("userTypeId")Long userTypeId);

    void addUser(@Param("user") User user);

    User getUserByUserName(@Param("userName") String userName);

    User getUserByPhone(@Param("phone")String phone);




    void updateLoginDateById(@Param("userId")Long userId,@Param("loginDate") String loginDate);

    User getUserByUserId(@Param("userId")Long userId);

    void updatePwdByPhone(@Param("phone")String phone,@Param("newPwd") String newPwd);

    void updateUserImageById(@Param("userId")Long userId,@Param("newImagePath") String newImagePath);


    void updateCreateByByUserId(@Param("userId")Long userId);


}