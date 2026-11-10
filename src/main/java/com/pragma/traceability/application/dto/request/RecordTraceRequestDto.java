package com.pragma.traceability.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Request body sent by food-court-service to record one order status change (HU17).
 * The {@code id} and {@code createdAt} are assigned by traceability-service, not by the caller.
 */
@Getter
@Setter
public class RecordTraceRequestDto {
    private UUID orderId;
    private UUID clientId;
    private UUID restaurantId;
    private String previousStatus;
    private String newStatus;
    private UUID employeeId;
}
