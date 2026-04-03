# Pitfalls Research

## Pitfall 1: Designing Goal Types Too Rigidly

Warning signs:

- Every new goal category requires new tables and custom flows
- Shared dashboard logic becomes impossible across goal types

Prevention:

- Start with a shared goal model that supports target, unit, cadence, and progress entries
- Introduce category-specific extensions only when concrete use cases force them

Suggested phase:

- Phase 1 and Phase 2

## Pitfall 2: Building Dashboard UI Before Trustworthy Calculations

Warning signs:

- Dashboard looks polished but statuses are inconsistent or hard to explain
- Users cannot understand why a goal is marked behind or ahead

Prevention:

- Implement checkpoint and status calculations first
- Make dashboard components consume backend-owned summary models

Suggested phase:

- Phase 2 and Phase 3

## Pitfall 3: Letting Auth Distort The First Milestone

Warning signs:

- Identity, email, and JWT work consume the schedule before goal tracking exists
- Early schema and API design center around accounts instead of goals

Prevention:

- Keep v1 in personal mode
- Design the domain so auth can wrap it later instead of defining it now

Suggested phase:

- Roadmap-level scope control

## Pitfall 4: Ignoring The Scaffold Gaps

Warning signs:

- Liquibase remains configured without migrations
- Placeholder Angular UI survives into feature work
- Architecture claims exist only in docs, not in package structure

Prevention:

- Make foundation cleanup an explicit early phase
- Treat template leftovers and missing migrations as delivery blockers

Suggested phase:

- Phase 1

## Pitfall 5: Choosing A UI Kit Before Defining Product Visual Direction

Warning signs:

- The app feels like a stock admin console
- Component defaults drive information architecture instead of product intent

Prevention:

- Choose a single library deliberately
- Establish core typography, spacing, dashboard composition, and state visuals before scaling feature screens

Suggested phase:

- Phase 3
