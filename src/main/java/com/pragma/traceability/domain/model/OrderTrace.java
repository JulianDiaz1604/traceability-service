package com.pragma.traceability.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * A single order status-change record (HU17). Traceability stores one of these per transition so a
 * client can later review the full history of their order. The service owns {@code id} and
 * {@code createdAt}; the rest comes from food-court-service when the transition happens.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrace {
    private UUID id;
    private UUID orderId;
    private UUID clientId;
    private UUID restaurantId;
    private String previousStatus;
    private String newStatus;
    private UUID employeeId;
    private Instant createdAt;
}
