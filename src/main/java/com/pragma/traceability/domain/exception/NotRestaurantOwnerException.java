package com.pragma.traceability.domain.exception;

import com.pragma.traceability.domain.util.DomainConstants;

/**
 * Raised when an owner requests the efficiency report of a restaurant they do not own (HU18).
 * Mapped to HTTP 403 through a dedicated {@code ControllerAdvisor} handler.
 */
public class NotRestaurantOwnerException extends DomainException {
    public NotRestaurantOwnerException() {
        super(DomainConstants.NOT_RESTAURANT_OWNER_MESSAGE);
    }
}
