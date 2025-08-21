package org.demo.security.dao;

import io.lettuce.core.dynamic.annotation.Param;
import org.demo.security.entity.RoleMenu;

import java.util.List;

public interface RoleMenuMapper {

    void addRoleMenu(@Param("roleId") Long roleId,@Param("menuIds") List<Long> menuIds);

    void delByRoleId(@Param("roleId")Long roleId);

    void delByMenuId(@Param("menuId")Long menuId);
}