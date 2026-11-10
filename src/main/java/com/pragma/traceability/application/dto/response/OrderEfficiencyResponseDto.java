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
public class OrderEfficiencyResponseDto {
    private UUID orderId;
    private Instant startedAt;
    private Instant finishedAt;
    private long durationSeconds;
}
