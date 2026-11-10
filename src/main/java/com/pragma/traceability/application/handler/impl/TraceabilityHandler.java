package com.pragma.traceability.application.handler.impl;

import com.pragma.traceability.application.dto.request.RecordTraceRequestDto;
import com.pragma.traceability.application.dto.response.TraceResponseDto;
import com.pragma.traceability.application.handler.ITraceabilityHandler;
import com.pragma.traceability.application.mapper.ITraceRequestMapper;
import com.pragma.traceability.application.mapper.ITraceResponseMapper;
import com.pragma.traceability.domain.api.ITraceabilityServicePort;
import com.pragma.traceability.domain.model.OrderTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraceabilityHandler implements ITraceabilityHandler {

    private final ITraceabilityServicePort traceabilityServicePort;
    private final ITraceRequestMapper traceRequestMapper;
    private final ITraceResponseMapper traceResponseMapper;

    @Override
    public TraceResponseDto recordTrace(RecordTraceRequestDto request) {
        OrderTrace recorded = traceabilityServicePort.recordTrace(traceRequestMapper.toOrderTrace(request));
        return traceResponseMapper.toResponse(recorded);
    }

    @Override
    public List<TraceResponseDto> getOrderHistory(UUID orderId, UUID clientId) {
        return traceResponseMapper.toResponseList(
                traceabilityServicePort.getOrderHistory(orderId, clientId));
    }
}
