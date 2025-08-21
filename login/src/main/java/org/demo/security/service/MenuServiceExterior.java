package org.demo.security.service;

import org.demo.security.common.web.model.Result;
import org.springframework.web.bind.annotation.RequestParam;

public interface MenuServiceExterior {
    Result updateMenu(Long menuId,Long parentId, String menuType, String icon, String menuName, Integer orderNum, Integer isFrame, String path,String perms,String visible,String status,String remark);
    Result addMenu(Long parentId, String menuType, String icon, String menuName, Integer orderNum, Integer isFrame, String path,String perms,String visible,String status,String remark);

    Result delMenu(Long menuId);
}
