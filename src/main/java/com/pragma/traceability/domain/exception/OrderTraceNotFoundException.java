package com.pragma.traceability.domain.exception;

import com.pragma.traceability.domain.util.DomainConstants;

/**
 * Raised when a client queries the history of an order that has no traceability records (HU17).
 * Mapped to HTTP 404 through a dedicated {@code ControllerAdvisor} handler.
 */
public class OrderTraceNotFoundException extends DomainException {
    public OrderTraceNotFoundException() {
        super(DomainConstants.ORDER_TRACE_NOT_FOUND_MESSAGE);
    }
}
