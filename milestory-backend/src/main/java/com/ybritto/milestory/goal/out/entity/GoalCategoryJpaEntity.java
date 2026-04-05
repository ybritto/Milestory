package com.ybritto.milestory.goal.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal_category")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalCategoryJpaEntity {

    @Id
    @Column(name = "category_id", nullable = false, updatable = false)
    private UUID categoryId;

    @Column(name = "key")
    private String key;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "system_defined", nullable = false)
    private boolean systemDefined;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
