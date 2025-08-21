package org.demo.security.service.Impl;

import org.demo.security.dao.CertificateMapper;
import org.demo.security.service.CertificateServiceExterior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateServiceExteriorImpl implements CertificateServiceExterior {
    @Autowired
    private CertificateMapper certificateMapper;
}
