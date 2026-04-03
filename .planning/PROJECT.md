# Milestory

## What This Is

Milestory is a web application for setting yearly goals and following them with clear progress signals throughout the year. It helps a user define measurable resolutions, receive suggested checkpoints, track ongoing updates, and understand whether each goal is on track, below plan, or ahead of plan through a polished dashboard experience.

## Core Value

Turn long-term resolutions into clear, motivating progress signals so the user always knows what to do next and whether they are winning.

## Requirements

### Validated

(None yet — ship to validate)

### Active

- [ ] Users can create measurable yearly goals across multiple life domains such as fitness, finance, reading, and weight management.
- [ ] Users receive editable checkpoint suggestions for each goal so a yearly target becomes a practical plan.
- [ ] Users can log progress and immediately understand whether they are behind, on track, or ahead.
- [ ] Users see motivating accomplishment levels such as 80%, 100%, and 120% to encourage consistency and stretch outcomes.
- [ ] Users can access a personalized dashboard after authentication, but authentication is intentionally implemented after the core goal-tracking experience.

### Out of Scope

- Native mobile apps — web-first delivery keeps the MVP focused on product quality and backend correctness.
- Social features, leaderboards, and public goal sharing — motivation in v1 is personal progress, not social comparison.
- AI coaching or LLM-generated advice — not required to validate the core resolution-tracking loop.
- Rich offline support — useful later, but not part of the first validated release.

## Context

The project starts from a reusable Java/Spring/Angular/OpenAPI template that has been partially renamed to Milestory but still contains template leftovers in package names and some frontend metadata. The product direction emphasizes excellence in UX and technical architecture, with business logic centered in the backend. The user wants authentication to be email-first and JWT-based, but also wants the MVP to prioritize goal modeling, progress tracking, checkpoint planning, and dashboards before the auth flow is fully implemented.

The current technical baseline already includes Java 25, Spring Boot 4, Angular 21, and OpenAPI 3.2. Component-library choice is still open between Angular Material and PrimeNG, and that decision should be made early because it will influence dashboard patterns, form density, and long-term UI consistency.

## Constraints

- **Tech stack**: Java 25, Spring Boot 4, Angular 21, and OpenAPI 3.2 are already chosen — the architecture should build on the existing template rather than replacing it.
- **Architecture**: Core business logic must remain in the backend — progress calculations, checkpoint generation, status evaluation, and motivational tier logic should not live in the Angular client.
- **UX quality**: Dashboard clarity and guided goal setup are primary product drivers — feature design should prefer understandable flows over feature volume.
- **Identity timing**: The repository is still partially in template state — planning can proceed, but broad rename cleanup should happen before substantial implementation to avoid drift.
- **Authentication sequencing**: Email-first JWT authentication is required, but it should land after the main goal-tracking loop is working — phase ordering must respect that.

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| Build Milestory around yearly measurable goals with progress checkpoints | This is the clearest expression of the product vision and drives the data model | — Pending |
| Keep backend as the source of truth for planning and progress evaluation | Ensures consistent calculations and simpler clients | — Pending |
| Defer authentication implementation until after the core goal workflow | Allows the MVP to validate the main user value before spending time on account flows | — Pending |
| Keep Milestory as the project identity for now | User confirmed the application name and package namespace can proceed as-is for planning | — Pending |
| Defer template cleanup and brownfield codebase mapping until after initialization | Keeps momentum on project setup while making the tradeoff explicit | ⚠️ Revisit |
| Decide between Angular Material and PrimeNG during early planning, not ad hoc during implementation | The component library will shape UX consistency and implementation patterns | — Pending |

## Evolution

This document evolves at phase transitions and milestone boundaries.

**After each phase transition** (via `$gsd-transition`):
1. Requirements invalidated? → Move to Out of Scope with reason
2. Requirements validated? → Move to Validated with phase reference
3. New requirements emerged? → Add to Active
4. Decisions to log? → Add to Key Decisions
5. "What This Is" still accurate? → Update if drifted

**After each milestone** (via `$gsd-complete-milestone`):
1. Full review of all sections
2. Core Value check — still the right priority?
3. Audit Out of Scope — reasons still valid?
4. Update Context with current state

---
*Last updated: 2026-04-03 after initialization*
