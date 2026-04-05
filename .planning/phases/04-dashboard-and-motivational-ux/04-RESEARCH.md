# Phase 4: Dashboard And Motivational UX - Research

**Researched:** 2026-04-06
**Domain:** Angular dashboard UX, application shell routing, motivational progress presentation
**Confidence:** HIGH

<user_constraints>
## User Constraints (from CONTEXT.md)

### Locked Decisions

### Navigation Shell

- The app should adopt a persistent application shell with sidebar navigation across dashboard, active goals, archive, and goal detail.
- The sidebar should include `Dashboard`, `Active goals`, `Archive`, and a strong `New goal` action.
- The current home page should stop being the primary shipped destination once the dashboard shell exists.
- The sidebar should stay visible on desktop and collapse into a drawer on mobile and tablet.

### Dashboard Information Architecture

- The dashboard should lead with a balanced overview rather than purely risk-first or celebration-first framing.
- The top section should act as a practical command center showing counts, the strongest risk, the strongest win, and a suggested next goal to open.
- Goal summaries should then be grouped into sections such as `Behind`, `On pace`, and `Ahead`.
- Within each group, the most urgent goals should appear first.
- Strongest risk and strongest win callouts should route the user into goal detail for the next concrete action.

### Motivational Presentation

- Accomplishment bands should feel bold and highly celebratory rather than subtle.
- Celebration should stay tied to meaningful progress milestones like the roadmap-defined accomplishment thresholds, not shallow gamification.

### Card Density

- Dashboard cards should use a medium-density summary model.
- Each card should include concise status context plus `actual vs expected`, `next checkpoint`, and the strongest accomplishment or risk signal.
- The dashboard should remain a decision surface, not a stacked set of mini detail pages.

### Claude's Discretion

The implementation can still choose the exact visual treatment, motion, copy tone, iconography, empty states, loading states, and responsive layout details as long as those choices preserve the decisions above and the product direction already established in prior phases.

### Deferred Ideas (OUT OF SCOPE)

- Direct progress logging from the dashboard is deferred; dashboard callouts should open goal detail instead.
- Direct checkpoint editing from the dashboard is deferred.
- Broader product areas outside the dashboard shell and motivational goal overview remain out of scope for this phase.
</user_constraints>

<phase_requirements>
## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| PLAT-03 | User can interact with a web UI that reflects the real Milestory product instead of starter placeholder content | Replace the root home route with a shell-first routed app, add a real dashboard route, and preserve Milestory-specific styling rather than starter placeholder content |
| DASH-01 | User lands on a dashboard that summarizes current goal health | Build a dashboard page backed by `GoalsService.listGoals('ACTIVE')`, with a command-center summary and grouped sections |
| DASH-02 | User can identify which goals need attention first from the dashboard | Sort and group using backend-owned fields (`paceStatus`, `expectedProgressValueToday`, `progressPercentOfTarget`) without inventing new client-side status logic |
| DASH-03 | User can see accomplishment levels such as 80%, 100%, and 120% for relevant goals | Centralize milestone thresholds in a shared presenter/utility and surface them in dashboard cards and goal detail badges |
| DASH-04 | User can drill from the dashboard into goal-level progress detail | Use router-linked cards and callouts inside a persistent shell, plus explicit active-link/focus behavior for clear navigation |
</phase_requirements>

## Summary

Phase 4 should be planned as a frontend architecture and product-shell phase, not just a page redesign. The repo already has the needed backend-owned pace model, a signal-based Angular 21 app, and goal detail/archive routes. The missing piece is a persistent shell that turns those routes into one coherent product surface, with the dashboard as the default landing experience.

Use the existing active-goal list contract first. `ListGoals200ResponseInner` already carries `paceStatus`, `paceSummary`, `paceDetail`, `currentProgressValue`, `expectedProgressValueToday`, `progressPercentOfTarget`, checkpoints, and progress entries. That is enough to build dashboard grouping, strongest-risk/win callouts, accomplishment bands, and drill-down links without introducing new frontend-owned domain math. Keep any client-side derivation limited to presentation ordering and threshold display.

Do not introduce a full UI library reset in this phase. The existing Milestory frontend already has a warm, custom visual language and standalone Angular patterns. Preserve that. Add `@angular/cdk` only for the interaction primitives that are easy to get wrong, especially focus trapping and responsive shell behavior.

