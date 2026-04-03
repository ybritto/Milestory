# Stack Research: Milestory

## Baseline

Milestory already has a strong contract-first baseline:

- Java 25
- Spring Boot 4
- Angular 21
- OpenAPI 3.2

This stack fits the stated priorities well: backend-owned logic, strong API contracts, and a modern SPA for the dashboard-heavy experience.

## Recommended Direction

- Keep the contract-first workflow with OpenAPI as the shared source of truth between backend and frontend.
- Put all goal progression rules, checkpoint generation, status evaluation, and accomplishment-tier logic in backend services.
- Keep Angular focused on orchestration, visualization, accessibility, and stateful UI flows.
- Standardize UI components early rather than mixing patterns later.

## UI Library Decision

Two realistic options fit the current stack:

### Angular Material

Best fit if the priority is long-term consistency, accessibility defaults, and a more controlled design system.

Why it fits:
- Strong accessibility baseline
- Good foundation for custom dashboard styling
- Lower visual lock-in if Milestory wants a distinct product identity

Tradeoff:
- Some dashboard widgets may need more custom composition work

### PrimeNG

Best fit if the priority is shipping dense, ready-made dashboard widgets quickly.

Why it fits:
- Broad component catalog
- Faster path to tables, charts, overlays, and data-heavy admin-style screens

Tradeoff:
- Easier to drift into a generic enterprise look unless intentionally restyled

## Recommendation

Favor Angular Material for Milestory unless the upcoming UI phase proves PrimeNG materially reduces delivery risk for the dashboard experience. The project goal emphasizes UX excellence, and Material gives a cleaner base for a distinct, polished product language.

## What Not To Do

- Do not split business calculations across frontend and backend.
- Do not choose a component library phase by phase.
- Do not let generated API models become the domain model boundary in backend business logic.
