package com.pragma.traceability.domain.exception;

import com.pragma.traceability.domain.util.DomainConstants;

/**
 * Raised when the restaurant referenced in an efficiency request does not exist in
 * food-court-service (HU18). Mapped to HTTP 404 through a dedicated {@code ControllerAdvisor} handler.
 */
public class RestaurantNotFoundException extends DomainException {
    public RestaurantNotFoundException() {
        super(DomainConstants.RESTAURANT_NOT_FOUND_MESSAGE);
    }
}
