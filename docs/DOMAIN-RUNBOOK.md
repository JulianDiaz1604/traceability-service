# Domain runbook — traceability-service

Living source of truth for the domain's behavior. Updated with every HU: business rules,
invariants, and the custom exception → message → HTTP status table.

---

## HU17 — Order traceability (status history)

- **Roles:** any authenticated caller records a trace (food-court-service); **Client** reads history.
- **Endpoints:**
  - `POST /api/v1/traces` — record one order status change (called by food-court-service).
  - `GET /api/v1/traces/{orderId}` — chronological history of an order (client only).
- **Use case:** `TraceabilityUseCase` (`recordTrace`, `getOrderHistory`).
- **Validator:** `TraceValidator.validateNewTrace` (pure required-field rule: `orderId`, `clientId`,
  `newStatus`).
- **Persistence:** DynamoDB via `OrderTraceDynamoAdapter` (`ITraceabilityPersistencePort`).
  - Table `order_traces`, partition key `orderId`, sort key `createdAt` — a single partition query
    returns every trace of an order in chronological order.

### Business rules / invariants
- **Criterion 1 — a record per status change.** `recordTrace` persists one `OrderTrace`
  (orderId, clientId, previousStatus, newStatus, employeeId). The service owns the record metadata:
  it always stamps a fresh `id` and `createdAt`, ignoring any values the caller sent.
- **Criterion 2 — a client reads only their own orders.** `getOrderHistory` loads the order's traces
  and verifies the stored `clientId` matches the authenticated client; otherwise
  `TraceNotFromClientException` (403). The client id comes from the JWT, never from the path/body.
- An order with no records raises `OrderTraceNotFoundException` (404).
- A missing required field on record raises `RequiredFieldException` (400).
- Recording requires a valid (forwarded) token; reading requires the **CLIENT** role
  (`SecurityConfig`).

### Integration
food-court-service calls `POST /api/v1/traces` synchronously on every order transition (JSON/HTTP),
forwarding the caller's JWT — consistent with the users/messaging integrations.

### Exceptions
| Exception | Message | HTTP |
|-----------|---------|------|
| `RequiredFieldException` | All required trace fields must be provided | 400 |
| `TraceNotFromClientException` | The order history does not belong to the requesting client | 403 |
| `OrderTraceNotFoundException` | No traceability history exists for the requested order | 404 |
| (unauthenticated request) | Authentication is required | 401 |
| (authenticated, non-client role on read) | (Spring Security forbidden) | 403 |

---

## HU18 — Order efficiency (owner report)

- **Role:** Owner
- **Endpoints:**
  - `GET /api/v1/efficiency/restaurants/{restaurantId}/orders` — per-order duration (criterion 1).
  - `GET /api/v1/efficiency/restaurants/{restaurantId}/employees` — per-employee average ranking (criterion 2).
- **Use case:** `EfficiencyUseCase` (`getOrderEfficiency`, `getEmployeeRanking`).
- **Persistence:** `ITraceabilityPersistencePort.findByRestaurantId` → `OrderTraceDynamoAdapter`
  (DynamoDB scan filtered by `restaurantId`; traces now carry `restaurantId`, sent by food-court).
- **Cross-service port:** `IRestaurantOwnershipPort.getOwnerId` → `FoodCourtRestClientAdapter`
  (`GET /api/v1/restaurants/{restaurantId}/owner` on food-court-service, JWT forwarded).

### Business rules / invariants
- **Efficiency metric (decided):** an order's duration is measured from **PENDING → DELIVERED**
  (creation to delivery). Only completed (delivered) orders are included; in-progress and cancelled
  orders are excluded.
- **Criterion 1 — per order:** for each completed order, `{orderId, startedAt, finishedAt,
  durationSeconds}`, sorted by `startedAt`.
- **Criterion 2 — per employee:** average order duration grouped by the employee who handled the
  final leg (the `employeeId` on the `DELIVERED` trace), with the completed-order count; the ranking
  is sorted ascending (fastest average first).
- **Ownership:** only the restaurant's owner may read its reports. The owner id comes from the JWT;
  the real owner is resolved from food-court-service and compared → `NotRestaurantOwnerException`
  (403) on mismatch. The persistence scan runs only after ownership passes.
- If the restaurant does not exist in food-court, `RestaurantNotFoundException` (404). If food-court
  is unreachable, `FoodCourtServiceUnavailableException` (502).

### Exceptions
| Exception | Message | HTTP |
|-----------|---------|------|
| `NotRestaurantOwnerException` | This efficiency report can only be viewed by the owner of the restaurant | 403 |
| `RestaurantNotFoundException` | The restaurant does not exist | 404 |
| `FoodCourtServiceUnavailableException` | The food-court service is currently unavailable; please try again later | 502 |
| (unauthenticated request) | Authentication is required | 401 |
| (authenticated, non-owner role) | (Spring Security forbidden) | 403 |

> HU18 also extends the trace schema with `restaurantId` (food-court sends it on every transition),
> which is what makes the per-restaurant efficiency scan possible.
