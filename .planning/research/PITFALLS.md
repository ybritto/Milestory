# Pitfalls Research: Milestory

## Pitfall 1: Treating goals as generic notes instead of measurable systems

Warning signs:
- Goals lack units, cadence, or target logic
- Dashboard status becomes subjective

Prevention:
- Require measurable goal definitions from the domain model onward
- Encode goal type semantics explicitly in backend rules

Suggested phase:
- Phase 1

## Pitfall 2: Letting the frontend own progress calculations

Warning signs:
- Angular computes on-track status locally
- Backend returns only raw numbers with no interpretation

Prevention:
- Expose derived status, progress percentages, and accomplishment tiers from backend APIs
- Keep frontend display-oriented

Suggested phase:
- Phase 1 and Phase 3

## Pitfall 3: Auto-generated checkpoints that feel arbitrary

Warning signs:
- Generated milestones are mathematically even but behaviorally unrealistic
- Users immediately need to rewrite every suggestion

Prevention:
- Support category-aware defaults and editable checkpoints
- Make checkpoint rules explicit and testable

Suggested phase:
- Phase 2

## Pitfall 4: Dashboard visuals without actionable meaning

Warning signs:
- Attractive charts but no clear next step
- User cannot tell what to do when behind

Prevention:
- Every dashboard card should express status, current gap, and next checkpoint
- Design API read models around user decisions, not generic chart data

Suggested phase:
- Phase 3

## Pitfall 5: Adding auth late without preparing for ownership boundaries

Warning signs:
- Early domain data assumes a single hard-coded user
- Retrofitting access rules touches every endpoint

Prevention:
- Design entities and APIs with future ownership fields and security seams
- Keep a clear adapter layer where JWT-based user context can attach later

Suggested phase:
- Phase 1 and Phase 5
