package org.demo.security.service;

import org.demo.security.common.web.model.Result;
import org.demo.security.entity.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface UserServiceExterior {

    Result addUser(String userName, String phone, String password, Long roleId,Long userTypeId);

    Result individualRegist(String userName,
                            String phone,
                            String password,
                            String country,
                            String idNumber,
                            String certificateStartDate,
                            String certificateEndDate,
                            Integer certificateTypeId);

    Result legalPersonRegist(String userName,
                             String phone,
                             String password,
                             String country,
                             String idNumber,
                             String certificateStartDate,
                             String certificateEndDate,
                             Integer certificateTypeId,
                             String licenseNumber,
                             String creditCode,
                             String companyName,
                             Integer legalPersonTypeId);








    Result updatePwd(String phone, String newPwd);

    Result updateUserImage(MultipartFile userImage);


}
