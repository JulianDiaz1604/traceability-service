package com.pragma.traceability.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Per-employee efficiency ranking entry (HU18, criterion 2): the average order duration
 * ({@code PENDING} → {@code DELIVERED}) across the completed orders the employee handled.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEfficiency {
    private UUID employeeId;
    private long averageDurationSeconds;
    private int completedOrders;
}
