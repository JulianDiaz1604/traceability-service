package com.pragma.traceability.domain.util;

public final class DomainConstants {

    private DomainConstants() {
    }

    public static final String FIELD_REQUIRED_MESSAGE = "All required trace fields must be provided";
    public static final String ORDER_TRACE_NOT_FOUND_MESSAGE =
            "No traceability history exists for the requested order";
    public static final String TRACE_NOT_FROM_CLIENT_MESSAGE =
            "The order history does not belong to the requesting client";

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_DELIVERED = "DELIVERED";

    public static final String NOT_RESTAURANT_OWNER_MESSAGE =
            "This efficiency report can only be viewed by the owner of the restaurant";
    public static final String RESTAURANT_NOT_FOUND_MESSAGE = "The restaurant does not exist";
    public static final String FOOD_COURT_SERVICE_UNAVAILABLE_MESSAGE =
            "The food-court service is currently unavailable; please try again later";
}
