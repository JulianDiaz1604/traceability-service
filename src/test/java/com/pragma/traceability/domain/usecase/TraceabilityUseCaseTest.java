package com.pragma.traceability.domain.usecase;

import com.pragma.traceability.domain.exception.OrderTraceNotFoundException;
import com.pragma.traceability.domain.exception.RequiredFieldException;
import com.pragma.traceability.domain.exception.TraceNotFromClientException;
import com.pragma.traceability.domain.model.OrderTrace;
import com.pragma.traceability.domain.spi.ITraceabilityPersistencePort;
import com.pragma.traceability.domain.validation.TraceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraceabilityUseCaseTest {

    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();

    @Mock
    private ITraceabilityPersistencePort traceabilityPersistencePort;

    private TraceabilityUseCase traceabilityUseCase;

    @BeforeEach
    void setUp() {
        traceabilityUseCase = new TraceabilityUseCase(traceabilityPersistencePort, new TraceValidator());
    }

    private OrderTrace trace(UUID clientId, String newStatus, Instant createdAt) {
        OrderTrace trace = new OrderTrace();
        trace.setOrderId(ORDER_ID);
        trace.setClientId(clientId);
        trace.setNewStatus(newStatus);
        trace.setCreatedAt(createdAt);
        return trace;
    }

    @Test
    @DisplayName("Records a valid trace, stamping a fresh id and createdAt")
    void shouldRecordTraceStampingIdAndCreatedAt() {
        OrderTrace incoming = trace(CLIENT_ID, "PENDING", null);
        when(traceabilityPersistencePort.save(any(OrderTrace.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderTrace result = traceabilityUseCase.recordTrace(incoming);

        ArgumentCaptor<OrderTrace> captor = ArgumentCaptor.forClass(OrderTrace.class);
        verify(traceabilityPersistencePort).save(captor.capture());
        OrderTrace saved = captor.getValue();
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertEquals("PENDING", saved.getNewStatus());
        assertEquals("PENDING", result.getNewStatus());
    }

    @Test
    @DisplayName("Rejects recording an invalid trace without persisting")
    void shouldThrowWhenRecordingInvalidTrace() {
        OrderTrace invalid = trace(null, "PENDING", null);

        assertThrows(RequiredFieldException.class, () -> traceabilityUseCase.recordTrace(invalid));
        verify(traceabilityPersistencePort, never()).save(any());
    }

    @Test
    @DisplayName("Returns the order history sorted chronologically for the owning client")
    void shouldReturnHistorySortedByCreatedAt() {
        Instant t1 = Instant.parse("2026-01-01T10:00:00Z");
        Instant t2 = Instant.parse("2026-01-01T11:00:00Z");
        Instant t3 = Instant.parse("2026-01-01T12:00:00Z");
        when(traceabilityPersistencePort.findByOrderId(ORDER_ID))
                .thenReturn(List.of(trace(CLIENT_ID, "READY", t3),
                        trace(CLIENT_ID, "PENDING", t1),
                        trace(CLIENT_ID, "IN_PREPARATION", t2)));

        List<OrderTrace> history = traceabilityUseCase.getOrderHistory(ORDER_ID, CLIENT_ID);

        assertEquals(List.of("PENDING", "IN_PREPARATION", "READY"),
                history.stream().map(OrderTrace::getNewStatus).toList());
    }

    @Test
    @DisplayName("Rejects reading the history of an order with no records")
    void shouldThrowWhenOrderHasNoHistory() {
        when(traceabilityPersistencePort.findByOrderId(ORDER_ID)).thenReturn(List.of());

        assertThrows(OrderTraceNotFoundException.class,
                () -> traceabilityUseCase.getOrderHistory(ORDER_ID, CLIENT_ID));
    }

    @Test
    @DisplayName("Rejects reading the history of an order that belongs to another client")
    void shouldThrowWhenHistoryBelongsToAnotherClient() {
        when(traceabilityPersistencePort.findByOrderId(ORDER_ID))
                .thenReturn(List.of(trace(UUID.randomUUID(), "PENDING", Instant.now())));

        assertThrows(TraceNotFromClientException.class,
                () -> traceabilityUseCase.getOrderHistory(ORDER_ID, CLIENT_ID));
    }
}
