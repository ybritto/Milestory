package com.ybritto.milestory.goal.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ybritto.milestory.goal.support.GoalTestSupport;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GoalProgressStatusServiceTest {

    private final GoalProgressStatusService service = new GoalProgressStatusService();

    @Test
    void derivesBehindStatusWithSupportiveButHonestCopy() {
        GoalProgressSnapshot snapshot = service.calculate(
                BigDecimal.valueOf(24),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24)),
                List.of(entry(LocalDate.of(2026, 4, 4), 1, GoalProgressEntryType.NORMAL)),
                LocalDate.of(2026, 4, 4)
        );

        assertEquals(new BigDecimal("1"), snapshot.currentProgressValue());
        assertEquals(GoalPaceStatus.BEHIND, snapshot.paceStatus());
        assertEquals("You're behind the pace you planned for today, but the year still has room to recover.", snapshot.paceSummary());
        assertEquals("You're a bit under today's planned mark. A focused push before the next checkpoint can close the gap.", snapshot.paceDetail());
    }

    @Test
    void derivesOnPaceStatusInsideToleranceBand() {
        GoalProgressSnapshot snapshot = service.calculate(
                BigDecimal.valueOf(24),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24)),
                List.of(entry(LocalDate.of(2026, 4, 4), 6.5, GoalProgressEntryType.NORMAL)),
                LocalDate.of(2026, 4, 4)
        );

        assertEquals(GoalPaceStatus.ON_PACE, snapshot.paceStatus());
        assertEquals("You're right where this goal expected you to be today.", snapshot.paceSummary());
        assertEquals("Your progress is tracking the checkpoint path you set for this point in the year.", snapshot.paceDetail());
    }

    @Test
    void derivesAheadStatusWithMotivationalCopy() {
        GoalProgressSnapshot snapshot = service.calculate(
                BigDecimal.valueOf(24),
                GoalTestSupport.checkpoints(BigDecimal.valueOf(24)),
                List.of(entry(LocalDate.of(2026, 4, 4), 9, GoalProgressEntryType.NORMAL)),
                LocalDate.of(2026, 4, 4)
        );

        assertEquals(GoalPaceStatus.AHEAD, snapshot.paceStatus());
        assertEquals("You're ahead of the pace you planned for today.", snapshot.paceSummary());
        assertEquals("You've already moved past today's planned mark. Keep pressing while this rhythm is working.", snapshot.paceDetail());
    }

    private GoalProgressEntry entry(LocalDate entryDate, double progressValue, GoalProgressEntryType entryType) {
        return GoalProgressEntry.record(
                UUID.randomUUID(),
                UUID.fromString("00000000-0000-0000-0000-000000000499"),
                entryDate,
                BigDecimal.valueOf(progressValue),
                "",
                entryType,
                GoalTestSupport.FIXED_CLOCK.instant()
        );
    }
}
