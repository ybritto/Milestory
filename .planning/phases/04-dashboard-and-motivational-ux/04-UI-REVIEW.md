# Phase 04 - UI Review

**Audited:** 2026-04-06
**Baseline:** abstract 6-pillar standards
**Screenshots:** not captured (no dev server on localhost:3000, 5173, or 8080)

---

## Pillar Scores

| Pillar | Score | Key Finding |
|--------|-------|-------------|
| 1. Copywriting | 3/4 | Product copy is specific and on-brand, but key Phase 04 routes do not define empty or failure copy. |
| 2. Visuals | 3/4 | The shell and dashboard establish a clear focal hierarchy, though the active-goals route is visually thin for a primary destination. |
| 3. Color | 2/4 | The palette is coherent, but Phase 04 still leans on many hardcoded `rgb()` and hex values instead of tokenized color roles. |
| 4. Typography | 2/4 | The serif/sans pairing is strong, but the scale is broader than necessary and mixes several one-off sizes. |
| 5. Spacing | 3/4 | Layout spacing mostly follows the shared token scale, with only minor local rem-based deviations. |
| 6. Experience Design | 2/4 | Goal detail is well-covered, but dashboard and active-goals do not render loading, empty, or error states despite using async store data. |

**Overall: 15/24**

---

## Top 3 Priority Fixes

1. **Add explicit loading, empty, and error states to dashboard and active goals** - users currently get silent blank layouts when fetches fail or return no active goals - branch on `viewState` and `goals().length` in [dashboard.page.html](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.html) and [active-goals.page.html](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.html), with route-specific retry and zero-data copy.
2. **Replace Phase 04 hardcoded colors with semantic tokens** - hardcoded gradients and fills make theme maintenance and contrast tuning brittle - move repeated `rgb()` and hex values in [dashboard.page.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss), [app-shell.component.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss), [goal-summary-card.component.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.scss), and [goal-detail.page.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.scss) into shared custom properties in [styles.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/styles.scss).
3. **Tighten the typography scale across the shell, dashboard, and cards** - the current mix of `0.72rem`, `0.76rem`, `0.875rem`, `1.1rem`, `1.15rem`, `1.35rem`, `1.6rem`, and `1.8rem` weakens rhythm - consolidate headings, eyebrow text, and metric values onto fewer approved sizes in [app-shell.component.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss), [dashboard.page.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss), [goal-summary-card.component.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.scss), and [goal-detail.page.scss](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.scss).

---

## Detailed Findings

### Pillar 1: Copywriting (3/4)

