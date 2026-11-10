package com.pragma.traceability.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Efficiency of a single completed order (HU18, criterion 1): the elapsed time between when the
 * order started ({@code PENDING}) and when it finished ({@code DELIVERED}).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEfficiency {
    private UUID orderId;
    private Instant startedAt;
    private Instant finishedAt;
    private long durationSeconds;
}
