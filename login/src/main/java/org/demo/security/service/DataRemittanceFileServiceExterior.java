package org.demo.security.service;

import org.demo.security.common.web.model.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataRemittanceFileServiceExterior {

    Result getDataRemittanceFileList(Long remittanceId);

}
