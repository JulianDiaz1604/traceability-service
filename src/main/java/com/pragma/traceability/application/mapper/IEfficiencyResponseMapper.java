package com.pragma.traceability.application.mapper;

import com.pragma.traceability.application.dto.response.EmployeeEfficiencyResponseDto;
import com.pragma.traceability.application.dto.response.OrderEfficiencyResponseDto;
import com.pragma.traceability.domain.model.EmployeeEfficiency;
import com.pragma.traceability.domain.model.OrderEfficiency;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface IEfficiencyResponseMapper {

    List<OrderEfficiencyResponseDto> toOrderEfficiencyList(List<OrderEfficiency> efficiencies);

    List<EmployeeEfficiencyResponseDto> toEmployeeEfficiencyList(List<EmployeeEfficiency> rankings);
}