**Primary recommendation:** Plan Phase 4 around a new app shell, a dedicated dashboard store/page, reusable goal-summary UI components, and centralized accomplishment-band presentation on top of the existing goal list API.

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| Angular standalone app (`@angular/core`, `@angular/router`, `@angular/forms`) | 21.2.x | Shell routing, standalone pages, signals, reactive forms | Already the repo standard; Angular Router is the official navigation layer and supports nested outlets and optional view transitions |
| Sass via Angular build | Existing repo stack | Product-specific shell and dashboard styling | Current pages already use custom SCSS, CSS variables, and warm/glass styling that should be extended rather than replaced |
| OpenAPI-generated Angular client | Existing repo stack | Backend contract access for goals/categories | Keeps dashboard data backend-driven and consistent with detail/archive screens |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| `@angular/cdk` | 21.2.5 | Accessibility and interaction primitives | Use for `cdkTrapFocus` and shell/drawer interaction behavior instead of custom focus-trap code |
| Vitest via Angular unit-test builder | Repo: 4.0.8, latest verified 4.1.2 | Fast component and store testing | Use for dashboard store sorting/grouping tests and shell/dashboard component rendering tests |

### Alternatives Considered
| Instead of | Could Use | Tradeoff |
|------------|-----------|----------|
| Custom SCSS + CDK primitives | Angular Material | Faster accessible drawer/card primitives, but it will fight the existing Milestory visual language and likely trigger broad restyling in a phase that should focus on product UX |
| Reusing `GoalPlanningStore` for dashboard state | New `DashboardStore` | Reuse is faster short-term, but it keeps expanding a store that already owns create/edit/detail/archive concerns and will make shell/dashboard tasks harder to reason about |
| New backend dashboard endpoint immediately | Existing `listGoals('ACTIVE')` read model | A dedicated endpoint could become useful later, but the current contract is already rich enough for this phase and avoids backend scope unless the payload proves too heavy |

**Installation:**
```bash
npm install @angular/cdk@^21.2.5
```

**Version verification:** Verified on 2026-04-06 with `npm view`.
- `@angular/core`: latest `21.2.7`, published 2026-04-01
- `@angular/router`: latest `21.2.7`, published 2026-04-01
- `@angular/cdk`: latest `21.2.5`, published 2026-04-01
- `vitest`: latest `4.1.2`, published 2026-03-26

## Architecture Patterns

### Recommended Project Structure
```text
src/app/
├── shell/                      # Persistent app shell, sidebar, mobile drawer, route focus management
├── features/dashboard/         # Dashboard page, store, summary grouping, command-center view models
├── features/goals/active/      # Dedicated active-goals route under the shell
├── features/goals/shared/      # Existing goal API-facing state and reusable goal UI bits
├── shared/ui/goal-summary-card/ # Reusable dashboard/active-list card component
└── shared/ui/accomplishment-band/ # Shared 80/100/120 presentation for dashboard and detail
```

### Pattern 1: Shell-First Nested Routing
**What:** Replace the current root home page with an application shell component that owns sidebar/drawer navigation and hosts child routes for dashboard, active goals, archive, create/edit, and detail.
**When to use:** Immediately; this is the structural basis for PLAT-03 and the navigation-shell decisions.
**Example:**
```typescript
// Source: Angular Router docs + repo routing pattern
export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./shell/app-shell.component').then((m) => m.AppShellComponent),
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./features/dashboard/dashboard.page').then((m) => m.DashboardPage),
      },
      {
        path: 'goals',
        loadChildren: () =>
          import('./features/goals/goal.routes').then((m) => m.GOAL_ROUTES),
      },
    ],
  },
];
```

### Pattern 2: Dashboard View-Model Assembly From Backend-Owned Fields
**What:** Build a dedicated dashboard store that loads active goals and categories, then computes sections, callouts, and suggested-next-goal view models from backend fields already returned by the API.
**When to use:** For dashboard-only orchestration and presentation ordering.
**Example:**
```typescript
// Source: repo signal-store pattern + existing goal list contract
const ACCOMPLISHMENT_BANDS = [80, 100, 120] as const;

const sectionOrder = {
  BEHIND: 0,
  ON_PACE: 1,
  AHEAD: 2,
} as const;

readonly sections = computed(() => {
  const activeGoals = this.goals().filter((goal) => goal.status === 'ACTIVE');

  return ['BEHIND', 'ON_PACE', 'AHEAD'].map((paceStatus) => ({
    paceStatus,
    goals: activeGoals
      .filter((goal) => goal.paceStatus === paceStatus)
      .sort((left, right) => urgencyScore(right) - urgencyScore(left))
      .map((goal) => ({
        goal,
        reachedBands: ACCOMPLISHMENT_BANDS.filter((band) => goal.progressPercentOfTarget >= band),
      })),
  }));
});
```

