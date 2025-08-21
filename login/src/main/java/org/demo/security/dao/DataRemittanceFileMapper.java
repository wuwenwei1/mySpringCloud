package org.demo.security.dao;

import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.DataRemittanceFile;

import java.util.List;

public interface DataRemittanceFileMapper {
    void updateDataRemittanceFileById(@Param("dataRemittanceFile")DataRemittanceFile dataRemittanceFile);
    Long getMaxIdByRemittanceId(@Param("remittanceId") Long remittanceId);

    void addDataRemittanceFile(@Param("dataRemittanceFile") DataRemittanceFile dataRemittanceFile);

    void delByIdAndRemittanceId(@Param("id")Long id, @Param("remittanceId")Long remittanceId);
    ////////////////////////////////









    List<DataRemittanceFile> getDataRemittanceFileListByRemittanceId(@Param("remittanceId")Long remittanceId);
}