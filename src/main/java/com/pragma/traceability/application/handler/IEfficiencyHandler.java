package com.pragma.traceability.application.handler;

import com.pragma.traceability.application.dto.response.EmployeeEfficiencyResponseDto;
import com.pragma.traceability.application.dto.response.OrderEfficiencyResponseDto;

import java.util.List;
import java.util.UUID;

public interface IEfficiencyHandler {

    List<OrderEfficiencyResponseDto> getOrderEfficiency(UUID restaurantId, UUID ownerId);

    List<EmployeeEfficiencyResponseDto> getEmployeeRanking(UUID restaurantId, UUID ownerId);
}
