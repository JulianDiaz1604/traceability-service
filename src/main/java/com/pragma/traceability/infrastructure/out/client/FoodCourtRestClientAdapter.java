package com.pragma.traceability.infrastructure.out.client;

import com.pragma.traceability.domain.exception.FoodCourtServiceUnavailableException;
import com.pragma.traceability.domain.exception.RestaurantNotFoundException;
import com.pragma.traceability.domain.spi.IRestaurantOwnershipPort;
import com.pragma.traceability.infrastructure.out.client.dto.OwnerIdResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

/**
 * Infrastructure adapter that resolves a restaurant's owner from food-court-service (HU18).
 *
 * <ul>
 *   <li>{@code GET /api/v1/restaurants/{restaurantId}/owner}</li>
 * </ul>
 *
 * The inbound {@code Authorization} header is forwarded via {@link AuthorizationForwardingInterceptor}.
 */
public class FoodCourtRestClientAdapter implements IRestaurantOwnershipPort {

    private static final String OWNER_PATH = "/api/v1/restaurants/{restaurantId}/owner";

    private final RestClient restClient;

    public FoodCourtRestClientAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public UUID getOwnerId(UUID restaurantId) {
        try {
            OwnerIdResponseDto response = restClient.get()
                    .uri(OWNER_PATH, restaurantId)
                    .retrieve()
                    .body(OwnerIdResponseDto.class);
            if (response == null || response.getOwnerId() == null) {
                throw new FoodCourtServiceUnavailableException();
            }
            return response.getOwnerId();
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RestaurantNotFoundException();
            }
            throw new FoodCourtServiceUnavailableException();
        } catch (RestClientException exception) {
            throw new FoodCourtServiceUnavailableException();
        }
    }
}
