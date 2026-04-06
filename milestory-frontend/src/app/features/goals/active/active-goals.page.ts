import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { GoalSummaryCardComponent } from '../../../shared/ui/goal-summary-card/goal-summary-card.component';
import { GoalPlanningStore } from '../shared/goal-planning.store';

@Component({
  selector: 'app-active-goals-page',
  imports: [GoalSummaryCardComponent, RouterLink],
  templateUrl: './active-goals.page.html',
  styleUrl: './active-goals.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ActiveGoalsPage {
  private readonly goalPlanningStore = inject(GoalPlanningStore);

  protected readonly viewState = this.goalPlanningStore.viewState;
  protected readonly goalCards = computed(() =>
    this.goalPlanningStore.goals().map((goal) => {
      const nextCheckpoint =
        [...goal.checkpoints]
          .sort((left, right) => left.checkpointDate.localeCompare(right.checkpointDate))
          .find((checkpoint) => checkpoint.targetValue > goal.currentProgressValue) ??
        goal.checkpoints[0] ??
        null;

      return {
        goalId: goal.goalId,
        title: goal.title,
        unit: goal.unit,
        paceStatus: goal.paceStatus,
        paceSummary: goal.paceSummary,
        paceDetail: goal.paceDetail,
        currentProgressValue: goal.currentProgressValue,
        expectedProgressValueToday: goal.expectedProgressValueToday,
        progressPercentOfTarget: goal.progressPercentOfTarget,
        nextCheckpointDate: nextCheckpoint?.checkpointDate ?? null,
        nextCheckpointLabel: nextCheckpoint?.progressContextLabel ?? null,
        strongestSignal: nextCheckpoint?.progressContextDetail ?? goal.paceDetail,
      };
    }),
  );
  protected readonly isLoading = computed(
    () => this.viewState().kind === 'loading' && this.goalPlanningStore.goals().length === 0,
  );
  protected readonly isError = computed(
    () => this.viewState().kind === 'error' && this.goalPlanningStore.goals().length === 0,
  );
  protected readonly isEmpty = computed(
    () => this.viewState().kind !== 'loading' && this.goalPlanningStore.goals().length === 0 && !this.isError(),
  );
  protected readonly errorMessage = computed(() => {
    const state = this.viewState();
    return state.kind === 'error' ? state.message : '';
  });

  constructor() {
    this.goalPlanningStore.loadGoals('ACTIVE');
  }

  protected retryActiveGoals(): void {
    this.goalPlanningStore.loadGoals('ACTIVE');
  }
}
