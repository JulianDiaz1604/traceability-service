package com.pragma.traceability.infrastructure.input.rest;

import com.pragma.traceability.application.dto.request.RecordTraceRequestDto;
import com.pragma.traceability.application.dto.response.TraceResponseDto;
import com.pragma.traceability.application.handler.ITraceabilityHandler;
import com.pragma.traceability.infrastructure.configuration.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/traces")
@RequiredArgsConstructor
@Tag(name = "Traceability", description = "Order status-change history endpoints")
public class TraceabilityRestController {

    private final ITraceabilityHandler traceabilityHandler;

    @Operation(summary = "Record an order status change (called by food-court-service on each "
            + "transition; requires a valid token)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trace recorded"),
            @ApiResponse(responseCode = "400", description = "A required trace field is missing"),
            @ApiResponse(responseCode = "401", description = "Authentication is required")
    })
    @PostMapping
    public ResponseEntity<TraceResponseDto> recordTrace(@RequestBody RecordTraceRequestDto request) {
        return new ResponseEntity<>(traceabilityHandler.recordTrace(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get the chronological status history of an order (client only; the client "
            + "is taken from the authenticated token and may only read their own orders)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status history"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "403", description = "Requester is not a client or the order "
                    + "belongs to another client"),
            @ApiResponse(responseCode = "404", description = "No history exists for the order")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<List<TraceResponseDto>> getOrderHistory(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable UUID orderId) {
        return ResponseEntity.ok(
                traceabilityHandler.getOrderHistory(orderId, authenticatedUser.userId()));
    }
}
