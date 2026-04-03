# Research Summary

## Stack

Keep the existing contract-first Java/Spring Boot/Angular/PostgreSQL stack. The repo already aligns with backend-owned domain logic, and Angular Material is the best default UI library choice for v1 unless later dashboard needs clearly justify PrimeNG.

## Table Stakes

Milestory needs strong goal planning, editable checkpoint generation, progress tracking, and a dashboard that clearly explains plan versus actual status. Those are the core product expectations for a personal yearly goal application.

## Differentiators

The strongest differentiators are backend-generated yearly checkpoints, accomplishment bands such as 80%/100%/120%, and a motivating presentation layer that makes progress feel actionable rather than judgmental.

## Watch Out For

- Do not overfit the domain model to specific goal categories too early
- Do not build dashboard polish before status calculations are trustworthy
- Do not let auth scope delay the personal goal-tracking core
- Do not ignore current scaffold gaps like missing Liquibase migrations and template leftovers

## Planning Guidance

The roadmap should begin with foundation cleanup and architectural skeleton work, then move into goal planning, progress calculations, and dashboard presentation. Authentication should remain a later phase after the personal single-user product loop is proven.
