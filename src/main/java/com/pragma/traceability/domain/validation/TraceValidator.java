package com.pragma.traceability.domain.validation;

import com.pragma.traceability.domain.exception.RequiredFieldException;
import com.pragma.traceability.domain.model.OrderTrace;

public class TraceValidator {

    public void validateNewTrace(OrderTrace trace) {
        if (trace == null
                || trace.getOrderId() == null
                || trace.getClientId() == null
                || trace.getRestaurantId() == null
                || isBlank(trace.getNewStatus())) {
            throw new RequiredFieldException();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