### Pattern 3: Shared Goal Summary Components, Not Repeated Dashboard Markup
**What:** Extract reusable summary card and accomplishment-band components once both dashboard and active goals need the same medium-density presentation and drill-down behavior.
**When to use:** After shell routing is in place and dashboard IA is confirmed.
**Example:**
```typescript
// Source: Angular standalone component patterns already used in the repo
@Component({
  selector: 'app-goal-summary-card',
  imports: [RouterLink],
  templateUrl: './goal-summary-card.component.html',
})
export class GoalSummaryCardComponent {
  goal = input.required<ListGoals200ResponseInner>();
  categoryLabel = input.required<string>();
}
```

### Pattern 4: Route Focus and Motion As Progressive Enhancement
**What:** Treat route transitions and navigation focus as shell concerns. Update focus after navigation, and keep motion optional via Router view transitions.
**When to use:** In the app shell once the dashboard becomes the default landing surface.
**Example:**
```typescript
// Source: Angular accessibility guide + Router view transitions docs
provideRouter(routes, withViewTransitions());
```

### Anti-Patterns to Avoid
- **Frontend-owned pace logic:** Do not recompute status, summary, or pace text from raw numbers in templates or components. Use backend fields already returned by the API.
- **Dashboard inside `HomePage`:** Replace the placeholder home route instead of growing the Phase 1 page into a permanent dashboard.
- **One store for all goals UX:** Do not keep piling shell and dashboard concerns into `GoalPlanningStore`; it already owns create/edit/detail/archive workflows.
- **Mini detail pages on the dashboard:** The dashboard should stay a decision surface. Do not dump full checkpoint lists or full progress history into summary cards.
- **Custom drawer/focus management everywhere:** Use CDK a11y primitives for trapping and restoring focus instead of duplicating bespoke keyboard logic.

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Drawer/dialog focus trap | Manual Tab-looping for the mobile shell drawer | `cdkTrapFocus` from `@angular/cdk/a11y` | Keyboard loops and focus return are easy to get subtly wrong |
| Route motion plumbing | Custom `document.startViewTransition` orchestration | `withViewTransitions()` in Angular Router | Official router integration already handles navigation lifecycle and degrades gracefully |
| Active navigation semantics | Custom “selected” classes with no ARIA support | `RouterLinkActive` plus `ariaCurrentWhenActive` | Keeps active nav accessible to screen-reader users |
| Dashboard status math | Custom client-side pace/status formulas | Existing backend fields from `listGoals` and `getGoal` | Phase 3 explicitly established backend-owned progress interpretation |
| Repeated accomplishment threshold checks in templates | Inline `>= 80`, `>= 100`, `>= 120` logic everywhere | Shared accomplishment-band utility/component | Prevents drift between dashboard and detail |

**Key insight:** The tricky part of this phase is not HTML layout. It is preserving backend-owned domain interpretation while creating a shell and dashboard that feel polished, responsive, and accessible.

## Common Pitfalls

### Pitfall 1: Recreating domain logic in the dashboard
**What goes wrong:** The dashboard invents its own “risk,” “win,” or “priority” formulas directly in templates.
**Why it happens:** The list payload looks rich enough that it is tempting to keep deriving more meaning in the UI.
**How to avoid:** Derive only presentation ordering from backend-owned fields. If the UI starts needing new domain judgments, add them to a backend read model instead.
**Warning signs:** Multiple helpers that parse `expectedProgressValueToday`, `progressPercentOfTarget`, and `paceStatus` in different components.

### Pitfall 2: Turning the dashboard into a dense admin board
**What goes wrong:** Cards become overloaded with too much text, checkpoints, and controls.
**Why it happens:** The goal detail contract already contains lots of interesting fields.
**How to avoid:** Cap cards at status context, actual vs expected, next checkpoint, and strongest signal. Route to detail for action.
**Warning signs:** Users need to scroll inside cards or read full checkpoint narratives to decide where to click.

