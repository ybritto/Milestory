package com.ybritto.milestory.goal.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalJpaEntity {

    @Id
    @Column(name = "goal_id", nullable = false, updatable = false)
    private UUID goalId;

    @Column(name = "planning_year", nullable = false)
    private Integer planningYear;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private GoalCategoryJpaEntity category;

    @Column(name = "target_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal targetValue;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "motivation", nullable = false)
    private String motivation;

    @Column(name = "notes", nullable = false)
    private String notes;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "suggestion_basis", nullable = false)
    private String suggestionBasis;

    @Column(name = "customized_from_suggestion", nullable = false)
    private boolean customizedFromSuggestion;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "goal", orphanRemoval = true, cascade = jakarta.persistence.CascadeType.ALL)
    @OrderBy("sequenceNumber ASC")
    private List<GoalCheckpointJpaEntity> checkpoints = new ArrayList<>();

    @OneToMany(mappedBy = "goal")
    @OrderBy("entryDate ASC, recordedAt ASC")
    private List<GoalProgressEntryJpaEntity> progressEntries = new ArrayList<>();
}
