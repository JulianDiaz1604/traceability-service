package com.pragma.traceability.domain.api;

import com.pragma.traceability.domain.model.EmployeeEfficiency;
import com.pragma.traceability.domain.model.OrderEfficiency;

import java.util.List;
import java.util.UUID;

public interface IEfficiencyServicePort {

    List<OrderEfficiency> getOrderEfficiency(UUID restaurantId, UUID ownerId);

    List<EmployeeEfficiency> getEmployeeRanking(UUID restaurantId, UUID ownerId);
}
