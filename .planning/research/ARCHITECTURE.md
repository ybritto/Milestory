# Architecture Research: Milestory

## Core Components

### Goal Planning Domain

Responsible for goal definitions, category semantics, measurement units, targets, and yearly planning rules.

### Checkpoint Engine

Responsible for generating default checkpoints, validating edits, and exposing expected progress curves over time.

### Progress Tracking Domain

Responsible for progress entries, normalization, cumulative totals, and derived status calculations.

### Dashboard Read Models

Responsible for aggregating domain data into frontend-friendly views such as status cards, trend summaries, and accomplishment tiers.

### Identity and Access

Responsible for the email-first entry flow, JWT authentication, and ownership boundaries around private goal data.

## Data Flow

1. User defines a goal in the frontend.
2. Backend stores the goal and generates checkpoint suggestions.
3. User reviews or edits checkpoints.
4. User logs progress updates over time.
5. Backend recalculates expected-vs-actual progress and accomplishment tiers.
6. Dashboard endpoints return aggregated views for the UI.
7. Authentication later restricts access to the user’s own plans and dashboard data.

## Boundary Guidance

- Backend owns rules, calculations, validations, and derived states.
- Frontend owns input flow, display logic, and interaction design.
- OpenAPI contracts define the public seam between frontend and backend.

## Suggested Build Order

1. Establish domain model and contract boundaries.
2. Build goal creation and checkpoint suggestion flow.
3. Add progress logging and status evaluation.
4. Build dashboard read models and frontend experience.
5. Add motivation tiers and celebratory feedback.
6. Add email-first authentication and authorization last.
