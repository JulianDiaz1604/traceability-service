package com.pragma.traceability.domain.spi;

import com.pragma.traceability.domain.model.OrderTrace;

import java.util.List;
import java.util.UUID;

public interface ITraceabilityPersistencePort {

    OrderTrace save(OrderTrace trace);

    List<OrderTrace> findByOrderId(UUID orderId);

    List<OrderTrace> findByRestaurantId(UUID restaurantId);
}
