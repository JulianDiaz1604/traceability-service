package com.pragma.traceability.domain.exception;

import com.pragma.traceability.domain.util.DomainConstants;

/**
 * Raised when food-court-service is unreachable or returns an unexpected error while resolving a
 * restaurant's owner (HU18). Mapped to HTTP 502 through a dedicated {@code ControllerAdvisor} handler.
 */
public class FoodCourtServiceUnavailableException extends DomainException {
    public FoodCourtServiceUnavailableException() {
        super(DomainConstants.FOOD_COURT_SERVICE_UNAVAILABLE_MESSAGE);
    }
}
