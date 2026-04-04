package com.ybritto.milestory.goal.application.model;

import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalProgressSnapshot;
import java.util.List;
import java.util.Objects;

public record GoalView(
        Goal goal,
        GoalProgressSnapshot progressSnapshot,
        List<GoalProgressEntry> progressEntries,
        List<GoalCheckpointView> checkpoints
) {

    public GoalView {
        Objects.requireNonNull(goal, "goal must not be null");
        Objects.requireNonNull(progressSnapshot, "progressSnapshot must not be null");
        progressEntries = List.copyOf(Objects.requireNonNull(progressEntries, "progressEntries must not be null"));
        checkpoints = List.copyOf(Objects.requireNonNull(checkpoints, "checkpoints must not be null"));
    }
}
