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
