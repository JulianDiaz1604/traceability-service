package com.pragma.traceability.domain.usecase;

import com.pragma.traceability.domain.api.ITraceabilityServicePort;
import com.pragma.traceability.domain.exception.OrderTraceNotFoundException;
import com.pragma.traceability.domain.exception.TraceNotFromClientException;
import com.pragma.traceability.domain.model.OrderTrace;
import com.pragma.traceability.domain.spi.ITraceabilityPersistencePort;
import com.pragma.traceability.domain.validation.TraceValidator;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TraceabilityUseCase implements ITraceabilityServicePort {

    private final ITraceabilityPersistencePort traceabilityPersistencePort;
    private final TraceValidator traceValidator;

    public TraceabilityUseCase(ITraceabilityPersistencePort traceabilityPersistencePort,
                               TraceValidator traceValidator) {
        this.traceabilityPersistencePort = traceabilityPersistencePort;
        this.traceValidator = traceValidator;
    }

    /**
     * Records one order status change (HU17, criterion 1). The service owns the record metadata:
     * it always stamps a fresh {@code id} and {@code createdAt}, ignoring any values the caller sent.
     */
    @Override
    public OrderTrace recordTrace(OrderTrace trace) {
        traceValidator.validateNewTrace(trace);
        trace.setId(UUID.randomUUID());
        trace.setCreatedAt(Instant.now());
        return traceabilityPersistencePort.save(trace);
    }

    /**
     * Returns the chronological history of an order (HU17, criterion 2). A client may only read the
     * history of their own orders; otherwise {@link TraceNotFromClientException}.
     */
    @Override
    public List<OrderTrace> getOrderHistory(UUID orderId, UUID clientId) {
        List<OrderTrace> history = traceabilityPersistencePort.findByOrderId(orderId);
        if (history.isEmpty()) {
            throw new OrderTraceNotFoundException();
        }
        if (!clientId.equals(history.get(0).getClientId())) {
            throw new TraceNotFromClientException();
        }
        return history.stream()
                .sorted(Comparator.comparing(OrderTrace::getCreatedAt))
                .toList();
    }
}
