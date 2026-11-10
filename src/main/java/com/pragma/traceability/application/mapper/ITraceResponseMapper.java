package com.pragma.traceability.application.mapper;

import com.pragma.traceability.application.dto.response.TraceResponseDto;
import com.pragma.traceability.domain.model.OrderTrace;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ITraceResponseMapper {

    TraceResponseDto toResponse(OrderTrace trace);

    List<TraceResponseDto> toResponseList(List<OrderTrace> traces);
}
