import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal, WritableSignal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { ListGoalCategories200ResponseInner } from '../../../../api/model/listGoalCategories200ResponseInner';
import { ListGoals200ResponseInner } from '../../../../api/model/listGoals200ResponseInner';
import { ListGoals200ResponseInnerProgressEntriesInner } from '../../../../api/model/listGoals200ResponseInnerProgressEntriesInner';
import { GoalPlanningStore, ProgressOverlayState } from '../shared/goal-planning.store';
import { GoalDetailPage } from './goal-detail.page';

describe('GoalDetailPage', () => {
  let fixture: ComponentFixture<GoalDetailPage>;
  let goalSignal: WritableSignal<ListGoals200ResponseInner | null>;
  let categoriesSignal: WritableSignal<ListGoalCategories200ResponseInner[]>;
  let viewStateSignal: WritableSignal<{ kind: string; message?: string }>;
  let progressOverlaySignal: WritableSignal<ProgressOverlayState>;
  let successMessageSignal: WritableSignal<string | null>;
  let loadGoal: ReturnType<typeof vi.fn>;
  let loadGoalCategories: ReturnType<typeof vi.fn>;
  let archiveGoal: ReturnType<typeof vi.fn>;
  let recordProgress: ReturnType<typeof vi.fn>;
  let openProgressOverlay: ReturnType<typeof vi.fn>;
  let closeProgressOverlay: ReturnType<typeof vi.fn>;

  async function createComponent(): Promise<void> {
    await TestBed.configureTestingModule({
      imports: [GoalDetailPage],
      providers: [
        {
          provide: GoalPlanningStore,
          useValue: {
            goal: goalSignal.asReadonly(),
            goalCategories: categoriesSignal.asReadonly(),
            viewState: viewStateSignal.asReadonly(),
            progressOverlayState: progressOverlaySignal.asReadonly(),
            successMessage: successMessageSignal.asReadonly(),
            loadGoal,
            loadGoalCategories,
            archiveGoal,
            recordProgress,
            openProgressOverlay,
            closeProgressOverlay,
          },
        },
        {
          provide: Router,
          useValue: {
            navigate: vi.fn(),
          },
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: vi.fn(() => 'goal-id'),
              },
            },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(GoalDetailPage);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  }

  beforeEach(async () => {
    goalSignal = signal(createGoal());
    categoriesSignal = signal(createCategories());
    viewStateSignal = signal({ kind: 'idle' });
    progressOverlaySignal = signal<ProgressOverlayState>({ kind: 'closed' });
    successMessageSignal = signal<string | null>(null);
    loadGoal = vi.fn();
    loadGoalCategories = vi.fn();
    archiveGoal = vi.fn(() => of({ archivedAt: '2026-04-04T10:00:00Z' }));
    recordProgress = vi.fn(() => of(createProgressEntry()));
    openProgressOverlay = vi.fn(() => progressOverlaySignal.set({ kind: 'open' }));
    closeProgressOverlay = vi.fn(() => progressOverlaySignal.set({ kind: 'closed' }));

    await createComponent();
  });

  it('renders the sections in status, comparison, history, checkpoint order', () => {
    const hero = fixture.nativeElement.querySelector('.goal-status-hero');
    const comparison = fixture.nativeElement.querySelector('.goal-comparison-strip');
    const history = fixture.nativeElement.querySelector('.goal-progress-history');
    const checkpoints = fixture.nativeElement.querySelector('.goal-checkpoint-section');

    expect(hero).not.toBeNull();
    expect(comparison).not.toBeNull();
    expect(history).not.toBeNull();
    expect(checkpoints).not.toBeNull();
    expect(hero.compareDocumentPosition(comparison)).toBe(Node.DOCUMENT_POSITION_FOLLOWING);
    expect(comparison.compareDocumentPosition(history)).toBe(Node.DOCUMENT_POSITION_FOLLOWING);
    expect(history.compareDocumentPosition(checkpoints)).toBe(Node.DOCUMENT_POSITION_FOLLOWING);
  });

  it('shows the active-goal CTA and comparison copy', () => {
    expect(textContent()).toContain('Add progress update');
    expect(textContent()).toContain('Actual so far');
    expect(textContent()).toContain('Expected by today');
  });

  it('renders the accomplishment band in the status hero once progress reaches the 80 percent threshold', async () => {
    goalSignal.set(createGoal({ progressPercentOfTarget: 80 }));
    fixture.detectChanges();
    await fixture.whenStable();

    const band = fixture.nativeElement.querySelector('app-accomplishment-band');

    expect(band).not.toBeNull();
    expect(band.closest('.goal-status-hero')).not.toBeNull();
    expect(textContent()).toContain('80% milestone');
  });

  it('reuses the shared accomplishment-band copy for the 80, 100, and 120 percent thresholds', async () => {
    goalSignal.set(createGoal({ progressPercentOfTarget: 80 }));
    fixture.detectChanges();
    await fixture.whenStable();
    expect(textContent()).toContain('80% milestone');

    goalSignal.set(createGoal({ progressPercentOfTarget: 100 }));
    fixture.detectChanges();
    await fixture.whenStable();
    expect(textContent()).toContain('Target reached');

    goalSignal.set(createGoal({ progressPercentOfTarget: 120 }));
    fixture.detectChanges();
    await fixture.whenStable();
    expect(textContent()).toContain('120% stretch');
  });

  it('hides the add-progress CTA for archived goals and shows the read-only note', async () => {
    goalSignal.set(createGoal({ status: 'ARCHIVED' }));
    fixture.detectChanges();
    await fixture.whenStable();

    expect(textContent()).not.toContain('Add progress update');
    expect(textContent()).toContain('This goal is archived. Its plan and progress history stay visible, but new updates are disabled.');
  });

  it('renders loading skeletons for the hero and comparison strip', async () => {
    goalSignal.set(null);
    viewStateSignal.set({ kind: 'loading' });
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.querySelector('.goal-status-hero__skeleton-label')).not.toBeNull();
    expect(fixture.nativeElement.querySelector('.goal-status-hero__skeleton-headline')).not.toBeNull();
    expect(fixture.nativeElement.querySelector('.goal-status-hero__skeleton-summary')).not.toBeNull();
    expect(fixture.nativeElement.querySelector('.goal-comparison-strip__skeleton')).not.toBeNull();
  });

  it('renders the exact retry copy in the error state', async () => {
    goalSignal.set(null);
    viewStateSignal.set({
      kind: 'error',
      message:
        'Milestory could not load the current progress status for this goal. Retry the page, and if the issue persists confirm the backend is running and the goal still exists.',
    });
    fixture.detectChanges();
    await fixture.whenStable();

    expect(textContent()).toContain(
      'Milestory could not load the current progress status for this goal. Retry the page, and if the issue persists confirm the backend is running and the goal still exists.',
    );
    expect(textContent()).toContain('Retry page');
  });

  it('renders the exact empty-state heading and helper body', async () => {
    goalSignal.set(createGoal({ progressEntries: [] }));
    fixture.detectChanges();
    await fixture.whenStable();

    expect(textContent()).toContain('No progress logged yet');
    expect(textContent()).toContain(
      'Add your first update to see how this goal is tracking against the plan for today.',
    );
  });

  it('opens the overlay, traps focus, closes on escape, and returns focus to the trigger', async () => {
    const trigger = fixture.nativeElement.querySelector('.goal-status-hero__cta');

    trigger.click();
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const dialog = fixture.nativeElement.querySelector('[role="dialog"]') as HTMLElement;
    const dateInput = fixture.nativeElement.querySelector('#progress-entry-date') as HTMLInputElement;
    const actionButtons = fixture.nativeElement.querySelectorAll(
      '.goal-progress-overlay__actions .goal-button',
    ) as NodeListOf<HTMLButtonElement>;
    const lastFocusableButton = actionButtons[actionButtons.length - 1];

    expect(dialog).not.toBeNull();
    expect(document.activeElement).toBe(dateInput);

    lastFocusableButton.focus();
    dialog.dispatchEvent(new KeyboardEvent('keydown', { key: 'Tab' }));
    fixture.detectChanges();
    expect(document.activeElement).toBe(dateInput);

    document.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.querySelector('[role="dialog"]')).toBeNull();
    expect(document.activeElement).toBe(trigger);
  });
});

