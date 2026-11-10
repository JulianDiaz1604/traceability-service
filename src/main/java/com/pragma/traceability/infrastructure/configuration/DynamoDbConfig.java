package com.pragma.traceability.infrastructure.configuration;

import com.pragma.traceability.infrastructure.out.dynamo.entity.OrderTraceEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

/**
 * Wires the AWS SDK v2 DynamoDB Enhanced Client (HU17). The client is built lazily (no network call
 * at startup), so the Spring context — and CI {@code contextLoads()} — comes up without a live
 * DynamoDB. For local development point {@code dynamodb.endpoint} at DynamoDB Local.
 */
@Configuration
public class DynamoDbConfig {

    private final String region;
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private final String tableName;

    public DynamoDbConfig(
            @Value("${dynamodb.region:us-east-1}") String region,
            @Value("${dynamodb.endpoint:http://localhost:8000}") String endpoint,
            @Value("${dynamodb.access-key:local}") String accessKey,
            @Value("${dynamodb.secret-key:local}") String secretKey,
            @Value("${dynamodb.table-name:order_traces}") String tableName) {
        this.region = region;
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.tableName = tableName;
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<OrderTraceEntity> orderTraceTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table(tableName, TableSchema.fromBean(OrderTraceEntity.class));
    }
}
