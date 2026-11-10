package com.pragma.traceability.infrastructure.configuration;

import com.pragma.traceability.domain.api.ITraceabilityServicePort;
import com.pragma.traceability.domain.spi.ITraceabilityPersistencePort;
import com.pragma.traceability.domain.usecase.TraceabilityUseCase;
import com.pragma.traceability.domain.validation.TraceValidator;
import com.pragma.traceability.infrastructure.out.dynamo.adapter.OrderTraceDynamoAdapter;
import com.pragma.traceability.infrastructure.out.dynamo.entity.OrderTraceEntity;
import com.pragma.traceability.infrastructure.out.dynamo.mapper.IOrderTraceEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final DynamoDbTable<OrderTraceEntity> orderTraceTable;
    private final IOrderTraceEntityMapper orderTraceEntityMapper;

    @Bean
    public ITraceabilityPersistencePort traceabilityPersistencePort() {
        return new OrderTraceDynamoAdapter(orderTraceTable, orderTraceEntityMapper);
    }

    @Bean
    public TraceValidator traceValidator() {
        return new TraceValidator();
    }

    @Bean
    public ITraceabilityServicePort traceabilityServicePort() {
        return new TraceabilityUseCase(traceabilityPersistencePort(), traceValidator());
    }
}
