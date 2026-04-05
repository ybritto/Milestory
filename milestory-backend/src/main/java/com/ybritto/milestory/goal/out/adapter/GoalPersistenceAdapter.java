package com.ybritto.milestory.goal.out.adapter;

import com.ybritto.milestory.goal.application.model.GoalCategory;
import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalProgressEntryPersistencePort;
import com.ybritto.milestory.goal.application.usecase.GoalCategoryNotFoundException;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalCheckpoint;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalStatus;
import java.time.Clock;
import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ybritto.milestory.goal.out.entity.GoalCategoryJpaEntity;
import com.ybritto.milestory.goal.out.entity.GoalCheckpointJpaEntity;
import com.ybritto.milestory.goal.out.entity.GoalJpaEntity;
import com.ybritto.milestory.goal.out.entity.GoalProgressEntryJpaEntity;
import com.ybritto.milestory.goal.out.repository.GoalCategoryJpaRepository;
import com.ybritto.milestory.goal.out.repository.GoalJpaRepository;
import com.ybritto.milestory.goal.out.repository.GoalProgressEntryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoalPersistenceAdapter implements GoalPersistencePort, GoalCategoryPersistencePort, GoalProgressEntryPersistencePort {

    private static final String CUSTOM_CATEGORY_PREFIX = "custom-";

    private final GoalJpaRepository goalJpaRepository;
    private final GoalCategoryJpaRepository goalCategoryJpaRepository;
    private final GoalProgressEntryJpaRepository goalProgressEntryJpaRepository;
    private final Clock goalClock;

    @Override
    @Transactional
    public Goal save(Goal goal) {
        GoalJpaEntity entity = goalJpaRepository.findById(goal.goalId()).orElseGet(GoalJpaEntity::new);
        GoalCategoryJpaEntity category = goalCategoryJpaRepository.findById(goal.categoryId())
                .orElseThrow(() -> new GoalCategoryNotFoundException(goal.categoryId()));

        entity.setGoalId(goal.goalId());
        entity.setPlanningYear(goal.planningYear().getValue());
        entity.setTitle(goal.title());
        entity.setCategory(category);
        entity.setTargetValue(goal.targetValue());
        entity.setUnit(goal.unit());
        entity.setMotivation(goal.motivation());
        entity.setNotes(goal.notes());
        entity.setStatus(goal.status().name());
        entity.setSuggestionBasis(goal.suggestionBasis());
        entity.setCustomizedFromSuggestion(goal.customizedFromSuggestion());
        entity.setArchivedAt(goal.archivedAt());
        entity.setCreatedAt(goal.createdAt());
        entity.setUpdatedAt(goal.updatedAt());
        entity.getCheckpoints().clear();
        for (GoalCheckpoint checkpoint : goal.checkpoints()) {
            GoalCheckpointJpaEntity checkpointEntity = new GoalCheckpointJpaEntity();
            checkpointEntity.setCheckpointId(checkpoint.checkpointId());
            checkpointEntity.setGoal(entity);
            checkpointEntity.setSequenceNumber(checkpoint.sequenceNumber());
            checkpointEntity.setCheckpointDate(checkpoint.checkpointDate());
            checkpointEntity.setTargetValue(checkpoint.targetValue());
            checkpointEntity.setNote(checkpoint.note());
            checkpointEntity.setOrigin(checkpoint.origin());
            checkpointEntity.setOriginalCheckpointDate(checkpoint.originalCheckpointDate());
            checkpointEntity.setOriginalTargetValue(checkpoint.originalTargetValue());
            entity.getCheckpoints().add(checkpointEntity);
        }

        return toGoal(goalJpaRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Goal> findById(UUID goalId) {
        return goalJpaRepository.findById(goalId).map(this::toGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Goal> listGoals(GoalStatus status) {
        List<GoalJpaEntity> entities = status == null
                ? goalJpaRepository.findAllByOrderByUpdatedAtDesc()
                : goalJpaRepository.findAllByStatusOrderByUpdatedAtDesc(status.name());
        return entities.stream().map(this::toGoal).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalCategory> listCategories() {
        return goalCategoryJpaRepository.findAllByOrderBySystemDefinedDescDisplayNameAsc().stream()
                .map(this::toGoalCategory)
                .sorted(categoryComparator())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoalCategory> findCategoryById(UUID categoryId) {
        return goalCategoryJpaRepository.findById(categoryId).map(this::toGoalCategory);
    }

    @Override
    @Transactional
    public GoalCategory createCustomCategory(String displayName) {
        Instant now = Instant.now(goalClock);
        GoalCategoryJpaEntity entity = new GoalCategoryJpaEntity();
        entity.setCategoryId(UUID.randomUUID());
        entity.setKey(CUSTOM_CATEGORY_PREFIX + UUID.randomUUID());
        entity.setDisplayName(displayName);
        entity.setSystemDefined(false);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return toGoalCategory(goalCategoryJpaRepository.save(entity));
    }

    @Override
    @Transactional
    public GoalProgressEntry save(GoalProgressEntry entry) {
        GoalProgressEntryJpaEntity entity = new GoalProgressEntryJpaEntity();
        entity.setProgressEntryId(entry.progressEntryId());
        entity.setGoal(goalJpaRepository.getReferenceById(entry.goalId()));
        entity.setEntryDate(entry.entryDate());
        entity.setProgressValue(entry.progressValue());
        entity.setNote(entry.note());
        entity.setEntryType(entry.entryType());
        entity.setRecordedAt(entry.recordedAt());
        return toGoalProgressEntry(goalProgressEntryJpaRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalProgressEntry> findByGoalId(UUID goalId) {
        return goalProgressEntryJpaRepository.findAllByGoalGoalIdOrderByEntryDateAscRecordedAtAsc(goalId).stream()
                .map(this::toGoalProgressEntry)
                .toList();
    }

    private Goal toGoal(GoalJpaEntity entity) {
        List<GoalCheckpoint> checkpoints = new ArrayList<>();
        for (GoalCheckpointJpaEntity checkpointEntity : entity.getCheckpoints()) {
            checkpoints.add(GoalCheckpoint.of(
                    checkpointEntity.getCheckpointId(),
                    checkpointEntity.getSequenceNumber(),
                    checkpointEntity.getCheckpointDate(),
                    checkpointEntity.getTargetValue(),
                    checkpointEntity.getNote(),
                    checkpointEntity.getOrigin(),
                    checkpointEntity.getOriginalCheckpointDate(),
                    checkpointEntity.getOriginalTargetValue()
            ));
        }

        return Goal.rehydrate(
                entity.getGoalId(),
                Year.of(entity.getPlanningYear()),
                entity.getTitle(),
                entity.getCategory().getCategoryId(),
                entity.getTargetValue(),
                entity.getUnit(),
                entity.getMotivation(),
                entity.getNotes(),
                GoalStatus.valueOf(entity.getStatus()),
                entity.getSuggestionBasis(),
                entity.isCustomizedFromSuggestion(),
                entity.getArchivedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                checkpoints
        );
    }

    private GoalCategory toGoalCategory(GoalCategoryJpaEntity entity) {
        return new GoalCategory(
                entity.getCategoryId(),
                entity.getKey(),
                entity.getDisplayName(),
                entity.isSystemDefined()
        );
    }

    private GoalProgressEntry toGoalProgressEntry(GoalProgressEntryJpaEntity entity) {
        return GoalProgressEntry.record(
                entity.getProgressEntryId(),
                entity.getGoal().getGoalId(),
                entity.getEntryDate(),
                entity.getProgressValue(),
                entity.getNote(),
                entity.getEntryType(),
                entity.getRecordedAt()
        );
    }

    private Comparator<GoalCategory> categoryComparator() {
        List<String> categoryOrder = List.of("financial", "fitness", "reading", "weight", "custom");
        return Comparator
                .comparingInt((GoalCategory category) -> {
                    int index = categoryOrder.indexOf(normalizedCategoryKey(category.key()));
                    return index >= 0 ? index : categoryOrder.size();
                })
                .thenComparing(GoalCategory::displayName, String.CASE_INSENSITIVE_ORDER);
    }

    private String normalizedCategoryKey(String key) {
        if (key == null) {
            return "custom";
        }
        if (key.startsWith(CUSTOM_CATEGORY_PREFIX)) {
            return "custom";
        }
        return key;
    }
}
