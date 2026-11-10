package com.pragma.traceability.domain.spi;

import java.util.UUID;

/**
 * Driven port for resolving which owner a restaurant belongs to (HU18). The implementation calls
 * food-court-service and lives in {@code infrastructure/out/client}.
 */
public interface IRestaurantOwnershipPort {

    /**
     * Returns the owner id of the given restaurant.
     *
     * @throws com.pragma.traceability.domain.exception.RestaurantNotFoundException if the restaurant
     *         does not exist
     * @throws com.pragma.traceability.domain.exception.FoodCourtServiceUnavailableException if
     *         food-court-service is unreachable or responds with an unexpected error
     */
    UUID getOwnerId(UUID restaurantId);
}
