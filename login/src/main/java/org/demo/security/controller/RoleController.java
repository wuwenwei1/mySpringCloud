package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.RoleServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Role")
public class RoleController {

    @Autowired
    private RoleServiceExterior roleServiceExterior;


    /**
     * 获取下级角色下拉框
     * @return
     */
    @PostMapping("/getChildRoleDropDownBox")
    public Result getChildRoleDropDownBox(){
        return roleServiceExterior.getChildRoleDropDownBox();
    }


    /**
     * 修改角色的权限范围
     * @param roleId
     * @param dataScope
     * @return
     */
    @PostMapping("/updateDataScope")
    public Result updateDataScope(@RequestParam Long roleId,
                                  @RequestParam String dataScope,
                                  @RequestParam(required = false) List<Long> userTypeIds){
        if(null==roleId){
            return ResultBuilder.aResult().msg("请选择角色数据!").code("7000").build();
        }
        if(null==dataScope|| StringUtils.isEmpty(dataScope.replace(" ",""))){
            return ResultBuilder.aResult().msg("请选择权限范围!").code("7000").build();
        }else {
            dataScope=dataScope.replace(" ","");
            if(!("1".equals(dataScope)||"2".equals(dataScope)||"3".equals(dataScope)||"4".equals(dataScope)||"5".equals(dataScope))){
                return ResultBuilder.aResult().msg("权限范围错误!").code("7000").build();
            }
        }
        return roleServiceExterior.updateDataScope(roleId,dataScope,userTypeIds);
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @PostMapping("/delRole")
    public Result delRole(@RequestParam Long roleId){
        if(null==roleId){
            return ResultBuilder.aResult().msg("请选择要删除的数据!").code("7000").build();
        }
        return roleServiceExterior.delRole(roleId);
    }

    /**
     * 修改角色
     * @param roleId
     * @param roleName
     * @return
     */
    @PostMapping("/updateRole")
    public Result updateRole(@RequestParam Long roleId,
                             @RequestParam String roleName,
                             @RequestParam String roleKey,
                             @RequestParam Integer roleSort,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false) String remark,
                             @RequestParam(required = false) List<Long> menuIds
    ){

        if(null==roleId){
            return ResultBuilder.aResult().msg("请选择要修改的数据!").code("7000").build();
        }

        if(null==roleName|| StringUtils.isEmpty(roleName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写角色名称!").code("7000").build();
        }else {
            roleName=roleName.replace(" ","");
        }
        if(null==roleKey|| StringUtils.isEmpty(roleKey.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写角色标识字符!").code("7000").build();
        }else {
            roleKey=roleKey.replace(" ","");
        }
        if(null==roleSort){
            return ResultBuilder.aResult().msg("请填写角色排序!").code("7000").build();
        }else {
            if(0 > roleSort){
                roleSort=0;
            }
        }
        if(null==status|| StringUtils.isEmpty(status.replace(" ",""))){
            status="0";
        }else {
            status=status.replace(" ","");
            if(!("0".equals(status)||"1".equals(status))){
                status="0";
            }
        }
        if(null==remark|| StringUtils.isEmpty(remark.replace(" ",""))){
            remark="";
        }else {
            remark=remark.replace(" ","");
        }

        if(null==menuIds|| 0==menuIds.size()){
            menuIds=null;
        }

        return roleServiceExterior.updateRole(roleId,roleName,roleKey,roleSort,status,remark,menuIds);
    }

    /**
     * 添加角色
     * @param roleName
     * @return
     */
    @PostMapping("/addRole")
    public Result addRole(@RequestParam String roleName,
                          @RequestParam String roleKey,
                          @RequestParam Integer roleSort,
                          @RequestParam(required = false) String status,
                          @RequestParam(required = false) String remark,
                          @RequestParam(required = false) List<Long> menuIds){

        if(null==roleName|| StringUtils.isEmpty(roleName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写角色名称!").code("7000").build();
        }else {
            roleName=roleName.replace(" ","");
        }
        if(null==roleKey|| StringUtils.isEmpty(roleKey.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写角色标识字符!").code("7000").build();
        }else {
            roleKey=roleKey.replace(" ","");
        }
        if(null==roleSort){
            return ResultBuilder.aResult().msg("请填写角色排序!").code("7000").build();
        }else {
            if(0 > roleSort){
                roleSort=0;
            }
        }
        if(null==status|| StringUtils.isEmpty(status.replace(" ",""))){
            status="0";
        }else {
            status=status.replace(" ","");
            if(!("0".equals(status)||"1".equals(status))){
                status="0";
            }
        }
        if(null==remark|| StringUtils.isEmpty(remark.replace(" ",""))){
            remark="";
        }else {
            remark=remark.replace(" ","");
        }

        if(null==menuIds|| 0==menuIds.size()){
            menuIds=null;
        }
        return roleServiceExterior.addRole(roleName,roleKey,roleSort,status,remark,menuIds);
    }
}
