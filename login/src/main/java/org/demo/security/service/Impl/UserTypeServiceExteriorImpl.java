package org.demo.security.service.Impl;

import org.demo.security.common.web.exception.ExceptionTool;
import org.demo.security.common.web.model.Result;
import org.demo.security.common.web.model.ResultBuilder;
import org.demo.security.common.web.util.DataConversionUtil;
import org.demo.security.common.web.util.SecurityUtils;
import org.demo.security.dao.RoleUserTypeMapper;
import org.demo.security.dao.UserMapper;
import org.demo.security.dao.UserTypeMapper;
import org.demo.security.entity.Menu;
import org.demo.security.entity.User;
import org.demo.security.entity.UserType;
import org.demo.security.service.UserTypeServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserTypeServiceExteriorImpl implements UserTypeServiceExterior {
    @Autowired
    private UserTypeMapper userTypeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleUserTypeMapper roleUserTypeMapper;

    /**
     * 删除账号类型
     * @param userTypeId
     * @return
     */
    @Transactional
    @Override
    public Result delUserType(Long userTypeId) {
        Boolean hasProgeny=userTypeMapper.getHasProgenyByUserTypeId(userTypeId);
        if(hasProgeny){
            ExceptionTool.throwException("存在下级数据,无法删除!","7000");
        }else {
            Boolean hasUserBinding=userMapper.getHasUserBindingByUserTypeId(userTypeId);
            if(hasUserBinding){
                ExceptionTool.throwException("已有绑定用户,无法删除!","7000");
            }
            try {
                userTypeMapper.delUserTypeByUserTypeId(userTypeId);
                roleUserTypeMapper.delByUserTypeId(userTypeId);
            }catch (Exception e){
                ExceptionTool.throwException("删除失败!","7000");
            }
        }
        return ResultBuilder.aResult().msg("删除成功").code("2000").build();
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
    @Transactional
    @Override
    public Result updateUserType(Long userTypeId, Long parentId, String userTypeName, Integer orderNum, String status) {
        Long userId = SecurityUtils.getUserId();
        //Role role = roleMapper.getRoleByUserId(userId);
        UserType newUserType = new UserType();
        User user=userMapper.getUserByUserId(userId);

        UserType tarUserType=userTypeMapper.getUserTypeByUserTypeId(userTypeId);
        if(null==tarUserType||"1".equals(tarUserType.getDelFlag())){
            ExceptionTool.throwException("用户类型已不存在,无需修改!","7000");
        }

        UserType parentUserType=userTypeMapper.getUserTypeByUserTypeId(parentId);
        if(null==parentUserType||"1".equals(parentUserType.getDelFlag())){
            ExceptionTool.throwException("修改失败,上级类型不存在!","7000");
        }
        if(!"0".equals(parentUserType.getStatus())){
            ExceptionTool.throwException("该用户类型已被禁用!!","7000");
        }

        newUserType.setUserTypeId(userTypeId);
        newUserType.setParentId(parentId);
        newUserType.setUserTypeName(userTypeName);
        newUserType.setOrderNum(orderNum);
        newUserType.setStatus(status);
        newUserType.setUpdateBy(user.getUserId());
        newUserType.setUpdateTime(DataConversionUtil.getTodayAllStr(new Date()));


        if(tarUserType.getParentId().equals(parentId)){
            if(tarUserType.getOrderNum()!=orderNum){
                List<UserType> parentIdAndorderNum=userTypeMapper.getUserTypeByParentIdAndOrderNum(parentId,orderNum);
                if(null!=parentIdAndorderNum||parentIdAndorderNum.size()>0){
                    if(tarUserType.getOrderNum()<orderNum){
                        userTypeMapper.increaseUserTypeOrderNumByParentId(parentId,orderNum);
                    }else if(tarUserType.getOrderNum()>orderNum){
                        userTypeMapper.increaseUserTypeOrderNumByParentId2(parentId,orderNum,tarUserType.getOrderNum());
                    }
                }
            }

        }else if(!tarUserType.getParentId().equals(parentId)){
            if(tarUserType.getOrderNum().equals(orderNum)){
                List<UserType> parentIdAndorderNum=userTypeMapper.getUserTypeByParentIdAndOrderNum(parentId,orderNum);
                if(null!=parentIdAndorderNum||parentIdAndorderNum.size()>0){
                    if(tarUserType.getOrderNum()<orderNum){
                        userTypeMapper.increaseUserTypeOrderNumByParentId(parentId,orderNum);
                    }else if(tarUserType.getOrderNum()>orderNum){
                        userTypeMapper.increaseUserTypeOrderNumByParentId2(parentId,orderNum,tarUserType.getOrderNum());
                    }
                }
            }else if(!tarUserType.getOrderNum().equals(orderNum)){
                List<UserType> parentIdAndorderNum=userTypeMapper.getUserTypeByParentIdAndOrderNum(parentId,orderNum);
                if(null!=parentIdAndorderNum||parentIdAndorderNum.size()>0){
                    if(tarUserType.getOrderNum()<orderNum){
                        userTypeMapper.increaseUserTypeOrderNumByParentId(parentId,orderNum);
                    }else if(tarUserType.getOrderNum()>orderNum){
                        userTypeMapper.increaseUserTypeOrderNumByParentId2(parentId,orderNum,tarUserType.getOrderNum());
                    }
                }
            }
        }
        try {
            userTypeMapper.updateUserType(newUserType);
        }catch (DuplicateKeyException e){
            ExceptionTool.throwException("同级类型名称重复!","7000");
        }catch (Exception e){
            ExceptionTool.throwException("修改失败!","7000");
        }
        return ResultBuilder.aResult().msg("修改成功").code("2000").build();
    }






    /**
     * 添加账号类型
     * @param parentId
     * @param userTypeName
     * @param orderNum
     * @param status
     * @return
     */
    @Transactional
    @Override
    public Result addUserType(Long parentId, String userTypeName, Integer orderNum, String status) {
        Long userId = SecurityUtils.getUserId();
        UserType parentUserType=userTypeMapper.getUserTypeByUserTypeId(parentId);
        if(null==parentUserType||!"0".equals(parentUserType.getDelFlag())){
            ExceptionTool.throwException("新增用户类型'"+userTypeName+"'失败,上级类型不存在!","7000");
        }
        if(!"0".equals(parentUserType.getStatus())){
            ExceptionTool.throwException("上级类型已被禁用!!","7000");
        }

        UserType userType = new UserType();
        userType.setParentId(parentId);
        userType.setUserTypeName(userTypeName);
        userType.setOrderNum(orderNum);
        userType.setStatus(status);
        userType.setDelFlag("0");
        userType.setCreateBy(userId);
        userType.setCreateTime(DataConversionUtil.getTodayAllStr(new Date()));
        userType.setUpdateTime("");

        List<UserType> parentIdAndOrderNum=userTypeMapper.getUserTypeByParentIdAndOrderNum(parentId,orderNum);
        if(null!=parentIdAndOrderNum||parentIdAndOrderNum.size()>0){
            userTypeMapper.increaseUserTypeOrderNumByParentId(parentId,orderNum);
        }
        try {
            userTypeMapper.addUserType(userType);
        }catch (DuplicateKeyException e){
            ExceptionTool.throwException("添加用户类型'"+userTypeName+"'失败,同级类型名称重复!","7000");
        }catch (Exception e){
            ExceptionTool.throwException("添加失败!","7000");
        }
        return ResultBuilder.aResult().msg("添加成功").code("2000").build();
    }


}
