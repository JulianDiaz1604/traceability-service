package com.pragma.traceability.infrastructure.out.dynamo.mapper;

import com.pragma.traceability.domain.model.OrderTrace;
import com.pragma.traceability.infrastructure.out.dynamo.entity.OrderTraceEntity;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface IOrderTraceEntityMapper {

    OrderTraceEntity toEntity(OrderTrace trace);

    OrderTrace toModel(OrderTraceEntity entity);

    List<OrderTrace> toModelList(List<OrderTraceEntity> entities);
}
