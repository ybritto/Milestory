# Feature Research: Milestory

## Table Stakes

### Goal Definition

- Users can create a goal with a title, category, metric, target value, and target period.
- Users can view all active goals in one place.
- Users can edit goal settings after creation.

### Planning

- The app suggests intermediate checkpoints rather than leaving the yearly target abstract.
- Users can adjust generated checkpoints when real life requires a different cadence.

### Tracking

- Users can log progress updates against a goal.
- The app translates raw progress into simple status language such as behind, on track, and ahead.

### Dashboard

- Users can see overall progress across goals.
- Each goal clearly communicates current status and next milestone.

## Differentiators

- Accomplishment tiers such as 80%, 100%, and 120% that make overachievement visible.
- Cross-goal momentum summaries that reinforce whether the year is trending well overall.
- Goal templates by category that help users set better initial plans.

## Anti-Features

- Social feeds, public challenges, and public accountability loops
- Gamification that overwhelms the core progress signal
- Generic note-taking without measurable outcomes
- Complex budgeting or fitness-tracker integrations before the core workflow is validated

## Dependencies

- Dashboard quality depends on strong goal and checkpoint models.
- Motivation features depend on reliable status and progress calculations.
- Authentication can be added late only if earlier phases keep domain logic user-agnostic and session boundaries clean.
