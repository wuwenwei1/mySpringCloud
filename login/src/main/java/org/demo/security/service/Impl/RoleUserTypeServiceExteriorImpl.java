package org.demo.security.service.Impl;

import org.demo.security.dao.RoleUserTypeMapper;
import org.demo.security.service.RoleUserTypeServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleUserTypeServiceExteriorImpl implements RoleUserTypeServiceExterior {
    @Autowired
    private RoleUserTypeMapper roleUserTypeMapper;
}
