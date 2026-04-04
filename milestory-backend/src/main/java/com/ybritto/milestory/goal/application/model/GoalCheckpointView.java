package com.ybritto.milestory.goal.application.model;

import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import java.util.Objects;

public record GoalCheckpointView(
        GoalCheckpoint checkpoint,
        String progressContextLabel,
        String progressContextDetail
) {

    public GoalCheckpointView {
        Objects.requireNonNull(checkpoint, "checkpoint must not be null");
        Objects.requireNonNull(progressContextLabel, "progressContextLabel must not be null");
        Objects.requireNonNull(progressContextDetail, "progressContextDetail must not be null");
    }
}
