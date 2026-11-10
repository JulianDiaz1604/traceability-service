package com.pragma.traceability.application.handler.impl;

import com.pragma.traceability.application.dto.response.EmployeeEfficiencyResponseDto;
import com.pragma.traceability.application.dto.response.OrderEfficiencyResponseDto;
import com.pragma.traceability.application.handler.IEfficiencyHandler;
import com.pragma.traceability.application.mapper.IEfficiencyResponseMapper;
import com.pragma.traceability.domain.api.IEfficiencyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EfficiencyHandler implements IEfficiencyHandler {

    private final IEfficiencyServicePort efficiencyServicePort;
    private final IEfficiencyResponseMapper efficiencyResponseMapper;

    @Override
    public List<OrderEfficiencyResponseDto> getOrderEfficiency(UUID restaurantId, UUID ownerId) {
        return efficiencyResponseMapper.toOrderEfficiencyList(
                efficiencyServicePort.getOrderEfficiency(restaurantId, ownerId));
    }

    @Override
    public List<EmployeeEfficiencyResponseDto> getEmployeeRanking(UUID restaurantId, UUID ownerId) {
        return efficiencyResponseMapper.toEmployeeEfficiencyList(
                efficiencyServicePort.getEmployeeRanking(restaurantId, ownerId));
    }
}
