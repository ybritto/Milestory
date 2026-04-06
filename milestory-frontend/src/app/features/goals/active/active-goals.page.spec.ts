import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { GoalPlanningStore } from '../shared/goal-planning.store';
import { GOAL_ROUTES } from '../goal.routes';
import { ActiveGoalsPage } from './active-goals.page';
import { ListGoalCategories200ResponseInner } from '../../../../api/model/listGoalCategories200ResponseInner';
import { ListGoals200ResponseInner } from '../../../../api/model/listGoals200ResponseInner';

describe('ActiveGoalsPage', () => {
  const goals = signal<ListGoals200ResponseInner[]>([createGoal()]);
  const categories = signal<ListGoalCategories200ResponseInner[]>([
    {
      categoryId: 'reading',
      key: 'reading',
      displayName: 'Reading',
      systemDefined: true,
    },
  ]);
  const viewState = signal<{ kind: string; message?: string }>({ kind: 'idle' });
  const loadGoals = vi.fn();
  const loadGoalCategories = vi.fn();

  beforeEach(() => {
    goals.set([createGoal()]);
    viewState.set({ kind: 'idle' });
    loadGoals.mockReset();
    loadGoalCategories.mockReset();
  });

  async function createComponent() {
    await TestBed.configureTestingModule({
      imports: [ActiveGoalsPage],
      providers: [
        provideRouter([]),
        {
          provide: GoalPlanningStore,
          useValue: {
            goals: goals.asReadonly(),
            goalCategories: categories.asReadonly(),
            viewState: viewState.asReadonly(),
            loadGoals,
            loadGoalCategories,
          },
        },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(ActiveGoalsPage);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    return fixture;
  }

  it('loads active goals on construction', async () => {
    await createComponent();

    expect(loadGoals).toHaveBeenCalledWith('ACTIVE');
  });

  it('renders the exact heading and reuses the shared goal summary cards', async () => {
    const fixture = await createComponent();

    expect(fixture.nativeElement.querySelector('h1')?.textContent).toContain('Active goals');
    expect(fixture.nativeElement.querySelectorAll('app-goal-summary-card')).toHaveLength(1);
  });

  it('keeps active and archive reserved ahead of the generic goal detail route', () => {
    const activeIndex = GOAL_ROUTES.findIndex((route) => route.path === 'active');
    const archiveIndex = GOAL_ROUTES.findIndex((route) => route.path === 'archive');
    const goalDetailIndex = GOAL_ROUTES.findIndex((route) => route.path === ':goalId');

    expect(activeIndex).toBeGreaterThan(-1);
    expect(archiveIndex).toBeGreaterThan(-1);
    expect(goalDetailIndex).toBeGreaterThan(activeIndex);
    expect(goalDetailIndex).toBeGreaterThan(archiveIndex);
  });

  it('links each active-goal card to the matching goal detail route', async () => {
    const fixture = await createComponent();
    const link = fixture.nativeElement.querySelector('.goal-summary-card__link') as HTMLAnchorElement | null;

    expect(link?.getAttribute('href')).toBe('/goals/goal-1');
  });

  it('renders a loading state while active goals are being requested', async () => {
    goals.set([]);
    viewState.set({ kind: 'loading' });

    const fixture = await createComponent();
    const text = fixture.nativeElement.textContent ?? '';

    expect(text).toContain('Loading active goals');
    expect(text).toContain('Milestory is gathering the goals that need today’s attention.');
  });

  it('renders an empty state when there are no active goals to show', async () => {
    goals.set([]);
    viewState.set({ kind: 'idle' });

    const fixture = await createComponent();
    const text = fixture.nativeElement.textContent ?? '';

    expect(text).toContain('No active goals yet');
    expect(text).toContain('Start a new goal to turn this route into your denser daily scan.');
    expect(text).toContain('Create a goal');
  });

  it('renders an error state with retry copy when active goals fail to load', async () => {
    goals.set([]);
    viewState.set({
      kind: 'error',
      message: 'Milestory could not load your active goals. Retry the page and confirm the backend is running.',
    });

    const fixture = await createComponent();
    const text = fixture.nativeElement.textContent ?? '';

    expect(text).toContain('Active goals unavailable');
    expect(text).toContain('Milestory could not load your active goals. Retry the page and confirm the backend is running.');
    expect(text).toContain('Retry active goals');
  });
});

function createGoal(overrides: Partial<ListGoals200ResponseInner> = {}): ListGoals200ResponseInner {
  return {
    goalId: 'goal-1',
    planningYear: 2026,
    title: 'Read 24 books',
    categoryId: 'reading',
    targetValue: 24,
    unit: 'books',
    motivation: 'Read more.',
    notes: 'Keep it consistent.',
    status: 'ACTIVE',
    suggestionBasis: 'CATEGORY_AWARE',
    customizedFromSuggestion: false,
    plannedPathSummary: 'Steady monthly progress.',
    currentProgressValue: 12,
    progressPercentOfTarget: 80,
    expectedProgressValueToday: 10,
    paceStatus: 'AHEAD',
    paceSummary: 'You are slightly ahead of plan.',
    paceDetail: 'Momentum is carrying this goal past today’s target.',
    progressEntries: [],
    checkpoints: [
      {
        checkpointId: 'checkpoint-1',
        sequenceNumber: 1,
        checkpointDate: '2026-06-30',
        targetValue: 12,
        note: 'Halfway there.',
        origin: 'SUGGESTED',
        progressContextLabel: 'Next checkpoint',
        progressContextDetail: 'Reach 12 books by June 30.',
      },
    ],
    ...overrides,
  };
}
