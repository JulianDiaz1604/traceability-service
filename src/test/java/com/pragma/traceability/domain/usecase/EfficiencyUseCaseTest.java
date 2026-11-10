package com.pragma.traceability.domain.usecase;

import com.pragma.traceability.domain.exception.NotRestaurantOwnerException;
import com.pragma.traceability.domain.model.EmployeeEfficiency;
import com.pragma.traceability.domain.model.OrderEfficiency;
import com.pragma.traceability.domain.model.OrderTrace;
import com.pragma.traceability.domain.spi.IRestaurantOwnershipPort;
import com.pragma.traceability.domain.spi.ITraceabilityPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EfficiencyUseCaseTest {

    private static final UUID RESTAURANT_ID = UUID.randomUUID();
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final UUID EMPLOYEE_A = UUID.randomUUID();
    private static final UUID EMPLOYEE_B = UUID.randomUUID();
    private static final UUID ORDER_1 = UUID.randomUUID();
    private static final UUID ORDER_2 = UUID.randomUUID();
    private static final UUID ORDER_3 = UUID.randomUUID();

    @Mock
    private ITraceabilityPersistencePort traceabilityPersistencePort;

    @Mock
    private IRestaurantOwnershipPort restaurantOwnershipPort;

    private EfficiencyUseCase efficiencyUseCase;

    @BeforeEach
    void setUp() {
        efficiencyUseCase = new EfficiencyUseCase(traceabilityPersistencePort, restaurantOwnershipPort);
    }

    private OrderTrace trace(UUID orderId, String status, Instant at, UUID employeeId) {
        OrderTrace t = new OrderTrace();
        t.setId(UUID.randomUUID());
        t.setOrderId(orderId);
        t.setClientId(CLIENT_ID);
        t.setRestaurantId(RESTAURANT_ID);
        t.setNewStatus(status);
        t.setCreatedAt(at);
        t.setEmployeeId(employeeId);
        return t;
    }

    private Instant at(String iso) {
        return Instant.parse(iso);
    }

    @Test
    @DisplayName("Returns the PENDING->DELIVERED duration per completed order and excludes incomplete ones")
    void shouldReturnOrderEfficiencyForCompletedOrdersOnly() {
        when(restaurantOwnershipPort.getOwnerId(RESTAURANT_ID)).thenReturn(OWNER_ID);
        when(traceabilityPersistencePort.findByRestaurantId(RESTAURANT_ID)).thenReturn(List.of(
                trace(ORDER_1, "PENDING", at("2026-01-01T10:00:00Z"), null),
                trace(ORDER_1, "DELIVERED", at("2026-01-01T10:05:00Z"), EMPLOYEE_A),
                trace(ORDER_2, "PENDING", at("2026-01-01T11:00:00Z"), null)));

        List<OrderEfficiency> report = efficiencyUseCase.getOrderEfficiency(RESTAURANT_ID, OWNER_ID);

        assertEquals(1, report.size());
        assertEquals(ORDER_1, report.get(0).getOrderId());
        assertEquals(300, report.get(0).getDurationSeconds());
    }

    @Test
    @DisplayName("Rejects order efficiency when the requester does not own the restaurant")
    void shouldThrowWhenRequesterIsNotOwnerForOrderEfficiency() {
        when(restaurantOwnershipPort.getOwnerId(RESTAURANT_ID)).thenReturn(UUID.randomUUID());

        assertThrows(NotRestaurantOwnerException.class,
                () -> efficiencyUseCase.getOrderEfficiency(RESTAURANT_ID, OWNER_ID));
        verify(traceabilityPersistencePort, never()).findByRestaurantId(RESTAURANT_ID);
    }

    @Test
    @DisplayName("Ranks employees by average PENDING->DELIVERED duration, fastest first")
    void shouldRankEmployeesByAverageDuration() {
        when(restaurantOwnershipPort.getOwnerId(RESTAURANT_ID)).thenReturn(OWNER_ID);
        when(traceabilityPersistencePort.findByRestaurantId(RESTAURANT_ID)).thenReturn(List.of(
                // EMPLOYEE_A: order1 = 300s, order2 = 500s -> avg 400s
                trace(ORDER_1, "PENDING", at("2026-01-01T10:00:00Z"), null),
                trace(ORDER_1, "DELIVERED", at("2026-01-01T10:05:00Z"), EMPLOYEE_A),
                trace(ORDER_2, "PENDING", at("2026-01-01T11:00:00Z"), null),
                trace(ORDER_2, "DELIVERED", at("2026-01-01T11:08:20Z"), EMPLOYEE_A),
                // EMPLOYEE_B: order3 = 100s -> avg 100s (fastest)
                trace(ORDER_3, "PENDING", at("2026-01-01T12:00:00Z"), null),
                trace(ORDER_3, "DELIVERED", at("2026-01-01T12:01:40Z"), EMPLOYEE_B)));

        List<EmployeeEfficiency> ranking = efficiencyUseCase.getEmployeeRanking(RESTAURANT_ID, OWNER_ID);

        assertEquals(2, ranking.size());
        assertEquals(EMPLOYEE_B, ranking.get(0).getEmployeeId());
        assertEquals(100, ranking.get(0).getAverageDurationSeconds());
        assertEquals(1, ranking.get(0).getCompletedOrders());
        assertEquals(EMPLOYEE_A, ranking.get(1).getEmployeeId());
        assertEquals(400, ranking.get(1).getAverageDurationSeconds());
        assertEquals(2, ranking.get(1).getCompletedOrders());
    }

    @Test
    @DisplayName("Rejects employee ranking when the requester does not own the restaurant")
    void shouldThrowWhenRequesterIsNotOwnerForEmployeeRanking() {
        when(restaurantOwnershipPort.getOwnerId(RESTAURANT_ID)).thenReturn(UUID.randomUUID());

        assertThrows(NotRestaurantOwnerException.class,
                () -> efficiencyUseCase.getEmployeeRanking(RESTAURANT_ID, OWNER_ID));
        verify(traceabilityPersistencePort, never()).findByRestaurantId(RESTAURANT_ID);
    }
}
