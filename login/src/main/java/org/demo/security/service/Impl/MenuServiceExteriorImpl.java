package org.demo.security.service.Impl;

import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.MenuMapper;
import org.demo.security.dao.RoleMapper;
import org.demo.security.dao.RoleMenuMapper;
import org.demo.security.dao.UserMapper;
import org.demo.security.entity.Menu;
import org.demo.security.entity.User;
import org.demo.security.service.MenuServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MenuServiceExteriorImpl implements MenuServiceExterior {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @Transactional
    @Override
    public Result delMenu(Long menuId) {
        Boolean hasProgeny=menuMapper.getHasProgenyByMenuId(menuId);
        if(hasProgeny){
            ExceptionTool.throwException("存在子菜单,不允许删除!","7000");
        }else {
            try {
                menuMapper.delMenuByMenuId(menuId);
                roleMenuMapper.delByMenuId(menuId);
            }catch (Exception e){
                ExceptionTool.throwException("删除菜单失败!","7000");
            }
        }
        return ResultBuilder.aResult().msg("删除成功").code("2000").build();
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
    @Transactional
    @Override
    public Result updateMenu(Long menuId,Long parentId, String menuType, String icon, String menuName, Integer orderNum, Integer isFrame, String path,String perms,String visible,String status,String remark) {
        Long userId = SecurityUtils.getUserId();
        //Role role = roleMapper.getRoleByUserId(userId);
        Menu menu = new Menu();
        User user=userMapper.getUserByUserId(userId);

        Menu tarMenu=menuMapper.getMenuByMuenId(menuId);
        if(null==tarMenu){
            ExceptionTool.throwException("菜单不存在,无需修改!","7000");
        }

        if(0L!=parentId){
            Menu parentMenu=menuMapper.getMenuByMuenId(parentId);
            if(null==parentMenu){
                ExceptionTool.throwException("父级菜单不存在!","7000");
            }
        }
        Menu isEmptyMenu=menuMapper.getMenuByParentIdAndMenuNameAndMenuId(menuId,parentId,menuName);
        if(null!=isEmptyMenu){
            ExceptionTool.throwException("修改菜单失败,同级菜单名称已存在!","7000");
        }

        if("F".equals(menuType.toUpperCase())){
            Menu isEmptyMenuByPerms=menuMapper.getMenuByPermsAndMenuId(menuId,perms);
            if(null!=isEmptyMenuByPerms){
                ExceptionTool.throwException("修改菜单失败,权限字符已存在!","7000");
            }
        }

        menu.setParentId(parentId);
        menu.setMenuId(menuId);
        menu.setMenuType(menuType);
        menu.setIcon(icon);
        menu.setOrderNum(orderNum);
        menu.setIsFrame(isFrame);
        menu.setPath(path);
        //要判断
        menu.setMenuName(menuName);
        menu.setPerms(perms);
        menu.setVisible(visible);
        menu.setStatus(status);
        menu.setUpdateBy(user.getUserId());
        menu.setUpdateTime(DataConversionUtil.getTodayAllStr(new Date()));
        menu.setRemark(remark);

        if(tarMenu.getParentId().equals(parentId)){
            if(tarMenu.getOrderNum()!=orderNum){
                List<Menu> parentIdAndorderNum=menuMapper.getMenuByParentIdAndOrderNum(parentId,orderNum);
                if(null!=parentIdAndorderNum||parentIdAndorderNum.size()>0){
                    if(tarMenu.getOrderNum()<menu.getOrderNum()){
                        menuMapper.increaseMenuOrderNumByParentId(parentId,orderNum);
                    }else if(tarMenu.getOrderNum()>menu.getOrderNum()){
                        menuMapper.increaseMenuOrderNumByParentId2(parentId,orderNum,tarMenu.getOrderNum());
                    }
                }
            }

        }else if(!tarMenu.getParentId().equals(parentId)){
            if(tarMenu.getOrderNum().equals(orderNum)){
                List<Menu> parentIdAndorderNum=menuMapper.getMenuByParentIdAndOrderNum(parentId,orderNum);
                if(null!=parentIdAndorderNum||parentIdAndorderNum.size()>0){
                    if(tarMenu.getOrderNum()<menu.getOrderNum()){
                        menuMapper.increaseMenuOrderNumByParentId(parentId,orderNum);
                    }else if(tarMenu.getOrderNum()>menu.getOrderNum()){
                        menuMapper.increaseMenuOrderNumByParentId2(parentId,orderNum,tarMenu.getOrderNum());
                    }
                }
            }else if(!tarMenu.getOrderNum().equals(orderNum)){
                List<Menu> parentIdAndorderNum=menuMapper.getMenuByParentIdAndOrderNum(parentId,orderNum);
                if(null!=parentIdAndorderNum||parentIdAndorderNum.size()>0){
                    if(tarMenu.getOrderNum()<menu.getOrderNum()){
                        menuMapper.increaseMenuOrderNumByParentId(parentId,orderNum);
                    }else if(tarMenu.getOrderNum()>menu.getOrderNum()){
                        menuMapper.increaseMenuOrderNumByParentId2(parentId,orderNum,tarMenu.getOrderNum());
                    }
                }
            }
        }
        try {
            menuMapper.updateMenu(menu);
        }catch (DuplicateKeyException e){
            ExceptionTool.throwException("同级菜单名称重复!","7000");
        }catch (Exception e){
            ExceptionTool.throwException("修改菜单失败!","7000");
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
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
    @Transactional
    @Override
    public Result addMenu(Long parentId, String menuType, String icon, String menuName, Integer orderNum, Integer isFrame, String path,String perms,String visible,String status,String remark) {
        Long userId = SecurityUtils.getUserId();
        //Role role = roleMapper.getRoleByUserId(userId);
        Menu menu = new Menu();
        User user=userMapper.getUserByUserId(userId);
        if(0L!=parentId){
            Menu parentMenu=menuMapper.getMenuByMuenId(parentId);
            if(null==parentMenu){
                ExceptionTool.throwException("新增菜单'"+menuName+"'失败,父级菜单不存在!","7000");
            }
        }

        Menu isEmptyMenu=menuMapper.getMenuByParentIdAndMenuName(parentId,menuName);
        if(null!=isEmptyMenu){
            ExceptionTool.throwException("新增菜单'"+menuName+"'失败,同级菜单名称已存在!","7000");
        }

        if("F".equals(menuType.toUpperCase())){
            Menu isEmptyMenuByPerms=menuMapper.getMenuByPerms(perms);
            if(null!=isEmptyMenuByPerms){
                ExceptionTool.throwException("新增菜单'"+menuName+"'失败,权限字符已存在!","7000");
            }
        }

        menu.setParentId(parentId);
        menu.setMenuType(menuType);
        menu.setIcon(icon);
        menu.setOrderNum(orderNum);
        menu.setIsFrame(isFrame);
        menu.setPath(path);
        //要判断
        menu.setMenuName(menuName);
        menu.setPerms(perms);
        menu.setVisible(visible);
        menu.setStatus(status);
        menu.setCreateBy(user.getUserId());
        menu.setCreateTime(DataConversionUtil.getTodayAllStr(new Date()));
        menu.setUpdateBy(0L);
        menu.setUpdateTime("");
        menu.setRemark(remark);

        List<Menu> parentIdAndorderNum=menuMapper.getMenuByParentIdAndOrderNum(parentId,orderNum);
        if(null!=parentIdAndorderNum||parentIdAndorderNum.size()>0){
            menuMapper.increaseMenuOrderNumByParentId(parentId,orderNum);
        }
        try {
            menuMapper.addMenu(menu);
        }catch (DuplicateKeyException e){
            ExceptionTool.throwException("添加菜单'"+menuName+"'失败,同级菜单名称重复!","7000");
        }catch (Exception e){
            ExceptionTool.throwException("添加失败!","7000");
        }
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();
    }


}
