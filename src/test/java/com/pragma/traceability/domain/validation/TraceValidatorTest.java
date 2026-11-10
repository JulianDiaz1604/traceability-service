package com.pragma.traceability.domain.validation;

import com.pragma.traceability.domain.exception.RequiredFieldException;
import com.pragma.traceability.domain.model.OrderTrace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TraceValidatorTest {

    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final UUID RESTAURANT_ID = UUID.randomUUID();

    private TraceValidator traceValidator;

    @BeforeEach
    void setUp() {
        traceValidator = new TraceValidator();
    }

    private OrderTrace traceWith(UUID orderId, UUID clientId, String newStatus) {
        OrderTrace trace = new OrderTrace();
        trace.setOrderId(orderId);
        trace.setClientId(clientId);
        trace.setRestaurantId(RESTAURANT_ID);
        trace.setNewStatus(newStatus);
        return trace;
    }

    @Test
    @DisplayName("Accepts a trace with order id, client id and a new status")
    void shouldAcceptValidTrace() {
        assertDoesNotThrow(() -> traceValidator.validateNewTrace(traceWith(ORDER_ID, CLIENT_ID, "PENDING")));
    }

    @Test
    @DisplayName("Rejects a null trace")
    void shouldThrowWhenTraceIsNull() {
        assertThrows(RequiredFieldException.class, () -> traceValidator.validateNewTrace(null));
    }

    @Test
    @DisplayName("Rejects a trace without order id")
    void shouldThrowWhenOrderIdIsMissing() {
        OrderTrace trace = traceWith(null, CLIENT_ID, "PENDING");

        assertThrows(RequiredFieldException.class, () -> traceValidator.validateNewTrace(trace));
    }

    @Test
    @DisplayName("Rejects a trace without client id")
    void shouldThrowWhenClientIdIsMissing() {
        OrderTrace trace = traceWith(ORDER_ID, null, "PENDING");

        assertThrows(RequiredFieldException.class, () -> traceValidator.validateNewTrace(trace));
    }

    @Test
    @DisplayName("Rejects a trace with a blank new status")
    void shouldThrowWhenNewStatusIsBlank() {
        OrderTrace trace = traceWith(ORDER_ID, CLIENT_ID, "  ");

        assertThrows(RequiredFieldException.class, () -> traceValidator.validateNewTrace(trace));
    }

    @Test
    @DisplayName("Rejects a trace without restaurant id")
    void shouldThrowWhenRestaurantIdIsMissing() {
        OrderTrace trace = traceWith(ORDER_ID, CLIENT_ID, "PENDING");
        trace.setRestaurantId(null);

        assertThrows(RequiredFieldException.class, () -> traceValidator.validateNewTrace(trace));
    }
}
