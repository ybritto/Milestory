package com.ybritto.milestory.goal.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal_checkpoint")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalCheckpointJpaEntity {

    @Id
    @Column(name = "checkpoint_id", nullable = false, updatable = false)
    private UUID checkpointId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goal_id", nullable = false)
    private GoalJpaEntity goal;

    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    @Column(name = "checkpoint_date", nullable = false)
    private LocalDate checkpointDate;

    @Column(name = "target_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal targetValue;

    @Column(name = "note")
    private String note;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "original_checkpoint_date")
    private LocalDate originalCheckpointDate;

    @Column(name = "original_target_value", precision = 19, scale = 4)
    private BigDecimal originalTargetValue;
}
