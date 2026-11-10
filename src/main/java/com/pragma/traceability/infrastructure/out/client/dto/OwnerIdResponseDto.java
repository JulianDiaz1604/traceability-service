package com.pragma.traceability.infrastructure.out.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Response body from food-court-service {@code GET /api/v1/restaurants/{restaurantId}/owner}.
 */
@Getter
@Setter
@NoArgsConstructor
public class OwnerIdResponseDto {
    private UUID restaurantId;
    private UUID ownerId;
}
