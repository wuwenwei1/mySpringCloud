package org.demo.security.dao;

import io.lettuce.core.dynamic.annotation.Param;
import org.demo.security.entity.Menu;

import java.util.List;

public interface MenuMapper {

    Menu getMenuByMuenId(@Param("id") Long id);

    Menu getMenuByParentIdAndMenuName(@Param("parentId")Long parentId,@Param("menuName") String menuName);

    Menu getMenuByPerms(@Param("perms")String perms);

    void increaseMenuOrderNumByParentId(@Param("parentId")Long parentId,@Param("orderNum") Integer orderNum);

    void increaseMenuOrderNumByParentId2(@Param("parentId") Long parentId,@Param("newOrderNum") Integer newOrderNum,@Param("tarOrderNum") Integer tarOrderNum);

    void addMenu(@Param("menu")Menu menu);

    Menu getMenuByParentIdAndMenuNameAndMenuId(@Param("menuId")Long menuId,@Param("parentId") Long parentId,@Param("menuName") String menuName);

    Menu getMenuByPermsAndMenuId(@Param("menuId")Long menuId,@Param("perms") String perms);

    void updateMenu(@Param("menu")Menu menu);

    List<Menu> getMenuByParentIdAndOrderNum(@Param("parentId")Long parentId, @Param("orderNum") Integer orderNum);

    Boolean getHasProgenyByMenuId(@Param("menuId")Long menuId);

    void delMenuByMenuId(@Param("menuId")Long menuId);
}