import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { GoalSummaryCardComponent } from '../../../shared/ui/goal-summary-card/goal-summary-card.component';
import { GoalPlanningStore } from '../shared/goal-planning.store';

@Component({
  selector: 'app-active-goals-page',
  imports: [GoalSummaryCardComponent],
  templateUrl: './active-goals.page.html',
  styleUrl: './active-goals.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ActiveGoalsPage {
  private readonly goalPlanningStore = inject(GoalPlanningStore);

  protected readonly goalCards = computed(() =>
    this.goalPlanningStore.goals().map((goal) => {
      const nextCheckpoint = goal.checkpoints[0] ?? null;

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

  constructor() {
    this.goalPlanningStore.loadGoals('ACTIVE');
  }
}
