package com.pragma.traceability.infrastructure.out.dynamo.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.UUID;

/**
 * DynamoDB item for an order status-change record (HU17). The table is keyed by {@code orderId}
 * (partition) + {@code createdAt} (sort), so all traces of an order are retrieved with a single
 * partition query and come back in chronological order.
 *
 * <p>Explicit accessors (no Lombok) because the Enhanced Client reads the {@code @DynamoDb*} key
 * annotations from the getter methods.</p>
 */
@DynamoDbBean
public class OrderTraceEntity {

    private UUID id;
    private UUID orderId;
    private UUID clientId;
    private String previousStatus;
    private String newStatus;
    private UUID employeeId;
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @DynamoDbPartitionKey
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    @DynamoDbSortKey
    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
