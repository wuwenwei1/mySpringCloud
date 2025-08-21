package org.demo.security.service;

import org.demo.security.common.web.model.Result;

public interface AuditRecordServiceExterior {
    Result getAuditRecordList(Long id);

    Result delReviewsIdea(Long id);
}
