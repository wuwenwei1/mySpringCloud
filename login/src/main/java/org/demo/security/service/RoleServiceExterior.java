package org.demo.security.service;

import org.demo.security.common.web.model.Result;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RoleServiceExterior {

    Result addRole(String roleName,String roleKey,Integer roleSort,String status,String remark,List<Long> menuIds);

    Result updateRole(Long roleId, String roleName,String roleKey,Integer roleSort,String status,String remark,List<Long> menuIds);

    Result updateDataScope(Long roleId, String dataScope,List<Long> userTypeIds);

    Result delRole(Long roleId);

    Result getChildRoleDropDownBox();


}
