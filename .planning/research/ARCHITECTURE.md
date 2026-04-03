# Architecture Research

## Recommended System Shape

Milestory should be implemented as a backend-centered domain application with a thin but polished Angular client. The frontend should orchestrate views and interactions, while the backend owns goal modeling, checkpoint generation, progress aggregation, status evaluation, and motivational band calculations.

## Suggested Bounded Areas

### Goal Definition

Owns goal identity, category, target values, units, time horizon, and lifecycle state.

### Checkpoint Planning

Owns suggested milestones, editable checkpoint schedules, and expected progress curves across the year.

### Progress Tracking

Owns recorded progress entries, aggregation logic, and comparisons between actual and planned progress.

### Dashboard And Insights

Owns summary projections, status groupings, and accomplishment band views for presentation to the frontend.

## Data Flow

1. User defines or edits a goal in the frontend
2. Backend validates the request and persists the goal
3. Backend generates or updates suggested checkpoints
4. User records progress events
5. Backend recalculates plan-versus-actual status and motivational bands
6. Dashboard endpoints return pre-computed or application-assembled summaries for rendering

## Build Order Implications

1. Establish project skeleton cleanup and architectural boundaries
2. Build goal and checkpoint domain model
3. Build progress recording and status calculation
4. Build dashboard queries and presentation endpoints
5. Add auth only after the personal product loop works well

## Architectural Warnings

- Do not model each goal type as a completely separate subsystem unless the shared model clearly fails
- Do not let the frontend invent status logic that differs from backend truth
- Do not postpone migration setup and domain boundaries until after UI work begins
