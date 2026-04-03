# Roadmap: Milestory

## Overview

Milestory will be built from the inside out: first a strong domain backbone for measurable goals, then checkpoint planning, then progress tracking, then a motivating dashboard experience, and finally the email-first authentication layer that personalizes and protects the product. This sequencing preserves the backend-first architecture and lets the team validate the core value loop before identity work becomes the final gate to release.

## Phases

**Phase Numbering:**
- Integer phases (1, 2, 3): Planned milestone work
- Decimal phases (2.1, 2.2): Urgent insertions (marked with INSERTED)

Decimal phases appear between their surrounding integers in numeric order.

- [ ] **Phase 1: Domain Foundation** - Establish the measurable goal model and backend-owned planning rules.
- [ ] **Phase 2: Goal Setup and Checkpoints** - Deliver goal creation and editable checkpoint suggestion flows.
- [ ] **Phase 3: Progress Tracking Engine** - Support progress logging and backend status evaluation.
- [ ] **Phase 4: Dashboard and Motivation** - Deliver the landing dashboard, goal insights, and accomplishment tiers.
- [ ] **Phase 5: Email-First Identity** - Add authentication, authorization, and the email-driven entry flow.

## Phase Details

### Phase 1: Domain Foundation
**Goal**: Establish the backend contract, domain model, and architectural seams required for measurable yearly goals and future user ownership.
**Depends on**: Nothing (first phase)
**Requirements**: GOAL-01, GOAL-03
**Success Criteria** (what must be TRUE):
  1. Backend APIs and domain objects can represent measurable goals across multiple categories.
  2. Goal definitions include the data needed for future checkpoint generation and progress tracking.
  3. Ownership and security seams are prepared so authentication can be added later without rewriting the domain.
**Plans**: 3 plans

Plans:
- [ ] 01-01: Define backend goal domain, validation rules, and OpenAPI contracts.
- [ ] 01-02: Establish persistence, mapping, and service boundaries for goal categories and measurements.
- [ ] 01-03: Create frontend app shell and API integration seams for the upcoming goal setup flow.

### Phase 2: Goal Setup and Checkpoints
**Goal**: Let a user create and edit goals with automatically suggested but editable checkpoints.
**Depends on**: Phase 1
**Requirements**: GOAL-02, PLAN-01, PLAN-02
**Success Criteria** (what must be TRUE):
  1. User can create a goal with measurable settings through the UI.
  2. Newly created goals receive backend-generated checkpoint suggestions.
  3. User can edit checkpoint suggestions and save the resulting plan.
**Plans**: 3 plans

Plans:
- [ ] 02-01: Implement checkpoint generation and editing rules in backend services.
- [ ] 02-02: Build the goal setup flow and checkpoint editor in Angular.
- [ ] 02-03: Add integration and UI tests for goal setup and checkpoint editing.

### Phase 3: Progress Tracking Engine
**Goal**: Enable progress logging and convert raw updates into trustworthy status signals.
**Depends on**: Phase 2
**Requirements**: TRAK-01, TRAK-02
**Success Criteria** (what must be TRUE):
  1. User can record progress updates against a goal.
  2. Backend evaluates current progress versus the plan and returns below-plan, on-track, or above-plan status.
  3. Progress history can drive later dashboard visualizations without recalculating business logic in the frontend.
**Plans**: 3 plans

Plans:
- [ ] 03-01: Implement progress-entry APIs, persistence, and cumulative evaluation logic.
- [ ] 03-02: Build the progress logging flow in Angular.
- [ ] 03-03: Verify status calculations across representative goal categories and edge cases.

### Phase 4: Dashboard and Motivation
**Goal**: Deliver a polished dashboard that explains yearly momentum and reinforces positive progress through accomplishment tiers.
**Depends on**: Phase 3
**Requirements**: DASH-01, DASH-02, MOTV-01, MOTV-02
**Success Criteria** (what must be TRUE):
  1. User can land on a dashboard that summarizes all active goals.
  2. Each goal view clearly shows current progress, next checkpoint, and trend.
  3. Accomplishment tiers such as 80%, 100%, and 120% are visible and meaningful.
  4. The UI experience feels motivating rather than purely administrative.
**Plans**: 3 plans

Plans:
- [ ] 04-01: Build backend dashboard read models and motivational tier calculations.
- [ ] 04-02: Implement the dashboard and goal detail experience with the chosen component library.
- [ ] 04-03: Refine UX, accessibility, and visual language for the dashboard journey.

### Phase 5: Email-First Identity
**Goal**: Add authentication and authorization without compromising the established domain model or dashboard flow.
**Depends on**: Phase 4
**Requirements**: AUTH-01, AUTH-02, AUTH-03
**Success Criteria** (what must be TRUE):
  1. User enters an email first and is directed to the correct sign-up or login flow.
  2. User can authenticate with email and password and receive JWT-backed access to the app.
  3. Goal, checkpoint, progress, and dashboard data are isolated to the authenticated user.
**Plans**: 3 plans

Plans:
- [ ] 05-01: Implement account lookup, registration, login, and JWT security backend flows.
- [ ] 05-02: Build the email-first entry, sign-up, and login screens in Angular.
- [ ] 05-03: Add authorization coverage and end-to-end verification for personalized data access.

## Progress

**Execution Order:**
Phases execute in numeric order: 1 → 2 → 3 → 4 → 5

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 1. Domain Foundation | 0/3 | Not started | - |
| 2. Goal Setup and Checkpoints | 0/3 | Not started | - |
| 3. Progress Tracking Engine | 0/3 | Not started | - |
| 4. Dashboard and Motivation | 0/3 | Not started | - |
| 5. Email-First Identity | 0/3 | Not started | - |
