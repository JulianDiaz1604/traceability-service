package com.pragma.traceability.application.mapper;

import com.pragma.traceability.application.dto.request.RecordTraceRequestDto;
import com.pragma.traceability.domain.model.OrderTrace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ITraceRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OrderTrace toOrderTrace(RecordTraceRequestDto request);
}