function createCategories(): ListGoalCategories200ResponseInner[] {
  return [
    {
      categoryId: 'starter-category-id',
      key: 'reading',
      displayName: 'Reading',
      systemDefined: true,
    },
  ];
}

function createGoal(
  overrides: Partial<ListGoals200ResponseInner> = {},
): ListGoals200ResponseInner {
  return {
    goalId: 'goal-id',
    planningYear: 2026,
    title: 'Read 24 books',
    categoryId: 'starter-category-id',
    targetValue: 24,
    unit: 'books',
    motivation: 'Build a steady reading habit.',
    notes: 'Mix novels and nonfiction.',
    status: 'ACTIVE',
    suggestionBasis: 'CATEGORY_AWARE',
    customizedFromSuggestion: true,
    plannedPathSummary: 'Monthly milestones carry the target across the year.',
    currentProgressValue: 6,
    progressPercentOfTarget: 25,
    expectedProgressValueToday: 6,
    paceStatus: 'ON_PACE',
    paceSummary: "You're right where this goal expected you to be today.",
    paceDetail: 'You are matching the planned pace for today.',
    progressEntries: [
      createProgressEntry({
        progressEntryId: 'progress-entry-2',
        entryDate: '2026-04-04',
        progressValue: 6,
        note: 'Finished another book.',
      }),
      createProgressEntry({
        progressEntryId: 'progress-entry-1',
        entryDate: '2026-03-29',
        progressValue: 5,
        note: 'Correction after removing a duplicate.',
        entryType: 'CORRECTION',
      }),
    ],
    checkpoints: [
      {
        checkpointId: 'checkpoint-1',
        sequenceNumber: 1,
        checkpointDate: '2026-01-31',
        targetValue: 2,
        note: 'Start with a manageable pace.',
        origin: 'SUGGESTED',
        progressContextLabel: 'Latest checkpoint passed',
        progressContextDetail: 'You cleared the first checkpoint and are carrying momentum forward.',
      },
      {
        checkpointId: 'checkpoint-2',
        sequenceNumber: 2,
        checkpointDate: '2026-06-30',
        targetValue: 12,
        note: 'Hold steady through midyear.',
        origin: 'SUGGESTED',
        progressContextLabel: 'Upcoming checkpoint',
        progressContextDetail: 'The next target is 12 books by June 30.',
      },
    ],
    ...overrides,
  };
}

function createProgressEntry(
  overrides: Partial<ListGoals200ResponseInnerProgressEntriesInner> = {},
): ListGoals200ResponseInnerProgressEntriesInner {
  return {
    progressEntryId: 'progress-entry-id',
    entryDate: '2026-04-04',
    progressValue: 6,
    note: 'Finished another book.',
    entryType: 'NORMAL',
    recordedAt: '2026-04-04T10:00:00Z',
    ...overrides,
  };
}

function textContent(): string {
  return document.body.textContent ?? '';
}
