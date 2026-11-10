package com.pragma.traceability.infrastructure.out.dynamo.adapter;

import com.pragma.traceability.domain.model.OrderTrace;
import com.pragma.traceability.domain.spi.ITraceabilityPersistencePort;
import com.pragma.traceability.infrastructure.out.dynamo.entity.OrderTraceEntity;
import com.pragma.traceability.infrastructure.out.dynamo.mapper.IOrderTraceEntityMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.UUID;

/**
 * DynamoDB output adapter (HU17). Persists and queries order traces through the Enhanced Client.
 * A query by the {@code orderId} partition key returns every trace of that order.
 */
public class OrderTraceDynamoAdapter implements ITraceabilityPersistencePort {

    private final DynamoDbTable<OrderTraceEntity> orderTraceTable;
    private final IOrderTraceEntityMapper orderTraceEntityMapper;

    public OrderTraceDynamoAdapter(DynamoDbTable<OrderTraceEntity> orderTraceTable,
                                   IOrderTraceEntityMapper orderTraceEntityMapper) {
        this.orderTraceTable = orderTraceTable;
        this.orderTraceEntityMapper = orderTraceEntityMapper;
    }

    @Override
    public OrderTrace save(OrderTrace trace) {
        orderTraceTable.putItem(orderTraceEntityMapper.toEntity(trace));
        return trace;
    }

    @Override
    public List<OrderTrace> findByOrderId(UUID orderId) {
        QueryConditional byOrder = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(orderId.toString()).build());
        List<OrderTraceEntity> entities = orderTraceTable.query(byOrder)
                .items()
                .stream()
                .toList();
        return orderTraceEntityMapper.toModelList(entities);
    }
}