### Pitfall 3: Shell accessibility regressions on mobile
**What goes wrong:** The drawer opens visually but traps focus badly, loses focus on close, or leaves keyboard users at `body` after route changes.
**Why it happens:** Responsive shell work mixes layout, routing, and keyboard behavior.
**How to avoid:** Make focus restoration and post-navigation focus explicit shell responsibilities, and use CDK focus helpers instead of bespoke loops.
**Warning signs:** Manual `document.querySelector()` focus code scattered across components; failing keyboard-only navigation.

### Pitfall 4: Reusing `GoalPlanningStore` until it becomes unmaintainable
**What goes wrong:** Dashboard loading, active-goals grouping, and shell commands end up sharing a store with create/edit/detail/archive logic.
**Why it happens:** The store already exists and exposes the right API services.
**How to avoid:** Create a `DashboardStore` for dashboard-specific read models and keep `GoalPlanningStore` focused on planning/detail flows.
**Warning signs:** Dashboard tests require mocking progress overlay, preview payload, and archive state just to render a summary page.

### Pitfall 5: View transitions applied in the wrong CSS scope
**What goes wrong:** Motion either does nothing or behaves inconsistently.
**Why it happens:** View-transition pseudo-elements are styled from component-scoped CSS.
**How to avoid:** Put transition CSS in global styles only, and treat view transitions as progressive enhancement.
**Warning signs:** Route animations work in one place, disappear in another, or require disabling style encapsulation.

## Code Examples

Verified patterns from official sources:

### Shell Navigation With Active-Link Semantics
```html
<!-- Source: https://angular.dev/best-practices/a11y -->
<nav class="app-shell__nav" aria-label="Primary">
  <a routerLink="/" routerLinkActive="is-active" ariaCurrentWhenActive="page">Dashboard</a>
  <a routerLink="/goals/active" routerLinkActive="is-active" ariaCurrentWhenActive="page">
    Active goals
  </a>
  <a routerLink="/goals/archive" routerLinkActive="is-active" ariaCurrentWhenActive="page">
    Archive
  </a>
</nav>
```

### Focus Main Content After Navigation
```typescript
// Source: https://angular.dev/best-practices/a11y
this.router.events
  .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
  .subscribe(() => {
    this.mainHeading()?.nativeElement.focus();
  });
```

### Router View Transitions
```typescript
// Source: https://angular.dev/guide/routing/route-transition-animations
export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes, withViewTransitions())],
};
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Placeholder landing page as app root | Product shell with nested routes and dashboard default | Angular standalone-router era; current Angular docs center route-first composition | Phase 4 should replace `HomePage` as the shipped entry surface |
| Manual route swaps with no transition support | Router-level view transitions via `withViewTransitions()` | Present in Angular 21 docs; still developer preview as of 2026-04-06 | Use as progressive enhancement only, not as a critical navigation dependency |
| Fully bespoke focus traps | CDK `cdkTrapFocus` and Angular a11y guidance | Longstanding Angular/CDK recommendation, current a11y docs still point to CDK | Reduces shell/drawer keyboard bugs |
| DOM-coupled tests for every reusable widget | Harnesses only for broadly reused interactive components | Angular testing guide recommends harnesses mainly for shared interactive components | For this phase, plain component tests are enough unless you extract reusable dashboard widgets used in many places |

**Deprecated/outdated:**
- Expanding the Phase 1 home/status page into the permanent product surface: outdated for this phase because the context explicitly moves the shipped destination to the dashboard shell.
- Hand-authored browser view-transition orchestration: unnecessary for route changes when Angular Router already integrates the browser API.

## Open Questions

1. **Is the current `listGoals('ACTIVE')` payload too heavy for the dashboard?**
   - What we know: It already includes checkpoints and progress entries for each goal, which is functionally sufficient for the dashboard.
   - What's unclear: Whether the payload remains acceptably small once a user has many active goals.
   - Recommendation: Start with the existing contract. If dashboard render/load feels heavy in implementation, add a slim backend summary projection as a follow-up task, not as an up-front assumption.

2. **Should “Active goals” be a separate page or just a filtered dashboard section?**
   - What we know: The locked navigation decision explicitly calls for a sidebar item named `Active goals`.
   - What's unclear: How much extra information the active-goals route should show relative to dashboard cards.
   - Recommendation: Plan a separate `ActiveGoalsPage` that reuses the same summary-card component at a slightly denser list layout.

3. **Where should accomplishment bands live?**
   - What we know: Both dashboard and detail must show the same milestone bands, and the thresholds are presentation-driven.
   - What's unclear: Whether the team wants only badges or additional celebratory copy/motion at each band.
   - Recommendation: Centralize thresholds and tone in one UI presenter/component so copy and badge logic cannot drift between screens.

## Validation Architecture

### Test Framework
| Property | Value |
|----------|-------|
| Framework | Angular unit-test builder + Vitest (`ng test`, repo currently `^4.0.8`) |
| Config file | [`angular.json`](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/angular.json) and [`tsconfig.spec.json`](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/tsconfig.spec.json); no standalone `vitest.config.ts` |
| Quick run command | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/**/*.spec.ts` |
| Full suite command | `npm --prefix milestory-frontend test -- --watch=false` |

