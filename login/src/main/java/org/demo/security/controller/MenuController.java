package org.demo.security.controller;

import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.StringUtils;
import org.demo.security.service.MenuServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Menu")
public class MenuController {
    @Autowired
    private MenuServiceExterior menuServiceExterior;


    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @PostMapping("/delMenu")
    public Result delMenu(@RequestParam Long menuId){
        if(null==menuId){
            return ResultBuilder.aResult().msg("请选择要删除的菜单!").code("7000").build();
        }
        return menuServiceExterior.delMenu(menuId);
    }


    /**
     * 修改菜单
     * @param menuId
     * @param parentId
     * @param menuType
     * @param icon
     * @param menuName
     * @param orderNum
     * @param isFrame
     * @param path
     * @param perms
     * @param visible
     * @param status
     * @param remark
     * @return
     */
    @PostMapping("/updateMenu")
    public Result updateMenu(@RequestParam Long menuId,
                             @RequestParam(required = false) Long parentId,
                             @RequestParam String menuType,
                             @RequestParam(required = false) String icon,
                             @RequestParam String menuName,
                             @RequestParam Integer orderNum,
                             @RequestParam(required = false) Integer isFrame,
                             @RequestParam(required = false)String path,
                             @RequestParam(required = false)String perms,
                             @RequestParam(required = false) String visible,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false)String remark
    ) {
        if(null==menuId){
            return ResultBuilder.aResult().msg("请选择要修改的菜单!").code("7000").build();
        }
        if(null==parentId){
            parentId=0L;
        }
        if(null==menuType|| StringUtils.isEmpty(menuType.replace(" ",""))){
            return ResultBuilder.aResult().msg("请选择菜单类型!").code("7000").build();
        }else {
            menuType=menuType.replace(" ","");
            if(!("M".equals(menuType.toUpperCase())||"C".equals(menuType.toUpperCase())||"F".equals(menuType.toUpperCase()))){
                return ResultBuilder.aResult().msg("菜单类型不存在!").code("7000").build();
            }
        }
        if(null==icon|| StringUtils.isEmpty(icon.replace(" ",""))){
            icon="";
        }else {
            icon=icon.replace(" ","");
        }

        if(null==menuName|| StringUtils.isEmpty(menuName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写菜单名称!").code("7000").build();
        }else {
            menuName=menuName.replace(" ","");
        }

        if(null==orderNum){
            return ResultBuilder.aResult().msg("请填写显示排序!").code("7000").build();
        }else {
            if(0 > orderNum){
                orderNum=0;
            }
        }


        if(null==isFrame){
            isFrame=1;
        }else {
            if(!(0==isFrame||1==isFrame)){
                return ResultBuilder.aResult().msg("请正确选择是否为外链!").code("7000").build();
            }
        }

        if(null==path|| StringUtils.isEmpty(path.replace(" ",""))){
            path="";
        }else {
            path=path.replace(" ","");
        }

        if(null==perms|| StringUtils.isEmpty(perms.replace(" ",""))){
            perms="";
        }else {
            perms=perms.replace(" ","");
        }



        if(null==visible|| StringUtils.isEmpty(visible.replace(" ",""))){
            visible="0";
        }else {
            visible=visible.replace(" ","");
            if(!("0".equals(visible)||"1".equals(visible))){
                return ResultBuilder.aResult().msg("请正确选择显示状态!").code("7000").build();
            }
        }

        if(null==status|| StringUtils.isEmpty(status.replace(" ",""))){
            status="0";
        }else {
            status=status.replace(" ","");
            if(!("0".equals(status)||"1".equals(status))){
                return ResultBuilder.aResult().msg("请正确选择菜单状态!").code("7000").build();
            }
        }
        if(null==remark){
            remark="";
        }



        if("M".equals(menuType.toUpperCase())){
            path="";
            perms="";
        }

        if("C".equals(menuType.toUpperCase())){
            perms="";
            if("".equals(path)){
                return ResultBuilder.aResult().msg("请填写路由地址!").code("7000").build();
            }
        }
        if("F".equals(menuType.toUpperCase())){
            icon="";
            isFrame=1;
            path="";
            visible="0";
            status="0";
            if("".equals(perms)){
                return ResultBuilder.aResult().msg("请填写权限字符!").code("7000").build();
            }
        }


        return menuServiceExterior.updateMenu(menuId,parentId,menuType,icon,menuName,orderNum,isFrame,path,perms,visible,status,remark);
    }

    /**
     * 添加菜单
     * @param parentId
     * @param menuType
     * @param icon
     * @param menuName
     * @param orderNum
     * @param isFrame
     * @param path
     * @param perms
     * @param visible
     * @param status
     * @param remark
     * @return
     */
    @PostMapping("/addMenu")
    public Result addMenu(@RequestParam(required = false) Long parentId,
                          @RequestParam String menuType,
                          @RequestParam(required = false) String icon,
                          @RequestParam String menuName,
                          @RequestParam Integer orderNum,
                          @RequestParam(required = false) Integer isFrame,
                          @RequestParam(required = false)String path,
                          @RequestParam(required = false)String perms,
                          @RequestParam(required = false) String visible,
                          @RequestParam(required = false) String status,
                          @RequestParam(required = false)String remark
                          ) {
        if(null==parentId){
            parentId=0L;
        }
        if(null==menuType|| StringUtils.isEmpty(menuType.replace(" ",""))){
            return ResultBuilder.aResult().msg("请选择菜单类型!").code("7000").build();
        }else {
            menuType=menuType.replace(" ","");
            if(!("M".equals(menuType.toUpperCase())||"C".equals(menuType.toUpperCase())||"F".equals(menuType.toUpperCase()))){
                return ResultBuilder.aResult().msg("菜单类型不存在!").code("7000").build();
            }
        }
        if(null==icon|| StringUtils.isEmpty(icon.replace(" ",""))){
            icon="";
        }else {
            icon=icon.replace(" ","");
        }

        if(null==menuName|| StringUtils.isEmpty(menuName.replace(" ",""))){
            return ResultBuilder.aResult().msg("请填写菜单名称!").code("7000").build();
        }else {
            menuName=menuName.replace(" ","");
        }

        if(null==orderNum){
            return ResultBuilder.aResult().msg("请填写显示排序!").code("7000").build();
        }else {
            if(0 > orderNum){
                orderNum=0;
            }
        }


        if(null==isFrame){
            isFrame=1;
        }else {
            if(!(0==isFrame||1==isFrame)){
                return ResultBuilder.aResult().msg("请正确选择是否为外链!").code("7000").build();
            }
        }

        if(null==path|| StringUtils.isEmpty(path.replace(" ",""))){
            path="";
        }else {
            path=path.replace(" ","");
        }

        if(null==perms|| StringUtils.isEmpty(perms.replace(" ",""))){
            perms="";
        }else {
            perms=perms.replace(" ","");
        }



        if(null==visible|| StringUtils.isEmpty(visible.replace(" ",""))){
            visible="0";
        }else {
            visible=visible.replace(" ","");
            if(!("0".equals(visible)||"1".equals(visible))){
                return ResultBuilder.aResult().msg("请正确选择显示状态!").code("7000").build();
            }
        }

        if(null==status|| StringUtils.isEmpty(status.replace(" ",""))){
            status="0";
        }else {
            status=status.replace(" ","");
            if(!("0".equals(status)||"1".equals(status))){
                return ResultBuilder.aResult().msg("请正确选择菜单状态!").code("7000").build();
            }
        }
        if(null==remark){
            remark="";
        }



        if("M".equals(menuType.toUpperCase())){
            path="";
            perms="";
        }

        if("C".equals(menuType.toUpperCase())){
            perms="";
            if("".equals(path)){
                return ResultBuilder.aResult().msg("请填写路由地址!").code("7000").build();
            }
        }
        if("F".equals(menuType.toUpperCase())){
            icon="";
            isFrame=1;
            path="";
            visible="0";
            status="0";
            if("".equals(perms)){
                return ResultBuilder.aResult().msg("请填写权限字符!").code("7000").build();
            }
        }


        return menuServiceExterior.addMenu(parentId,menuType,icon,menuName,orderNum,isFrame,path,perms,visible,status,remark);
    }


}
