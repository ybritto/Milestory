import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  effect,
  inject,
  viewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { RecordGoalProgressEntryRequest } from '../../../../api/model/recordGoalProgressEntryRequest';
import { GoalPlanningStore } from '../shared/goal-planning.store';

@Component({
  selector: 'app-goal-detail-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './goal-detail.page.html',
  styleUrl: './goal-detail.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '(document:keydown)': 'handleDocumentKeydown($event)',
  },
})
export class GoalDetailPage {
  private readonly goalPlanningStore = inject(GoalPlanningStore);
  private readonly formBuilder = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly goalId = this.route.snapshot.paramMap.get('goalId');

  private readonly progressTrigger = viewChild<ElementRef<HTMLButtonElement>>('progressTrigger');
  private readonly overlayDialog = viewChild<ElementRef<HTMLElement>>('overlayDialog');
  private readonly progressDateInput = viewChild<ElementRef<HTMLInputElement>>('progressDateInput');

  private previousOverlayOpen = false;

  readonly goal = this.goalPlanningStore.goal;
  readonly categories = this.goalPlanningStore.goalCategories;
  readonly viewState = this.goalPlanningStore.viewState;
  readonly progressOverlayState = this.goalPlanningStore.progressOverlayState;
  readonly successMessage = this.goalPlanningStore.successMessage;
  readonly progressForm = this.formBuilder.group({
    entryDate: this.formBuilder.nonNullable.control(this.today(), [Validators.required]),
    progressValue: this.formBuilder.nonNullable.control(0, [Validators.required, Validators.min(0)]),
    note: this.formBuilder.nonNullable.control(''),
  });

  readonly categoryLabel = computed(() => {
    const goal = this.goal();

    if (!goal) {
      return '';
    }

    return (
      this.categories().find((category) => category.categoryId === goal.categoryId)?.displayName ??
      goal.categoryId
    );
  });
  readonly checkpointCards = computed(() => this.goal()?.checkpoints ?? []);
  readonly progressEntries = computed(() =>
    [...(this.goal()?.progressEntries ?? [])].sort((left, right) => {
      const entryDateOrder = right.entryDate.localeCompare(left.entryDate);
      return entryDateOrder !== 0 ? entryDateOrder : right.recordedAt.localeCompare(left.recordedAt);
    }),
  );
  readonly isLoading = computed(() => this.viewState().kind === 'loading' && !this.goal());
  readonly isError = computed(() => this.viewState().kind === 'error' && !this.goal());
  readonly isReady = computed(() => Boolean(this.goal()) && !this.isLoading() && !this.isError());
  readonly isArchived = computed(() => this.goal()?.status === 'ARCHIVED');
  readonly hasHistory = computed(() => this.progressEntries().length > 0);
  readonly isOverlayOpen = computed(() => {
    const kind = this.progressOverlayState().kind;
    return kind === 'open' || kind === 'submitting';
  });
  readonly paceTone = computed(() => {
    switch (this.goal()?.paceStatus) {
      case 'AHEAD':
        return 'ahead';
      case 'BEHIND':
        return 'behind';
      default:
        return 'on-pace';
    }
  });

  constructor() {
    this.goalPlanningStore.loadGoalCategories();

    if (this.goalId) {
      this.goalPlanningStore.loadGoal(this.goalId);
    }

    effect(() => {
      const overlayOpen = this.isOverlayOpen();

      if (overlayOpen && !this.previousOverlayOpen) {
        queueMicrotask(() => this.progressDateInput()?.nativeElement.focus());
      }

      if (!overlayOpen && this.previousOverlayOpen) {
        queueMicrotask(() => this.progressTrigger()?.nativeElement.focus());
      }

      this.previousOverlayOpen = overlayOpen;
    });
  }

  editGoal(): void {
    const goal = this.goal();
    if (!goal) {
      return;
    }

    void this.router.navigate(['/goals', goal.goalId, 'edit']);
  }

  archiveGoal(): void {
    const goal = this.goal();
    if (!goal) {
      return;
    }

    this.goalPlanningStore.archiveGoal(goal.goalId).subscribe({
      next: () => {
        void this.router.navigate(['/goals/archive']);
      },
    });
  }

  retryLoadGoal(): void {
    if (!this.goalId) {
      return;
    }

    this.goalPlanningStore.loadGoal(this.goalId);
  }

  openProgressEntry(): void {
    const goal = this.goal();
    if (!goal || this.isArchived()) {
      return;
    }

    this.progressForm.reset({
      entryDate: this.today(),
      progressValue: goal.currentProgressValue,
      note: '',
    });
    this.goalPlanningStore.openProgressOverlay();
  }

  closeProgressEntry(): void {
    this.goalPlanningStore.closeProgressOverlay();
  }

  submitProgress(): void {
    if (!this.goalId) {
      return;
    }

    if (this.progressForm.invalid) {
      this.progressForm.markAllAsTouched();
      return;
    }

    const value = this.progressForm.getRawValue();
    const request: RecordGoalProgressEntryRequest = {
      entryDate: value.entryDate,
      progressValue: Number(value.progressValue),
      note: value.note.trim() || undefined,
    };

    this.goalPlanningStore.recordProgress(this.goalId, request).subscribe({
      next: () => {
        this.progressForm.reset({
          entryDate: this.today(),
          progressValue: this.goal()?.currentProgressValue ?? 0,
          note: '',
        });
      },
    });
  }

  handleDialogKeydown(event: KeyboardEvent): void {
    if (event.key !== 'Tab') {
      return;
    }

    const dialog = this.overlayDialog()?.nativeElement;
    if (!dialog) {
      return;
    }

    const focusableElements = this.focusableElements(dialog);
    if (focusableElements.length === 0) {
      return;
    }

    const first = focusableElements[0];
    const last = focusableElements[focusableElements.length - 1];
    const activeElement = document.activeElement as HTMLElement | null;

    if (event.shiftKey && activeElement === first) {
      event.preventDefault();
      last.focus();
    } else if (!event.shiftKey && activeElement === last) {
      event.preventDefault();
      first.focus();
    }
  }

  handleDocumentKeydown(event: KeyboardEvent): void {
    if (event.key === 'Escape' && this.isOverlayOpen()) {
      event.preventDefault();
      this.closeProgressEntry();
    }
  }

  private focusableElements(container: HTMLElement): HTMLElement[] {
    return Array.from(
      container.querySelectorAll<HTMLElement>(
        'button, [href], input, textarea, select, [tabindex]:not([tabindex="-1"])',
      ),
    ).filter((element) => !element.hasAttribute('disabled') && element.tabIndex !== -1);
  }

  private today(): string {
    return new Date().toISOString().slice(0, 10);
  }
}