### Phase Requirements → Test Map
| Req ID | Behavior | Test Type | Automated Command | File Exists? |
|--------|----------|-----------|-------------------|-------------|
| PLAT-03 | Root UX is a real app shell and dashboard, not starter placeholder content | component/routing | `npm --prefix milestory-frontend test -- --watch=false --include src/app/shell/app-shell.component.spec.ts` | ❌ Wave 0 |
| DASH-01 | Dashboard command center summarizes active goal health | component + store | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/dashboard.page.spec.ts` | ❌ Wave 0 |
| DASH-02 | Goals are grouped and urgency-ordered so attention needs are obvious | store/unit | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/dashboard.store.spec.ts` | ❌ Wave 0 |
| DASH-03 | Accomplishment bands render consistently in dashboard and detail | component/unit | `npm --prefix milestory-frontend test -- --watch=false --include src/app/shared/ui/accomplishment-band/accomplishment-band.component.spec.ts` | ❌ Wave 0 |
| DASH-04 | Dashboard cards and callouts drill into goal detail clearly | component/routing | `npm --prefix milestory-frontend test -- --watch=false --include src/app/features/dashboard/dashboard.page.spec.ts --filter \"drill|navigate\"` | ❌ Wave 0 |

### Sampling Rate
- **Per task commit:** `npm --prefix milestory-frontend test -- --watch=false --include <touched-specs>`
- **Per wave merge:** `npm --prefix milestory-frontend test -- --watch=false`
- **Phase gate:** Full suite green before `/gsd:verify-work`

### Wave 0 Gaps
- [ ] `milestory-frontend/src/app/shell/app-shell.component.spec.ts` — shell nav, active-link semantics, mobile drawer behavior
- [ ] `milestory-frontend/src/app/features/dashboard/dashboard.store.spec.ts` — grouping, urgency ordering, strongest risk/win, suggested-next-goal
- [ ] `milestory-frontend/src/app/features/dashboard/dashboard.page.spec.ts` — command-center rendering and drill-down links
- [ ] `milestory-frontend/src/app/shared/ui/accomplishment-band/accomplishment-band.component.spec.ts` — threshold rendering for 80/100/120

## Sources

### Primary (HIGH confidence)
- Official Angular Routing overview: https://angular.dev/guide/routing
- Official Angular Router API (`ExtraOptions`, `withViewTransitions`): https://angular.dev/api/router/ExtraOptions
- Official Angular route transition animations guide: https://angular.dev/guide/routing/route-transition-animations
- Official Angular accessibility guide: https://angular.dev/best-practices/a11y
- Official Angular testing guide for harness scope: https://angular.dev/guide/testing/creating-component-harnesses
- NPM registry verification on 2026-04-06 via `npm view` for `@angular/core`, `@angular/router`, `@angular/cdk`, and `vitest`
- Repo sources:
  - [`milestory-frontend/package.json`](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/package.json)
  - [`milestory-frontend/src/app/app.routes.ts`](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/app.routes.ts)
  - [`milestory-frontend/src/app/features/goals/shared/goal-planning.store.ts`](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/shared/goal-planning.store.ts)
  - [`milestory-frontend/src/api/model/listGoals200ResponseInner.ts`](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/api/model/listGoals200ResponseInner.ts)
  - [`milestory-frontend/angular.json`](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/angular.json)

### Secondary (MEDIUM confidence)
- None

### Tertiary (LOW confidence)
- None

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH - grounded in the existing repo plus current official Angular docs and live NPM registry checks
- Architecture: HIGH - aligns directly with locked phase decisions, current repo structure, and official Angular routing/a11y guidance
- Pitfalls: MEDIUM - derived from repo shape and standard Angular UX failure modes, but not all have been exercised in this codebase yet

**Research date:** 2026-04-06
**Valid until:** 2026-05-06
