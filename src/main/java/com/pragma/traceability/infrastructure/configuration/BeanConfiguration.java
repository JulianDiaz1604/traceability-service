package com.pragma.traceability.infrastructure.configuration;

import com.pragma.traceability.domain.api.IEfficiencyServicePort;
import com.pragma.traceability.domain.api.ITraceabilityServicePort;
import com.pragma.traceability.domain.spi.IRestaurantOwnershipPort;
import com.pragma.traceability.domain.spi.ITraceabilityPersistencePort;
import com.pragma.traceability.domain.usecase.EfficiencyUseCase;
import com.pragma.traceability.domain.usecase.TraceabilityUseCase;
import com.pragma.traceability.domain.validation.TraceValidator;
import com.pragma.traceability.infrastructure.out.client.AuthorizationForwardingInterceptor;
import com.pragma.traceability.infrastructure.out.client.FoodCourtRestClientAdapter;
import com.pragma.traceability.infrastructure.out.dynamo.adapter.OrderTraceDynamoAdapter;
import com.pragma.traceability.infrastructure.out.dynamo.entity.OrderTraceEntity;
import com.pragma.traceability.infrastructure.out.dynamo.mapper.IOrderTraceEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
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

    // ---- cross-service HTTP client beans (HU18: verify restaurant ownership) ----

    @Bean
    public AuthorizationForwardingInterceptor authorizationForwardingInterceptor() {
        return new AuthorizationForwardingInterceptor();
    }

    @Bean
    public RestClient foodCourtRestClient(
            @Value("${food-court-service.base-url}") String baseUrl,
            AuthorizationForwardingInterceptor authorizationForwardingInterceptor) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(authorizationForwardingInterceptor)
                .build();
    }

    @Bean
    public IRestaurantOwnershipPort restaurantOwnershipPort(RestClient foodCourtRestClient) {
        return new FoodCourtRestClientAdapter(foodCourtRestClient);
    }

    @Bean
    public IEfficiencyServicePort efficiencyServicePort(IRestaurantOwnershipPort restaurantOwnershipPort) {
        return new EfficiencyUseCase(traceabilityPersistencePort(), restaurantOwnershipPort);
    }
}
