package com.pragma.traceability.infrastructure.input.rest;

import com.pragma.traceability.application.dto.response.EmployeeEfficiencyResponseDto;
import com.pragma.traceability.application.dto.response.OrderEfficiencyResponseDto;
import com.pragma.traceability.application.handler.IEfficiencyHandler;
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
class EfficiencyRestControllerTest {

    @Mock
    private IEfficiencyHandler efficiencyHandler;

    private EfficiencyRestController controller;

    @BeforeEach
    void setUp() {
        controller = new EfficiencyRestController(efficiencyHandler);
    }

    @Test
    void whenGetOrderEfficiencyCalledThenPassesOwnerUserIdFromPrincipal() {
        UUID ownerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        AuthenticatedUser principal = new AuthenticatedUser(ownerId, "OWNER", null);
        List<OrderEfficiencyResponseDto> expected = List.of(new OrderEfficiencyResponseDto());
        when(efficiencyHandler.getOrderEfficiency(restaurantId, ownerId)).thenReturn(expected);

        ResponseEntity<List<OrderEfficiencyResponseDto>> response =
                controller.getOrderEfficiency(principal, restaurantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(efficiencyHandler).getOrderEfficiency(restaurantId, ownerId);
    }

    @Test
    void whenGetEmployeeRankingCalledThenPassesOwnerUserIdFromPrincipal() {
        UUID ownerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        AuthenticatedUser principal = new AuthenticatedUser(ownerId, "OWNER", null);
        List<EmployeeEfficiencyResponseDto> expected = List.of(new EmployeeEfficiencyResponseDto());
        when(efficiencyHandler.getEmployeeRanking(restaurantId, ownerId)).thenReturn(expected);

        ResponseEntity<List<EmployeeEfficiencyResponseDto>> response =
                controller.getEmployeeRanking(principal, restaurantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(efficiencyHandler).getEmployeeRanking(restaurantId, ownerId);
    }
}
