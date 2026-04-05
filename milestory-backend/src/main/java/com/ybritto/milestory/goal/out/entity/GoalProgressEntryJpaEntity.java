package com.ybritto.milestory.goal.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal_progress_entry")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalProgressEntryJpaEntity {

    @Id
    @Column(name = "progress_entry_id", nullable = false, updatable = false)
    private UUID progressEntryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goal_id", nullable = false)
    private GoalJpaEntity goal;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "progress_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal progressValue;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private com.ybritto.milestory.goal.domain.GoalProgressEntryType entryType;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;
}
