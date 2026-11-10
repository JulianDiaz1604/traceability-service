package com.pragma.traceability.infrastructure.input.rest;

import com.pragma.traceability.application.dto.request.RecordTraceRequestDto;
import com.pragma.traceability.application.dto.response.TraceResponseDto;
import com.pragma.traceability.application.handler.ITraceabilityHandler;
import com.pragma.traceability.infrastructure.configuration.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraceabilityRestControllerTest {

    @Mock
    private ITraceabilityHandler traceabilityHandler;

    private TraceabilityRestController controller;

    @BeforeEach
    void setUp() {
        controller = new TraceabilityRestController(traceabilityHandler);
    }

    @Test
    void whenRecordTraceCalledThenReturnsCreatedAndDelegatesToHandler() {
        RecordTraceRequestDto request = new RecordTraceRequestDto();
        TraceResponseDto expected = new TraceResponseDto();
        when(traceabilityHandler.recordTrace(request)).thenReturn(expected);

        ResponseEntity<TraceResponseDto> response = controller.recordTrace(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(traceabilityHandler).recordTrace(request);
    }

    @Test
    void whenGetOrderHistoryCalledThenPassesClientUserIdFromPrincipal() {
        UUID clientId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        AuthenticatedUser principal = new AuthenticatedUser(clientId, "CLIENT", null);
        List<TraceResponseDto> expected = List.of(new TraceResponseDto());
        when(traceabilityHandler.getOrderHistory(orderId, clientId)).thenReturn(expected);

        ResponseEntity<List<TraceResponseDto>> response = controller.getOrderHistory(principal, orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(traceabilityHandler).getOrderHistory(orderId, clientId);
    }
}
