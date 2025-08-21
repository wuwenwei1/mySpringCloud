package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.Role;

import java.util.List;

public interface RoleMapper {

    Role getRoleByUserId(@Param("userId") Long userId);

    Boolean hasRoleByRoleSort(@Param("roleSort")Integer roleSort);

    void addRole(@Param("role")Role role);

    void increaseRoleSortByRoleSort(@Param("roleSort")Integer roleSort);



    void increaseRoleSortByRoleSort1(@Param("newRoleSort")Integer newRoleSort,@Param("tarRoleSort") Integer tarRoleSort);

    Role getRoleByRoleId(@Param("roleId")Long roleId);

    void updateRole(@Param("role")Role role);

    void delRoleById(@Param("roleId")Long roleId);

    void updateDataScopeByRoleId(@Param("roleId")Long roleId,@Param("dataScope") String dataScope);












    List<JSONObject> getRoleIdAndName();



}