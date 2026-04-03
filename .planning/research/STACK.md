# Stack Research

## Recommendation

Keep the repository's current contract-first stack and avoid a major platform pivot during v1. The existing Java/Spring Boot/OpenAPI/Angular/PostgreSQL setup already matches the stated priorities of backend-owned business logic, strong architecture, and a polished web UX.

## Recommended Core Stack

- Backend: Java 25 + Spring Boot 4
- API contract: OpenAPI 3.1.2 with the existing dedicated API module
- Persistence: PostgreSQL + Liquibase
- Frontend: Angular 21 + SCSS + generated OpenAPI client
- Build orchestration: Maven parent workspace plus npm for frontend tooling

## UI Component Library Direction

Recommended default for v1: Angular Material.

Why it fits:

- Better match for a first polished product pass where accessibility and consistency matter more than sheer component volume
- Lower visual debt than adopting a heavier data-grid-oriented library too early
- Easier to customize into a stronger product identity while keeping interaction primitives stable

PrimeNG remains a viable fallback if later phases need richer ready-made data-heavy widgets or complex scheduling tables, but it should not be the default until a concrete gap appears.

## Keep Versus Add

Keep:

- Contract-first API generation already present in the repo
- Backend-owned calculation logic
- Liquibase-based schema evolution
- Angular standalone and signal-friendly app direction

Add early:

- Proper backend package structure for domain, application, and infrastructure boundaries
- Initial Liquibase changelog tree
- Design-system foundation in the frontend instead of relying on starter markup
- Focused test layers around domain rules and progress calculations

## Avoid For v1

- Pushing progress logic or motivational scoring into Angular state code
- Adding a second backend framework or alternate persistence layer
- Introducing multiple UI libraries at once
- Building auth and multi-user concerns into the first core milestone

## Confidence

- High confidence in keeping the existing backend and contract-first stack
- Medium confidence in Angular Material as the default component choice until dashboard detail needs are clearer
