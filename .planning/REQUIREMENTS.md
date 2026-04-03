# Requirements: Milestory

**Defined:** 2026-04-03
**Core Value:** Turn long-term resolutions into clear, motivating progress signals so the user always knows what to do next and whether they are winning.

## v1 Requirements

### Goal Setup

- [ ] **GOAL-01**: User can create a yearly goal with a title, category, unit, starting value, and target value.
- [ ] **GOAL-02**: User can edit an existing goal’s target or measurement settings without recreating the goal.
- [ ] **GOAL-03**: User can maintain goals across different categories such as finance, fitness, reading, and weight management.

### Checkpoints

- [ ] **PLAN-01**: User receives automatically suggested checkpoints for a newly created yearly goal.
- [ ] **PLAN-02**: User can edit suggested checkpoints before or after saving the goal.

### Progress Tracking

- [ ] **TRAK-01**: User can log progress updates against a goal over time.
- [ ] **TRAK-02**: User can see whether a goal is below plan, on track, or above plan based on backend-evaluated progress.

### Dashboard

- [ ] **DASH-01**: User can view a landing dashboard that summarizes the status of all active goals.
- [ ] **DASH-02**: User can open a goal and see current progress, upcoming checkpoint, and progress trend in a clear visual format.

### Motivation

- [ ] **MOTV-01**: User can see accomplishment levels such as 80%, 100%, and 120% for each goal.
- [ ] **MOTV-02**: User receives positive visual reinforcement when progress is ahead of plan or above target.

### Authentication

- [ ] **AUTH-01**: User can enter an email first and be routed to sign-up or login based on whether the account already exists.
- [ ] **AUTH-02**: User can sign up and log in with email, password, and JWT-based session handling.
- [ ] **AUTH-03**: Authenticated user can access only their own goals, checkpoints, progress entries, and dashboard data.

## v2 Requirements

### Authentication

- **AUTH-04**: User receives email verification during account creation.
- **AUTH-05**: User can reset password through an email-based recovery flow.

### Goal Intelligence

- **GOAL-04**: User can start from reusable goal templates tuned for common resolution types.
- **MOTV-03**: User receives contextual nudges when a goal is drifting off plan.

### Platform

- **PLAT-01**: User can access the same experience from a dedicated mobile app.
- **PLAT-02**: User can connect external services such as fitness or finance trackers.

## Out of Scope

| Feature | Reason |
|---------|--------|
| Social goal sharing | Not core to validating personal resolution tracking |
| Public leaderboards or challenges | Adds social complexity before the main loop is proven |
| AI coaching | Not necessary for the first version of progress clarity |
| OAuth providers | Email/password is sufficient for the intended v1 identity flow |
| Offline-first sync | Valuable later, but not required for MVP validation |

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| GOAL-01 | Phase 1 | Pending |
| GOAL-02 | Phase 2 | Pending |
| GOAL-03 | Phase 1 | Pending |
| PLAN-01 | Phase 2 | Pending |
| PLAN-02 | Phase 2 | Pending |
| TRAK-01 | Phase 3 | Pending |
| TRAK-02 | Phase 3 | Pending |
| DASH-01 | Phase 4 | Pending |
| DASH-02 | Phase 4 | Pending |
| MOTV-01 | Phase 4 | Pending |
| MOTV-02 | Phase 4 | Pending |
| AUTH-01 | Phase 5 | Pending |
| AUTH-02 | Phase 5 | Pending |
| AUTH-03 | Phase 5 | Pending |

**Coverage:**
- v1 requirements: 14 total
- Mapped to phases: 14
- Unmapped: 0

---
*Requirements defined: 2026-04-03*
*Last updated: 2026-04-03 after initialization*
