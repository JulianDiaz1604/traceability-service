package com.pragma.traceability.infrastructure.input.rest;

import com.pragma.traceability.application.dto.response.EmployeeEfficiencyResponseDto;
import com.pragma.traceability.application.dto.response.OrderEfficiencyResponseDto;
import com.pragma.traceability.application.handler.IEfficiencyHandler;
import com.pragma.traceability.infrastructure.configuration.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/efficiency")
@RequiredArgsConstructor
@Tag(name = "Efficiency", description = "Order efficiency reports for a restaurant owner")
public class EfficiencyRestController {

    private final IEfficiencyHandler efficiencyHandler;

    @Operation(summary = "Per-order efficiency: the PENDING -> DELIVERED duration of each completed "
            + "order of the restaurant (owner only; the owner is taken from the authenticated token)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of completed orders with their durations"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "Requester is not an owner or does not own "
                    + "the restaurant"),
            @ApiResponse(responseCode = "404", description = "The restaurant does not exist"),
            @ApiResponse(responseCode = "502", description = "food-court-service is unavailable")
    })
    @GetMapping("/restaurants/{restaurantId}/orders")
    public ResponseEntity<List<OrderEfficiencyResponseDto>> getOrderEfficiency(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable UUID restaurantId) {
        return ResponseEntity.ok(
                efficiencyHandler.getOrderEfficiency(restaurantId, authenticatedUser.userId()));
    }

    @Operation(summary = "Employee ranking: average PENDING -> DELIVERED duration per employee of the "
            + "restaurant, fastest first (owner only; the owner is taken from the authenticated token)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking of employees by average duration"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "Requester is not an owner or does not own "
                    + "the restaurant"),
            @ApiResponse(responseCode = "404", description = "The restaurant does not exist"),
            @ApiResponse(responseCode = "502", description = "food-court-service is unavailable")
    })
    @GetMapping("/restaurants/{restaurantId}/employees")
    public ResponseEntity<List<EmployeeEfficiencyResponseDto>> getEmployeeRanking(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable UUID restaurantId) {
        return ResponseEntity.ok(
                efficiencyHandler.getEmployeeRanking(restaurantId, authenticatedUser.userId()));
    }
}