- Copy is specific rather than generic across the main Phase 04 surfaces: the shell summary in [app-shell.component.html:6](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.html#L6), dashboard helper in [dashboard.page.html:7](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.html#L7), and active-goals summary in [active-goals.page.html:4](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.html#L4) all describe user value clearly.
- The accomplishment messaging is concise and reusable instead of repetitive, via [accomplishment-band.component.ts:61](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/accomplishment-band/accomplishment-band.component.ts#L61), [accomplishment-band.component.ts:65](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/accomplishment-band/accomplishment-band.component.ts#L65), and [accomplishment-band.component.ts:69](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/accomplishment-band/accomplishment-band.component.ts#L69).
- The CTA label `Open next` in [dashboard.page.html:31](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.html#L31) is the weakest string in the phase; it is understandable in context but less descriptive than the surrounding callout language.
- Goal detail has purposeful error and empty-state copy in [goal-detail.page.html:16](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html#L16) and [goal-detail.page.html:124](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html#L124), but the dashboard and active-goals routes do not provide comparable no-data or failure copy.

### Pillar 2: Visuals (3/4)

- The shell creates a clear information hierarchy with a branded left rail, prominent headline, and routed content frame in [app-shell.component.html:2](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.html#L2) and [app-shell.component.scss:6](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss#L6).
- The dashboard command center has a strong focal structure: one stat block, two callouts, and one suggested-next action in [dashboard.page.html:3](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.html#L3), reinforced by the 12-column layout in [dashboard.page.scss:18](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L18).
- Interaction affordances are mostly clear and accessible. The mobile drawer uses labeled buttons and focus trapping in [app-shell.component.html:36](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.html#L36) and [app-shell.component.html:63](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.html#L63), with matching logic in [app-shell.component.ts:45](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.ts#L45).
- The active-goals route is visually underdeveloped for a first-class destination. It is mostly a heading plus a vertical card stack in [active-goals.page.html:1](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.html#L1) and [active-goals.page.scss:34](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.scss#L34), so it lacks the stronger sectional framing present on the dashboard.

### Pillar 3: Color (2/4)

- The base palette is consistent and warm, with shared roles defined in [styles.scss:3](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/styles.scss#L3).
- Accent usage is frequent but still purposeful. A simple scan found `31` `var(--color-accent)` references across the frontend, largely on eyebrows, focus states, and primary actions.
- The main issue is token drift: Phase 04 surfaces still hardcode many presentation colors instead of consuming semantic variables. Examples include [app-shell.component.scss:18](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss#L18), [dashboard.page.scss:54](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L54), [goal-summary-card.component.scss:9](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.scss#L9), and [goal-detail.page.scss:59](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.scss#L59).
- Hex values like [app-shell.component.scss:106](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss#L106) and [dashboard.page.scss:110](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L110) show the same issue: foreground choices are embedded locally instead of defined as tokenized on-accent colors.

### Pillar 4: Typography (2/4)

- The font pairing is strong and intentional: serif display treatment for headlines and metrics in [styles.scss:1](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/styles.scss#L1), [dashboard.page.scss:80](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L80), and [goal-detail.page.scss:97](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.scss#L97), with sans body text globally in [styles.scss:36](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/styles.scss#L36).
- The scale is too wide for the amount of UI being shown. Across the audited Phase 04 surfaces, the code uses at least these distinct sizes: `0.72rem`, `0.76rem`, `0.875rem`, `1.1rem`, `1.15rem`, `1.35rem`, `1.6rem`, `1.8rem`, plus multiple `clamp(...)` headline sizes.
- Eyebrow styles are nearly but not fully standardized: compare [goal-summary-card.component.scss:36](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.scss#L36), [dashboard.page.scss:68](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L68), and [app-shell.component.scss:39](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss#L39).
- Weight usage is mostly restrained around `600` and `700`, which is good; the problem is size proliferation more than weight chaos.

### Pillar 5: Spacing (3/4)

- Spacing discipline is generally good. Shared tokens in [styles.scss:16](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/styles.scss#L16) are used heavily, with quick counts showing `var(--space-lg)` `42` times, `var(--space-md)` `41` times, and `var(--space-xl)` `31` times.
- Core layouts stay on the token scale in [app-shell.component.scss:14](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss#L14), [dashboard.page.scss:13](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L13), and [active-goals.page.scss:7](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.scss#L7).
- There are a few local rem-based values that break the rhythm slightly, such as [dashboard.page.scss:47](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L47), [dashboard.page.scss:64](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss#L64), [goal-summary-card.component.scss:23](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.scss#L23), and [goal-summary-card.component.scss:72](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.scss#L72).
- No arbitrary bracket-value utilities were found, which keeps the system maintainable.

### Pillar 6: Experience Design (2/4)

- Goal detail is the strongest interaction surface in the phase. It includes loading skeletons in [goal-detail.page.html:3](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html#L3), an error branch with retry in [goal-detail.page.html:13](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html#L13), an empty history state in [goal-detail.page.html:122](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html#L122), and archived-action protection in [goal-detail.page.html:64](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html#L64).
- The shell drawer interaction is solid and accessibility-aware, with escape handling and focus return in [app-shell.component.ts:55](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.ts#L55) and [app-shell.component.ts:73](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.ts#L73).
- The new dashboard and active-goals routes do async loads in [dashboard.page.ts:25](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.ts#L25) and [active-goals.page.ts:37](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.ts#L37) but never branch on `viewState` or empty results, so a failed request or zero-goal result produces a sparse or blank experience instead of a deliberate state.
- The shared store does emit error states for list loads in [goal-planning.store.ts:203](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/shared/goal-planning.store.ts#L203), but the two Phase 04 routes do not consume that signal. This is the main UX gap in the phase.
- Destructive archive is still one-click in [goal-detail.page.html:61](/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html#L61) with no confirmation step. That is survivable in a personal app, but it is still a notable interaction risk.

---

## Files Audited

- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/styles.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/app.routes.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.html`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shell/app-shell.component.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.html`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/dashboard/dashboard.page.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.html`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/active/active-goals.page.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.html`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/detail/goal-detail.page.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/features/goals/shared/goal-planning.store.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.ts`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.html`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/goal-summary-card/goal-summary-card.component.scss`
- `/Users/ybritto/dev/Personal/Milestory/milestory-frontend/src/app/shared/ui/accomplishment-band/accomplishment-band.component.ts`
