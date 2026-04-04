package com.ybritto.milestory.goal.application.usecase;

import com.ybritto.milestory.goal.application.model.GoalCheckpointView;
import com.ybritto.milestory.goal.application.model.GoalView;
import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalProgressEntryPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalProgressSnapshot;
import com.ybritto.milestory.goal.domain.GoalProgressStatusService;
import com.ybritto.milestory.goal.domain.GoalStatus;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ListGoalsUseCase {

    private final GoalPersistencePort goalPersistencePort;
    private final GoalProgressEntryPersistencePort goalProgressEntryPersistencePort;
    private final GoalProgressStatusService goalProgressStatusService;
    private final Clock clock;

    public ListGoalsUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalProgressEntryPersistencePort goalProgressEntryPersistencePort,
            GoalProgressStatusService goalProgressStatusService,
            Clock clock
    ) {
        this.goalPersistencePort = Objects.requireNonNull(goalPersistencePort, "goalPersistencePort must not be null");
        this.goalProgressEntryPersistencePort = Objects.requireNonNull(
                goalProgressEntryPersistencePort,
                "goalProgressEntryPersistencePort must not be null"
        );
        this.goalProgressStatusService = Objects.requireNonNull(
                goalProgressStatusService,
                "goalProgressStatusService must not be null"
        );
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public List<GoalView> listGoals(GoalStatus status) {
        GoalStatus effectiveStatus = status == null ? GoalStatus.ACTIVE : status;
        return goalPersistencePort.listGoals(effectiveStatus).stream()
                .map(this::toGoalView)
                .toList();
    }

    private GoalView toGoalView(Goal goal) {
        List<GoalProgressEntry> ascendingEntries = goalProgressEntryPersistencePort.findByGoalId(goal.goalId());
        GoalProgressSnapshot snapshot = goalProgressStatusService.calculate(
                goal.targetValue(),
                goal.checkpoints(),
                ascendingEntries,
                LocalDate.now(clock)
        );
        List<GoalProgressEntry> newestFirstEntries = ascendingEntries.stream()
                .sorted(Comparator.comparing(GoalProgressEntry::entryDate)
                        .thenComparing(GoalProgressEntry::recordedAt)
                        .reversed())
                .toList();
        return new GoalView(goal, snapshot, newestFirstEntries, mapCheckpointViews(goal, snapshot));
    }

    private List<GoalCheckpointView> mapCheckpointViews(Goal goal, GoalProgressSnapshot snapshot) {
        List<GoalCheckpoint> checkpoints = goal.checkpoints();
        LocalDate today = LocalDate.now(clock);
        int expectedCheckpointIndex = expectedCheckpointIndex(checkpoints, today);

        return checkpoints.stream()
                .map(checkpoint -> {
                    String progressContextLabel = checkpointLabel(checkpoint, checkpoints, expectedCheckpointIndex, today);
                    return new GoalCheckpointView(
                            checkpoint,
                            progressContextLabel,
                            checkpointDetail(progressContextLabel, checkpoint, goal, snapshot)
                    );
                })
                .toList();
    }

    private int expectedCheckpointIndex(List<GoalCheckpoint> checkpoints, LocalDate today) {
        for (int index = 0; index < checkpoints.size(); index++) {
            if (!checkpoints.get(index).checkpointDate().isBefore(today)) {
                return index;
            }
        }
        return checkpoints.size() - 1;
    }

    private String checkpointLabel(
            GoalCheckpoint checkpoint,
            List<GoalCheckpoint> checkpoints,
            int expectedCheckpointIndex,
            LocalDate today
    ) {
        GoalCheckpoint expectedCheckpoint = checkpoints.get(expectedCheckpointIndex);
        if (checkpoint.checkpointId().equals(expectedCheckpoint.checkpointId())) {
            return "Expected by now";
        }
        return checkpoint.checkpointDate().isBefore(today) ? "Latest checkpoint passed" : "Upcoming checkpoint";
    }

    private String checkpointDetail(
            String progressContextLabel,
            GoalCheckpoint checkpoint,
            Goal goal,
            GoalProgressSnapshot snapshot
    ) {
        String actualValue = displayValue(snapshot.currentProgressValue());
        String expectedValue = displayValue(snapshot.expectedProgressValueToday());
        String checkpointValue = displayValue(checkpoint.targetValue());
        return switch (progressContextLabel) {
            case "Expected by now" -> "By today, the plan expects %s %s on the way to %s %s by %s."
                    .formatted(expectedValue, goal.unit(), checkpointValue, goal.unit(), checkpoint.checkpointDate());
            case "Upcoming checkpoint" -> "This checkpoint is still ahead. Actual so far is %s %s, with %s %s planned by %s."
                    .formatted(actualValue, goal.unit(), checkpointValue, goal.unit(), checkpoint.checkpointDate());
            default -> "This checkpoint date has passed. Actual so far is %s %s against its %s %s target."
                    .formatted(actualValue, goal.unit(), checkpointValue, goal.unit());
        };
    }

    private String displayValue(java.math.BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}
