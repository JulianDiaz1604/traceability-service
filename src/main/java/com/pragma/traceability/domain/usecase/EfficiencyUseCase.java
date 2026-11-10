package com.pragma.traceability.domain.usecase;

import com.pragma.traceability.domain.api.IEfficiencyServicePort;
import com.pragma.traceability.domain.exception.NotRestaurantOwnerException;
import com.pragma.traceability.domain.model.EmployeeEfficiency;
import com.pragma.traceability.domain.model.OrderEfficiency;
import com.pragma.traceability.domain.model.OrderTrace;
import com.pragma.traceability.domain.spi.IRestaurantOwnershipPort;
import com.pragma.traceability.domain.spi.ITraceabilityPersistencePort;
import com.pragma.traceability.domain.util.DomainConstants;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Computes order-efficiency reports for a restaurant owner (HU18). Efficiency is measured as the
 * time between an order starting ({@code PENDING}) and finishing ({@code DELIVERED}); only completed
 * orders are considered. Access is restricted to the restaurant's owner, verified against
 * food-court-service.
 */
public class EfficiencyUseCase implements IEfficiencyServicePort {

    private final ITraceabilityPersistencePort traceabilityPersistencePort;
    private final IRestaurantOwnershipPort restaurantOwnershipPort;

    public EfficiencyUseCase(ITraceabilityPersistencePort traceabilityPersistencePort,
                             IRestaurantOwnershipPort restaurantOwnershipPort) {
        this.traceabilityPersistencePort = traceabilityPersistencePort;
        this.restaurantOwnershipPort = restaurantOwnershipPort;
    }

    @Override
    public List<OrderEfficiency> getOrderEfficiency(UUID restaurantId, UUID ownerId) {
        verifyOwnership(restaurantId, ownerId);
        List<OrderEfficiency> report = new ArrayList<>();
        for (Map.Entry<UUID, List<OrderTrace>> order : tracesByOrder(restaurantId).entrySet()) {
            Instant start = startOf(order.getValue());
            OrderTrace delivered = deliveredTraceOf(order.getValue());
            if (start != null && delivered != null) {
                report.add(new OrderEfficiency(order.getKey(), start, delivered.getCreatedAt(),
                        Duration.between(start, delivered.getCreatedAt()).getSeconds()));
            }
        }
        report.sort(Comparator.comparing(OrderEfficiency::getStartedAt));
        return report;
    }

    @Override
    public List<EmployeeEfficiency> getEmployeeRanking(UUID restaurantId, UUID ownerId) {
        verifyOwnership(restaurantId, ownerId);
        Map<UUID, List<Long>> durationsByEmployee = new HashMap<>();
        for (List<OrderTrace> order : tracesByOrder(restaurantId).values()) {
            Instant start = startOf(order);
            OrderTrace delivered = deliveredTraceOf(order);
            if (start != null && delivered != null && delivered.getEmployeeId() != null) {
                long duration = Duration.between(start, delivered.getCreatedAt()).getSeconds();
                durationsByEmployee
                        .computeIfAbsent(delivered.getEmployeeId(), employee -> new ArrayList<>())
                        .add(duration);
            }
        }
        List<EmployeeEfficiency> ranking = new ArrayList<>();
        durationsByEmployee.forEach((employeeId, durations) -> {
            long average = Math.round(durations.stream().mapToLong(Long::longValue).average().orElse(0));
            ranking.add(new EmployeeEfficiency(employeeId, average, durations.size()));
        });
        ranking.sort(Comparator.comparingLong(EmployeeEfficiency::getAverageDurationSeconds));
        return ranking;
    }

    private void verifyOwnership(UUID restaurantId, UUID ownerId) {
        UUID actualOwner = restaurantOwnershipPort.getOwnerId(restaurantId);
        if (!ownerId.equals(actualOwner)) {
            throw new NotRestaurantOwnerException();
        }
    }

    private Map<UUID, List<OrderTrace>> tracesByOrder(UUID restaurantId) {
        return traceabilityPersistencePort.findByRestaurantId(restaurantId).stream()
                .collect(Collectors.groupingBy(OrderTrace::getOrderId));
    }

    private Instant startOf(List<OrderTrace> orderTraces) {
        return orderTraces.stream()
                .filter(trace -> DomainConstants.STATUS_PENDING.equals(trace.getNewStatus()))
                .map(OrderTrace::getCreatedAt)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    private OrderTrace deliveredTraceOf(List<OrderTrace> orderTraces) {
        return orderTraces.stream()
                .filter(trace -> DomainConstants.STATUS_DELIVERED.equals(trace.getNewStatus()))
                .max(Comparator.comparing(OrderTrace::getCreatedAt))
                .orElse(null);
    }
}
