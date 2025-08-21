package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.MenuServiceExterior;
import org.demo.security.service.UserTypeServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/UserType")
public class UserTypeController {
    @Autowired
    private UserTypeServiceExterior userTypeServiceExterior;


    /**
     * 删除账号类型
     * @param userTypeId
     * @return
     */
    @PostMapping("/delUserType")
    public Result delUserType(@RequestParam Long userTypeId){
        if(null==userTypeId){
            return ResultBuilder.aResult().msg("请选择要删除的用户类型!").code("7000").build();
        }
        return userTypeServiceExterior.delUserType(userTypeId);
    }


    /**
     * 修改用户类型
     * @param userTypeId
     * @param parentId
     * @param userTypeName
     * @param orderNum
     * @param status
     * @return
     */
    @PostMapping("/updateMenu")
    public Result updateMenu(@RequestParam Long userTypeId,
                             @RequestParam Long parentId,
                             @RequestParam String userTypeName,
                             @RequestParam Integer orderNum,
                             @RequestParam String status
    ) {
        if(null==userTypeId){
            return ResultBuilder.aResult().msg("请选择需要修改的数据!").code("7000").build();
        }
        if(null==parentId){
            return ResultBuilder.aResult().msg("请选择上级类型!").code("7000").build();
        }else {
            if(2L==parentId||3L==parentId){
                return ResultBuilder.aResult().msg("不允许在该类型下添加类型!").code("7000").build();
            }
        }
        if(null==userTypeName|| StringUtils.isEmpty(userTypeName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请选择用户类型!").code("7000").build();
        }else {
            userTypeName=userTypeName.replace(" ","");

        }

        if(null==orderNum){
            return ResultBuilder.aResult().msg("请填写显示排序!").code("7000").build();
        }else {
            if(0 > orderNum){
                orderNum=0;
            }
        }
        if(null==status|| StringUtils.isEmpty(status.replace(" ",""))){
            status="0";
        }else {
            status=status.replace(" ","");
            if(!("0".equals(status)||"1".equals(status))){
                return ResultBuilder.aResult().msg("请正确选择用户类型状态!").code("7000").build();
            }
        }


        return userTypeServiceExterior.updateUserType(userTypeId,parentId,userTypeName,orderNum,status);
    }

    /**
     * 添加账号类型
     * @param parentId
     * @param userTypeName
     * @param orderNum
     * @param status
     * @return
     */
    @PostMapping("/addUserType")
    public Result addUserType(@RequestParam Long parentId,
                          @RequestParam String userTypeName,
                          @RequestParam Integer orderNum,
                          @RequestParam String status) {
        if(null==parentId){
            return ResultBuilder.aResult().msg("请选择上级类型!").code("7000").build();
        }else {
            if(2L==parentId||3L==parentId){
                return ResultBuilder.aResult().msg("不允许在该类型下添加类型!").code("7000").build();
            }
        }
        if(null==userTypeName|| StringUtils.isEmpty(userTypeName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请选择用户类型!").code("7000").build();
        }else {
            userTypeName=userTypeName.replace(" ","");

        }

        if(null==orderNum){
            return ResultBuilder.aResult().msg("请填写显示排序!").code("7000").build();
        }else {
            if(0 > orderNum){
                orderNum=0;
            }
        }
        if(null==status|| StringUtils.isEmpty(status.replace(" ",""))){
            status="0";
        }else {
            status=status.replace(" ","");
            if(!("0".equals(status)||"1".equals(status))){
                return ResultBuilder.aResult().msg("请正确选择用户类型状态!").code("7000").build();
            }
        }
        return userTypeServiceExterior.addUserType(parentId,userTypeName,orderNum,status);
    }


}
