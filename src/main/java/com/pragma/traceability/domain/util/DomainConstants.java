package com.pragma.traceability.domain.util;

public final class DomainConstants {

    private DomainConstants() {
    }

    public static final String FIELD_REQUIRED_MESSAGE = "All required trace fields must be provided";
    public static final String ORDER_TRACE_NOT_FOUND_MESSAGE =
            "No traceability history exists for the requested order";
    public static final String TRACE_NOT_FROM_CLIENT_MESSAGE =
            "The order history does not belong to the requesting client";
}
