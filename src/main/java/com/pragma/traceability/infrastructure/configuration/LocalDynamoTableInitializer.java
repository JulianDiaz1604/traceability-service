package com.pragma.traceability.infrastructure.configuration;

import com.pragma.traceability.infrastructure.out.dynamo.entity.OrderTraceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

/**
 * Creates the {@code order_traces} table on startup for local development against DynamoDB Local.
 *
 * <p>Guarded by the {@code local} profile so it never runs in CI or production: CI's
 * {@code contextLoads()} must not reach a live DynamoDB, and real environments manage the table
 * through infrastructure-as-code.</p>
 */
@Configuration
@Profile("local")
public class LocalDynamoTableInitializer {

    private static final Logger log = LoggerFactory.getLogger(LocalDynamoTableInitializer.class);

    @Bean
    public ApplicationRunner createOrderTraceTable(DynamoDbTable<OrderTraceEntity> orderTraceTable) {
        return args -> {
            try {
                orderTraceTable.createTable();
                log.info("Created DynamoDB table '{}' (local profile)", orderTraceTable.tableName());
            } catch (ResourceInUseException alreadyExists) {
                log.info("DynamoDB table '{}' already exists", orderTraceTable.tableName());
            }
        };
    }
}
