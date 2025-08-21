package org.demo.security.service;

import org.demo.security.common.web.model.Result;

public interface UserTypeServiceExterior {
    Result addUserType(Long parentId, String userTypeName, Integer orderNum, String status);

    Result delUserType(Long userTypeId);

    Result updateUserType(Long userTypeId, Long parentId, String userTypeName, Integer orderNum, String status);
}
