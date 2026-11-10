package com.pragma.traceability.domain.exception;

import com.pragma.traceability.domain.util.DomainConstants;

/**
 * Raised when a client tries to read the history of an order that belongs to another client (HU17,
 * acceptance criterion 2). Mapped to HTTP 403 through a dedicated {@code ControllerAdvisor} handler.
 */
public class TraceNotFromClientException extends DomainException {
    public TraceNotFromClientException() {
        super(DomainConstants.TRACE_NOT_FROM_CLIENT_MESSAGE);
    }
}
