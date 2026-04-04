package com.ybritto.milestory.goal.in.controller;

import com.ybritto.milestory.goal.application.model.GoalCheckpointView;
import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.application.model.GoalPlanPreview;
import com.ybritto.milestory.goal.application.model.GoalView;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalStatus;
import com.ybritto.milestory.generated.model.ArchiveGoalResponse;
import com.ybritto.milestory.generated.model.ArchiveGoalResponseStatus;
import com.ybritto.milestory.generated.model.GoalCategoryResponse;
import com.ybritto.milestory.generated.model.GoalCheckpointResponse;
import com.ybritto.milestory.generated.model.GoalCheckpointResponseOrigin;
import com.ybritto.milestory.generated.model.GoalPaceStatus;
import com.ybritto.milestory.generated.model.GoalPlanPreviewResponse;
import com.ybritto.milestory.generated.model.GoalPlanPreviewResponseSuggestionBasis;
import com.ybritto.milestory.generated.model.GoalProgressEntryResponse;
import com.ybritto.milestory.generated.model.GoalProgressEntryResponseEntryType;
import com.ybritto.milestory.generated.model.GoalResponse;
import com.ybritto.milestory.generated.model.GoalResponseStatus;
import com.ybritto.milestory.generated.model.GoalResponseSuggestionBasis;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoalResponseMapper {

    @Mapping(target = "year", expression = "java(preview.planningYear().getValue())")
    @Mapping(target = "planningYear", expression = "java(preview.planningYear().getValue())")
    @Mapping(target = "title", expression = "java(preview.title())")
    @Mapping(target = "categoryId", expression = "java(preview.categoryId().toString())")
    @Mapping(target = "targetValue", expression = "java(toDouble(preview.targetValue()))")
    @Mapping(target = "unit", expression = "java(preview.unit())")
    @Mapping(target = "motivation", expression = "java(preview.motivation())")
    @Mapping(target = "notes", expression = "java(preview.notes())")
    @Mapping(target = "suggestionBasis", expression = "java(toPreviewSuggestionBasis(preview.suggestionBasis()))")
    @Mapping(target = "customizedFromSuggestion", expression = "java(preview.customizedFromSuggestion())")
    @Mapping(target = "plannedPathSummary", expression = "java(preview.plannedPathSummary())")
    GoalPlanPreviewResponse mapPreview(GoalPlanPreview preview);

    @Mapping(target = "goalId", expression = "java(goal.goalId().toString())")
    @Mapping(target = "planningYear", expression = "java(goal.planningYear().getValue())")
    @Mapping(target = "title", expression = "java(goal.title())")
    @Mapping(target = "categoryId", expression = "java(goal.categoryId().toString())")
    @Mapping(target = "targetValue", expression = "java(toDouble(goal.targetValue()))")
    @Mapping(target = "unit", expression = "java(goal.unit())")
    @Mapping(target = "motivation", expression = "java(goal.motivation())")
    @Mapping(target = "notes", expression = "java(goal.notes())")
    @Mapping(target = "status", expression = "java(toResponseStatus(goal.status()))")
    @Mapping(target = "suggestionBasis", expression = "java(toGoalResponseSuggestionBasis(goal.suggestionBasis()))")
    @Mapping(target = "customizedFromSuggestion", expression = "java(goal.customizedFromSuggestion())")
    @Mapping(target = "plannedPathSummary", expression = "java(plannedPathSummary(goal))")
    @Mapping(target = "archivedAt", expression = "java(toOffsetDateTime(goal.archivedAt()))")
    @Mapping(target = "checkpoints", expression = "java(mapCheckpoints(goal.checkpoints()))")
    GoalResponse mapGoal(Goal goal);

    default GoalResponse mapGoal(GoalView goalView) {
        Goal goal = goalView.goal();
        return new GoalResponse(
                goal.goalId().toString(),
                goal.planningYear().getValue(),
                goal.title(),
                goal.categoryId().toString(),
                toDouble(goal.targetValue()),
                goal.unit(),
                goal.motivation(),
                goal.notes(),
                toResponseStatus(goal.status()),
                toGoalResponseSuggestionBasis(goal.suggestionBasis()),
                goal.customizedFromSuggestion(),
                plannedPathSummary(goal),
                toDouble(goalView.progressSnapshot().currentProgressValue()),
                toDouble(goalView.progressSnapshot().progressPercentOfTarget()),
                toDouble(goalView.progressSnapshot().expectedProgressValueToday()),
                GoalPaceStatus.valueOf(goalView.progressSnapshot().paceStatus().name()),
                goalView.progressSnapshot().paceSummary(),
                goalView.progressSnapshot().paceDetail(),
                toOffsetDateTime(goal.archivedAt()),
                mapProgressEntries(goalView.progressEntries()),
                mapCheckpointViews(goalView.checkpoints())
        );
    }

    @Mapping(target = "goalId", expression = "java(goal.goalId().toString())")
    @Mapping(target = "status", expression = "java(ArchiveGoalResponseStatus.ARCHIVED)")
    @Mapping(target = "archivedAt", expression = "java(toOffsetDateTime(goal.archivedAt()))")
    ArchiveGoalResponse mapArchive(Goal goal);

    @Mapping(target = "checkpointId", expression = "java(checkpoint.checkpointId().toString())")
    @Mapping(target = "sequenceNumber", expression = "java(checkpoint.sequenceNumber())")
    @Mapping(target = "checkpointDate", expression = "java(checkpoint.checkpointDate())")
    @Mapping(target = "targetValue", expression = "java(toDouble(checkpoint.targetValue()))")
    @Mapping(target = "note", expression = "java(checkpoint.note())")
    @Mapping(target = "origin", expression = "java(toCheckpointOrigin(checkpoint.origin()))")
    @Mapping(target = "originalCheckpointDate", expression = "java(checkpoint.originalCheckpointDate())")
    @Mapping(target = "originalTargetValue", expression = "java(toDouble(checkpoint.originalTargetValue()))")
    GoalCheckpointResponse mapCheckpoint(GoalCheckpoint checkpoint);

    default GoalProgressEntryResponse mapProgressEntry(GoalProgressEntry entry) {
        return new GoalProgressEntryResponse(
                entry.progressEntryId().toString(),
                entry.entryDate(),
                toDouble(entry.progressValue()),
                entry.note(),
                GoalProgressEntryResponseEntryType.valueOf(entry.entryType().name()),
                toOffsetDateTime(entry.recordedAt())
        );
    }

    default GoalCheckpointResponse mapCheckpointView(GoalCheckpointView checkpointView) {
        GoalCheckpoint checkpoint = checkpointView.checkpoint();
        return new GoalCheckpointResponse(
                checkpoint.checkpointId().toString(),
                checkpoint.sequenceNumber(),
                checkpoint.checkpointDate(),
                toDouble(checkpoint.targetValue()),
                checkpoint.note(),
                toCheckpointOrigin(checkpoint.origin()),
                checkpoint.originalCheckpointDate(),
                toDouble(checkpoint.originalTargetValue()),
                checkpointView.progressContextLabel(),
                checkpointView.progressContextDetail()
        );
    }

    default GoalCategoryResponse mapCategory(GoalCategory category) {
        return new GoalCategoryResponse(
                category.categoryId().toString(),
                category.key(),
                category.displayName(),
                category.systemDefined()
        );
    }

    default GoalCategoryResponse[] mapCategories(List<GoalCategory> categories) {
        return categories == null ? new GoalCategoryResponse[0] : categories.stream().map(this::mapCategory).toArray(GoalCategoryResponse[]::new);
    }

    default GoalResponse[] mapGoals(List<GoalView> goals) {
        return goals == null ? new GoalResponse[0] : goals.stream().map(this::mapGoal).toArray(GoalResponse[]::new);
    }

    default GoalCheckpointResponse[] mapCheckpoints(List<GoalCheckpoint> checkpoints) {
        return checkpoints == null ? new GoalCheckpointResponse[0] : checkpoints.stream().map(this::mapCheckpoint).toArray(GoalCheckpointResponse[]::new);
    }

    default GoalProgressEntryResponse[] mapProgressEntries(List<GoalProgressEntry> progressEntries) {
        return progressEntries == null
                ? new GoalProgressEntryResponse[0]
                : progressEntries.stream().map(this::mapProgressEntry).toArray(GoalProgressEntryResponse[]::new);
    }

    default GoalCheckpointResponse[] mapCheckpointViews(List<GoalCheckpointView> checkpoints) {
        return checkpoints == null ? new GoalCheckpointResponse[0] : checkpoints.stream()
                .map(this::mapCheckpointView)
                .toArray(GoalCheckpointResponse[]::new);
    }

    default GoalPlanPreviewResponseSuggestionBasis toPreviewSuggestionBasis(GoalPlanPreview.SuggestionBasis basis) {
        return basis == null ? null : GoalPlanPreviewResponseSuggestionBasis.valueOf(basis.name());
    }

    default GoalResponseSuggestionBasis toGoalResponseSuggestionBasis(String basis) {
        return basis == null ? null : GoalResponseSuggestionBasis.valueOf(basis);
    }

    default GoalResponseStatus toResponseStatus(GoalStatus status) {
        return status == null ? null : GoalResponseStatus.valueOf(status.name());
    }

    default GoalCheckpointResponseOrigin toCheckpointOrigin(String origin) {
        return origin == null ? null : GoalCheckpointResponseOrigin.valueOf(origin);
    }

    default Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    default OffsetDateTime toOffsetDateTime(java.time.Instant instant) {
        return instant == null ? null : OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    default String plannedPathSummary(Goal goal) {
        if (goal.suggestionBasis() != null && goal.suggestionBasis().equals(GoalPlanPreview.SuggestionBasis.CATEGORY_AWARE.name())) {
            return "Even monthly milestones tailored to the selected category; refine as needed.";
        }
        return "Even monthly milestones from January through December; refine the generic fallback to match your real pace.";
    }
}
