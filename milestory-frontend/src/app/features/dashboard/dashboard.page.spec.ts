import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal, WritableSignal } from '@angular/core';
import { By } from '@angular/platform-browser';
import { provideRouter, RouterLink } from '@angular/router';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { ListGoals200ResponseInner } from '../../../api/model/listGoals200ResponseInner';
import { GoalPlanningStore } from '../goals/shared/goal-planning.store';
import { DashboardPage } from './dashboard.page';

describe('DashboardPage', () => {
  let fixture: ComponentFixture<DashboardPage>;
  let goalsSignal: WritableSignal<ListGoals200ResponseInner[]>;
  let loadGoals: ReturnType<typeof vi.fn>;

  beforeEach(async () => {
    goalsSignal = signal([
      createGoal({
        goalId: 'behind-goal',
        title: 'Run 500 km',
        paceStatus: 'BEHIND',
        currentProgressValue: 110,
        expectedProgressValueToday: 220,
        progressPercentOfTarget: 22,
      }),
      createGoal({
        goalId: 'on-pace-goal',
        title: 'Save 10000 EUR',
        paceStatus: 'ON_PACE',
        currentProgressValue: 4200,
        expectedProgressValueToday: 4200,
        progressPercentOfTarget: 42,
        checkpoints: [createCheckpoint({ checkpointDate: '2026-05-15', targetValue: 4500 })],
      }),
      createGoal({
        goalId: 'ahead-goal',
        title: 'Read 36 books',
        paceStatus: 'AHEAD',
        currentProgressValue: 30,
        expectedProgressValueToday: 18,
        progressPercentOfTarget: 125,
      }),
    ]);
    loadGoals = vi.fn();

    await TestBed.configureTestingModule({
      imports: [DashboardPage],
      providers: [
        provideRouter([]),
        {
          provide: GoalPlanningStore,
          useValue: {
            goals: goalsSignal.asReadonly(),
            loadGoals,
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardPage);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  });

  it('calls loadGoals with ACTIVE when the page is constructed', () => {
    expect(loadGoals).toHaveBeenCalledWith('ACTIVE');
  });

  it('renders the command center labels and grouped dashboard headings', () => {
    const text = fixture.nativeElement.textContent ?? '';

    expect(text).toContain('Active goals');
    expect(text).toContain('Strongest risk');
    expect(text).toContain('Strongest win');
    expect(text).toContain('Open next');
    expect(text).toContain('Behind');
    expect(text).toContain('On pace');
    expect(text).toContain('Ahead');
  });

  it('only renders group sections that have items', async () => {
    goalsSignal.set([
      createGoal({
        goalId: 'behind-goal',
        title: 'Run 500 km',
        paceStatus: 'BEHIND',
        currentProgressValue: 110,
        expectedProgressValueToday: 220,
        progressPercentOfTarget: 22,
      }),
    ]);

    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const text = fixture.nativeElement.textContent ?? '';

    expect(text).toContain('Behind');
    expect(text).not.toContain('On pace');
    expect(text).not.toContain('Ahead');
  });

  it('routes strongest callouts and goal cards to goal detail paths', () => {
    const links = fixture.debugElement
      .queryAll(By.directive(RouterLink))
      .map((debugElement) => debugElement.injector.get(RouterLink).href);

    expect(links).toContain('/goals/behind-goal');
    expect(links).toContain('/goals/ahead-goal');
    expect(links).not.toContain('/goals/archive');
    expect(fixture.nativeElement.textContent ?? '').not.toContain('Add progress update');
  });
});

function createGoal(overrides: Partial<ListGoals200ResponseInner> = {}): ListGoals200ResponseInner {
  return {
    goalId: 'goal-id',
    planningYear: 2026,
    title: 'Default goal',
    categoryId: 'category-id',
    targetValue: 100,
    unit: 'units',
    motivation: 'Stay consistent.',
    notes: 'Keep going.',
    status: 'ACTIVE',
    suggestionBasis: 'CATEGORY_AWARE',
    customizedFromSuggestion: true,
    plannedPathSummary: 'Steady progress across the year.',
    currentProgressValue: 40,
    progressPercentOfTarget: 40,
    expectedProgressValueToday: 50,
    paceStatus: 'BEHIND',
    paceSummary: 'The goal is behind.',
    paceDetail: 'Progress is below the expected pace today.',
    archivedAt: undefined,
    progressEntries: [],
    checkpoints: [
      createCheckpoint({
        checkpointDate: '2026-06-30',
        targetValue: 50,
        progressContextLabel: 'Upcoming checkpoint',
      }),
    ],
    ...overrides,
  };
}

function createCheckpoint(
  overrides: Partial<ListGoals200ResponseInner['checkpoints'][number]> = {},
): ListGoals200ResponseInner['checkpoints'][number] {
  return {
    checkpointId: 'checkpoint-id',
    sequenceNumber: 1,
    checkpointDate: '2026-06-30',
    targetValue: 50,
    note: 'Keep the pace steady.',
    origin: 'SUGGESTED',
    originalCheckpointDate: undefined,
    originalTargetValue: undefined,
    progressContextLabel: 'Upcoming checkpoint',
    progressContextDetail: 'The next target is on the calendar.',
    ...overrides,
  };
}
