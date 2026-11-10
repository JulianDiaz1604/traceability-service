package com.pragma.traceability.domain.api;

import com.pragma.traceability.domain.model.OrderTrace;

import java.util.List;
import java.util.UUID;

public interface ITraceabilityServicePort {

    OrderTrace recordTrace(OrderTrace trace);

    List<OrderTrace> getOrderHistory(UUID orderId, UUID clientId);
}
