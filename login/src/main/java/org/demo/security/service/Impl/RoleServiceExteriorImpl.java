package org.demo.security.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.RoleMapper;
import org.demo.security.dao.RoleMenuMapper;
import org.demo.security.dao.RoleUserTypeMapper;
import org.demo.security.dao.UserRoleMapper;
import org.demo.security.entity.Menu;
import org.demo.security.entity.Role;
import org.demo.security.service.RoleServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoleServiceExteriorImpl implements RoleServiceExterior {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleUserTypeMapper roleUserTypeMapper;


    /**
     * 添加角色
     * @param roleName
     * @return
     */
    @Transactional
    @Override
    public Result addRole(String roleName,String roleKey,Integer roleSort,String status,String remark,List<Long> menuIds) {
        Long userId = SecurityUtils.getUserId();
        Role role = roleMapper.getRoleByUserId(userId);

        Role newRole = new Role();
        newRole.setRoleName(roleName);
        newRole.setRoleKey(roleKey);
        newRole.setRoleSort(roleSort);
        newRole.setDataScope("5");
        newRole.setStatus(status);
        newRole.setDelFlag("0");
        newRole.setCreateBy(userId);
        String nowTime = DataConversionUtil.getTodayAllStr(new Date());
        newRole.setCreateTime(nowTime);
        newRole.setUpdateTime("");
        newRole.setRemark(remark);
        Boolean hasAfterRoleSort=roleMapper.hasRoleByRoleSort(roleSort);
        if(hasAfterRoleSort){
            roleMapper.increaseRoleSortByRoleSort(roleSort);
        }

        try {
            roleMapper.addRole(newRole);
            if(null!=menuIds){
                roleMenuMapper.addRoleMenu(newRole.getRoleId(),menuIds);
            }

        }catch (DuplicateKeyException e){
            ExceptionTool.throwException("同级角色名冲突!","7000");
        }
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();
    }


    /**
     * 修改角色的权限范围
     * @param roleId
     * @param dataScope
     * @return
     */
    @Transactional
    @Override
    public Result updateDataScope(Long roleId, String dataScope,List<Long> userTypeIds) {

        Role tarRole =roleMapper.getRoleByRoleId(roleId);
        if(null==tarRole || "1".equals(tarRole.getDelFlag())){
            ExceptionTool.throwException("该角色信息已不存在,无法设置权限范围!","7000");
        }

        if("1".equals(tarRole.getStatus())){
            ExceptionTool.throwException("该角色已被禁用,无法设置权限范围!","7000");
        }

        roleUserTypeMapper.delByRoleId(roleId);
        roleMapper.updateDataScopeByRoleId(roleId,dataScope);
        if("2".equals(dataScope)){
            if(null!=userTypeIds&&userTypeIds.size()>0){
                roleUserTypeMapper.addRoleUserType(roleId,userTypeIds);
            }
        }
        return ResultBuilder.aResult().msg("设置成功").code("2000").build();
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @Transactional
    @Override
    public Result delRole(Long roleId) {
        Long userId = SecurityUtils.getUserId();
        Role role = roleMapper.getRoleByUserId(userId);


        if(1L==roleId||2L==roleId||3L==roleId){
            ExceptionTool.throwException("不允许删除该角色!","7000");
        }
        if(role.getRoleId().equals(roleId)){
            ExceptionTool.throwException("不允许删除自身角色!","7000");
        }


        Role tarRole =roleMapper.getRoleByRoleId(roleId);
        if(null==tarRole||"1".equals(tarRole.getDelFlag())){
            ExceptionTool.throwException("该角色信息已不存在!","7000");
        }

        roleMapper.delRoleById(roleId);
        roleMenuMapper.delByRoleId(roleId);
        roleUserTypeMapper.delByRoleId(roleId);
        return ResultBuilder.aResult().msg("删除成功").code("2000").build();
    }


    /**
     * 获取角色下拉框
     * @return
     */
    @Transactional
    @Override
    public Result getChildRoleDropDownBox() {
        Long userId = SecurityUtils.getUserId();
        Role role = roleMapper.getRoleByUserId(userId);
        List<JSONObject> childRoleDropDownBox=roleMapper.getRoleIdAndName();
        return ResultBuilder.aResult().code("2000").data(childRoleDropDownBox).build();
    }




    /**
     * 修改角色
     * @param roleId
     * @param roleName
     * @return
     */
    @Transactional
    @Override
    public Result updateRole(Long roleId, String roleName,String roleKey,Integer roleSort,String status,String remark,List<Long> menuIds) {

        Long userId = SecurityUtils.getUserId();
        //Role role = roleMapper.getRoleByUserId(userId);
        Role tarRole =roleMapper.getRoleByRoleId(roleId);
        if(null==tarRole || "1".equals(tarRole.getDelFlag())){
            ExceptionTool.throwException("该角色信息不存在!","7000");
        }


        if(1L==roleId||2L==roleId||3L==roleId){
            ExceptionTool.throwException("该角色信息不允许被修改!","7000");
        }
        if(tarRole.getRoleSort()!=roleSort){
            Boolean hasAfterRoleSort=roleMapper.hasRoleByRoleSort(roleSort);
            if(hasAfterRoleSort){
                if(tarRole.getRoleSort()<roleSort){
                    roleMapper.increaseRoleSortByRoleSort(roleSort);
                }else if(tarRole.getRoleSort()>roleSort){
                    roleMapper.increaseRoleSortByRoleSort1(roleSort,tarRole.getRoleSort());
                }
            }
        }
        try {
            tarRole.setRoleName(roleName);
            tarRole.setRoleKey(roleKey);
            tarRole.setRoleSort(roleSort);
            tarRole.setStatus(status);
            String nowTime = DataConversionUtil.getTodayAllStr(new Date());
            tarRole.setUpdateBy(userId);
            tarRole.setUpdateTime(nowTime);
            tarRole.setRemark(remark);
            roleMapper.updateRole(tarRole);
            roleMenuMapper.delByRoleId(roleId);
            if(null!=menuIds){
                roleMenuMapper.addRoleMenu(roleId,menuIds);
            }
        }catch (DuplicateKeyException e){
            ExceptionTool.throwException("该角色名称重复!","7000");
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }






}
