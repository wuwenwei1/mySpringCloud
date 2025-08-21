package org.demo.security.service;

import org.demo.security.common.web.model.Result;

public interface SubjectAreaServiceExteror {
    Result addSubjectArea(String subjectName);

    Result updateSubjectArea(Long id, String subjectName);

    Result delSubjectArea(Long id);

    Result updateSubjectAreaStatus(Long id, Integer status);

    Result getSubjectAreaList(String subjectName, Integer status, Integer starIndex, Integer pageSize);

    Result getSubjectAreaBox();
}
