package com.pragma.traceability.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEfficiencyResponseDto {
    private UUID employeeId;
    private long averageDurationSeconds;
    private int completedOrders;
}
