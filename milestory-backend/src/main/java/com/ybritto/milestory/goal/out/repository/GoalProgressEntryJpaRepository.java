package com.ybritto.milestory.goal.out.repository;

import java.util.List;
import java.util.UUID;

import com.ybritto.milestory.goal.out.entity.GoalProgressEntryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalProgressEntryJpaRepository extends JpaRepository<GoalProgressEntryJpaEntity, UUID> {

    List<GoalProgressEntryJpaEntity> findAllByGoalGoalIdOrderByEntryDateAscRecordedAtAsc(UUID goalId);
}
