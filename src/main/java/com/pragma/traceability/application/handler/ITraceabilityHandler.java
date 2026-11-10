package com.pragma.traceability.application.handler;

import com.pragma.traceability.application.dto.request.RecordTraceRequestDto;
import com.pragma.traceability.application.dto.response.TraceResponseDto;

import java.util.List;
import java.util.UUID;

public interface ITraceabilityHandler {

    TraceResponseDto recordTrace(RecordTraceRequestDto request);

    List<TraceResponseDto> getOrderHistory(UUID orderId, UUID clientId);
}
