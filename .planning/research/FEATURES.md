# Features Research

## Table Stakes

### Goal Planning

- Create a goal with a title, category, target, unit, and yearly horizon
- Edit or archive an existing goal
- Support multiple goal shapes without forcing separate products for each domain

### Checkpoint Planning

- Generate a suggested yearly breakdown from the main target
- Let the user edit generated checkpoints before committing
- Display upcoming checkpoints clearly

### Progress Tracking

- Record progress updates over time
- Compare actual progress to expected progress at the current date
- Show whether a goal is behind, on track, or ahead

### Dashboard

- Summarize current goal health in one place
- Surface which goals need attention first
- Show trend and plan-versus-actual views without making the user open every goal

## Differentiators

- Motivation system based on accomplishment bands such as 80%, 100%, and 120%
- Backend-generated checkpoint suggestions instead of manual-only planning
- One shared progress model that can work across very different goal categories
- Language and visuals that make the product feel encouraging rather than punitive

## Anti-Features

- Social feeds, communities, public profiles, or goal sharing
- Complex team or family planning workflows
- Heavy gamification before the core product loop is validated
- Authentication in the first release
- Native mobile app scope during v1

## Complexity Notes

- Goal modeling across heterogeneous categories is the core design challenge
- Dashboard quality depends on having trustworthy status calculations underneath
- Motivation bands are only useful if the checkpoint and progress model feels fair and intuitive

## Dependencies

- Goal planning must exist before dashboard usefulness emerges
- Progress status depends on checkpoint generation or equivalent plan expectations
- Motivational layers depend on reliable progress calculations
