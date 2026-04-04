---
phase: 03-progress-engine-and-status
plan: 01
subsystem: api
tags: [openapi, spring, ddd, tdd, progress-tracking]
requires:
  - phase: 02-goal-planning-and-checkpoints
    provides: goal aggregate, checkpoint planning contract, and goal detail/archive seams
provides:
  - progress-entry OpenAPI contract with backend-owned pace fields
  - framework-free progress recording use case and outbound port
  - checkpoint-path pace derivation service with backend-owned copy
affects: [phase-03-plan-02, phase-03-plan-03, goal-detail, generated-clients]
tech-stack:
  added: []
  patterns: [contract-first endpoint evolution, append-only cumulative progress entries, backend-owned pace copy]
key-files:
  created:
    - milestory-backend/src/main/java/com/ybritto/milestory/goal/application/usecase/RecordProgressEntryUseCase.java
    - milestory-backend/src/main/java/com/ybritto/milestory/goal/domain/GoalProgressStatusService.java
    - milestory-backend/src/test/java/com/ybritto/milestory/goal/application/usecase/RecordProgressEntryUseCaseTest.java
    - milestory-backend/src/test/java/com/ybritto/milestory/goal/domain/GoalProgressStatusServiceTest.java
  modified:
    - milestory-api/rest/api-v1.yaml
    - milestory-api/rest/paths/goal-by-id.yaml
    - milestory-api/rest/schemas/goal-planning.yaml
    - milestory-backend/src/main/java/com/ybritto/milestory/goal/in/controller/GoalController.java
key-decisions:
  - "Keep progress updates append-only and cumulative, with correction detection based on the latest entry by entryDate then recordedAt."
  - "Derive pace status from checkpoint interpolation on the backend with a named 5 percent of target tolerance constant."
  - "Keep user-facing pace summary and detail strings in the backend service so later UI work renders product-owned copy instead of recomputing tone."
patterns-established:
  - "RecordProgressEntryUseCase owns archived-goal rejection and correction classification before persistence adapters exist."
  - "GoalProgressStatusService returns one stable projection object with current, expected, percent, and copy fields for downstream adapters."
requirements-completed: [PROG-01, PROG-02, PROG-03]
duration: 19 min
completed: 2026-04-04
---

# Phase 3 Plan 1: Progress Contract And Domain Foundation Summary

**Progress-entry API contract, cumulative correction classification, and backend-owned pace-status derivation for goal tracking**

## Performance

- **Duration:** 19 min
- **Started:** 2026-04-04T17:18:00Z
- **Completed:** 2026-04-04T17:37:17Z
- **Tasks:** 2
- **Files modified:** 14

## Accomplishments
- Locked the Phase 3 OpenAPI contract for progress entry creation, pace fields, and checkpoint context annotations.
- Added a framework-free recording seam with a command, persistence port, and correction-aware use case.
- Added a backend pace-status service that computes current progress, expected progress, percent of target, and backend-owned status copy.

## Task Commits

1. **Task 1: Extend the contract for cumulative progress entries and pace-status read fields** - `b4e9163` (`feat`)
2. **Task 2: Create framework-free progress-entry and pace-status application contracts** - `367d1e9` (`test`)
3. **Task 2: Create framework-free progress-entry and pace-status application contracts** - `d35a805` (`feat`)

## Files Created/Modified

- `milestory-api/rest/api-v1.yaml` - mounts the new `/api/v1/goals/{goalId}/progress-entries` endpoint.
- `milestory-api/rest/paths/goal-by-id.yaml` - defines the progress-entry POST operation and archived-goal `409` response.
- `milestory-api/rest/schemas/goal-planning.yaml` - adds progress-entry schemas, `GoalPaceStatus`, goal pace fields, and checkpoint context fields.
- `milestory-backend/src/main/java/com/ybritto/milestory/goal/application/usecase/RecordProgressEntryUseCase.java` - records cumulative entries, rejects archived goals, and classifies corrections.
- `milestory-backend/src/main/java/com/ybritto/milestory/goal/domain/GoalProgressStatusService.java` - interpolates expected progress and returns backend-owned pace summaries/details.
- `milestory-backend/src/test/java/com/ybritto/milestory/goal/application/usecase/RecordProgressEntryUseCaseTest.java` - covers correction classification and archived-goal rejection.
- `milestory-backend/src/test/java/com/ybritto/milestory/goal/domain/GoalProgressStatusServiceTest.java` - covers `BEHIND`, `ON_PACE`, and `AHEAD` outcomes and exact copy ownership.

## Decisions Made

- Used the existing `IllegalStateException` conflict path for archived-goal progress rejection so later controller wiring can reuse the same HTTP mapping.
- Normalized the current cumulative value with trailing-zero stripping in the status service so domain assertions stay scale-stable.
- Left persistence, controller wiring, and response mapping out of scope for this plan and kept only the boundary seam required for compilation.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 3 - Blocking] Added `api-v1.yaml` path mount for the new progress endpoint**
- **Found during:** Task 1
- **Issue:** The plan file list omitted `milestory-api/rest/api-v1.yaml`, but without updating it the new endpoint would not exist in the actual OpenAPI document.
- **Fix:** Added the `/api/v1/goals/{goalId}/progress-entries` path reference in the root OpenAPI entrypoint.
- **Files modified:** `milestory-api/rest/api-v1.yaml`
- **Verification:** `mvn -q clean package` and generated frontend client includes progress-entry artifacts.
- **Committed in:** `b4e9163`

**2. [Rule 3 - Blocking] Added a compile-only controller stub after regenerating the Spring interface**
- **Found during:** Task 1
- **Issue:** Regenerated backend sources required `GoalController` to implement `recordGoalProgressEntry(...)`, causing `mvn -q clean package` to fail before later controller-wiring plans.
- **Fix:** Added a temporary `501 Not Implemented` controller method so the codebase compiles while keeping persistence and controller logic deferred.
- **Files modified:** `milestory-backend/src/main/java/com/ybritto/milestory/goal/in/controller/GoalController.java`
- **Verification:** `mvn -q clean package`
- **Committed in:** `b4e9163`

---

**Total deviations:** 2 auto-fixed (2 blocking)
**Impact on plan:** Both fixes were required to keep the contract change buildable. No persistence or UI scope was pulled forward.

## Issues Encountered

- `mvn -q clean package` initially failed after OpenAPI regeneration because the generated `GoalsApi` interface gained a new abstract method. The temporary boundary stub resolved that without pulling later-plan implementation into this plan.

## Known Stubs

- `milestory-backend/src/main/java/com/ybritto/milestory/goal/in/controller/GoalController.java:89` returns `501 Not Implemented` for `recordGoalProgressEntry(...)`. This is intentional for Plan `03-02`, which will wire the controller to the new use case and persistence layer.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness

- Phase `03-02` can now persist progress entries, map them through the controller boundary, and enrich goal detail/list reads using the locked field names and domain service.
- Phase `03-03` can consume `paceStatus`, `paceSummary`, `paceDetail`, `currentProgressValue`, `expectedProgressValueToday`, and `progressEntries` without inventing new naming.

## Self-Check: PASSED

---
*Phase: 03-progress-engine-and-status*
*Completed: 2026-04-04*
