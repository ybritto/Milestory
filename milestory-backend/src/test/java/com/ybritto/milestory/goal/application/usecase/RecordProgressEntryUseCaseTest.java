package com.ybritto.milestory.goal.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ybritto.milestory.goal.application.model.RecordProgressEntryCommand;
import com.ybritto.milestory.goal.application.port.out.GoalProgressEntryPersistencePort;
import com.ybritto.milestory.goal.domain.Goal;
import com.ybritto.milestory.goal.domain.GoalProgressEntry;
import com.ybritto.milestory.goal.domain.GoalProgressEntryType;
import com.ybritto.milestory.goal.domain.GoalStatus;
import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RecordProgressEntryUseCaseTest {

    @Test
    void classifiesLowerCumulativeEntryAsCorrection() {
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        InMemoryGoalProgressEntryPersistencePort progressEntryPort = new InMemoryGoalProgressEntryPersistencePort();
        Goal goal = GoalTestSupport.activeGoal(
                UUID.fromString("00000000-0000-0000-0000-000000000401"),
                GoalTestSupport.readingCategory().categoryId(),
                "Read 24 books",
                BigDecimal.valueOf(24),
                "books",
                "Build a steadier reading rhythm",
                "Stay consistent all year",
                "CATEGORY_AWARE",
                false,
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24))
        );
        goalPort.add(goal);
        progressEntryPort.save(GoalProgressEntry.record(
                UUID.fromString("00000000-0000-0000-0000-000000000411"),
                goal.goalId(),
                LocalDate.of(2026, 3, 10),
                BigDecimal.valueOf(8),
                "Finished a strong month",
                GoalProgressEntryType.NORMAL,
                Instant.parse("2026-03-10T18:00:00Z")
        ));

        RecordProgressEntryUseCase useCase = new RecordProgressEntryUseCase(
                goalPort,
                progressEntryPort,
                GoalTestSupport.FIXED_CLOCK
        );

        GoalProgressEntry savedEntry = useCase.record(
                goal.goalId(),
                new RecordProgressEntryCommand(
                        LocalDate.of(2026, 4, 4),
                        BigDecimal.valueOf(6),
                        "Corrected an overcount from last month"
                )
        );

        assertEquals(GoalProgressEntryType.CORRECTION, savedEntry.entryType());
        assertEquals(savedEntry, progressEntryPort.findLatestByGoalId(goal.goalId()).orElseThrow());
    }

    @Test
    void rejectsArchivedGoalsUsingTheConflictPath() {
        GoalTestSupport.InMemoryGoalPersistencePort goalPort = new GoalTestSupport.InMemoryGoalPersistencePort();
        InMemoryGoalProgressEntryPersistencePort progressEntryPort = new InMemoryGoalProgressEntryPersistencePort();
        Goal archivedGoal = GoalTestSupport.goal(
                UUID.fromString("00000000-0000-0000-0000-000000000402"),
                Year.of(2026),
                GoalTestSupport.financialCategory().categoryId(),
                "Save 1200 euros",
                BigDecimal.valueOf(1200),
                "eur",
                "Build an emergency buffer",
                "Monthly transfers",
                "GENERIC_FALLBACK",
                false,
                GoalStatus.ARCHIVED,
                Instant.parse("2026-03-15T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-03-15T00:00:00Z"),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(1200))
        );
        goalPort.add(archivedGoal);

        RecordProgressEntryUseCase useCase = new RecordProgressEntryUseCase(
                goalPort,
                progressEntryPort,
                GoalTestSupport.FIXED_CLOCK
        );

        assertThrows(IllegalStateException.class, () -> useCase.record(
                archivedGoal.goalId(),
                new RecordProgressEntryCommand(
                        LocalDate.of(2026, 4, 4),
                        BigDecimal.valueOf(300),
                        "Tried to add progress after archiving"
                )
        ));
    }

    private static final class InMemoryGoalProgressEntryPersistencePort implements GoalProgressEntryPersistencePort {

        private final List<GoalProgressEntry> entries = new ArrayList<>();

        @Override
        public GoalProgressEntry save(GoalProgressEntry entry) {
            entries.add(entry);
            return entry;
        }

        @Override
        public List<GoalProgressEntry> findByGoalId(UUID goalId) {
            return entries.stream()
                    .filter(entry -> entry.goalId().equals(goalId))
                    .sorted(Comparator.comparing(GoalProgressEntry::entryDate).thenComparing(GoalProgressEntry::recordedAt))
                    .toList();
        }

        private Optional<GoalProgressEntry> findLatestByGoalId(UUID goalId) {
            return findByGoalId(goalId).stream().reduce((first, second) -> second);
        }
    }
}
