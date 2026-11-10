package com.pragma.traceability.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraceResponseDto {
    private UUID id;
    private UUID orderId;
    private UUID clientId;
    private UUID restaurantId;
    private String previousStatus;
    private String newStatus;
    private UUID employeeId;
    private Instant createdAt;
}
