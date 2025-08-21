package org.demo.security.dao;

import org.apache.ibatis.annotations.Param;
import org.demo.security.entity.DataRemittanceReview;
import org.demo.security.entity.DataRemittanceReviewFile;

import java.util.List;

public interface DataRemittanceReviewFileMapper {

    void addDataRemittanceReviewFile(@Param("dataRemittanceReviewFile") DataRemittanceReviewFile dataRemittanceReviewFile);

    void updateDataRemittanceReviewFileById(@Param("dataRemittanceReviewFile")DataRemittanceReviewFile dataRemittanceReviewFile);

    List<DataRemittanceReviewFile> getDataRemittanceReviewFileListByReviewId(@Param("reviewId")Long reviewId);

    ///////////////////////////////////////////

}