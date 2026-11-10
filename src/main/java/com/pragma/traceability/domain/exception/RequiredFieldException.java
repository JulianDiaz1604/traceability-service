package com.pragma.traceability.domain.exception;

import com.pragma.traceability.domain.util.DomainConstants;

/**
 * Raised when a trace record is missing a required field (HU17). Mapped to HTTP 400 through the
 * {@link DomainException} base handler.
 */
public class RequiredFieldException extends DomainException {
    public RequiredFieldException() {
        super(DomainConstants.FIELD_REQUIRED_MESSAGE);
    }
}
