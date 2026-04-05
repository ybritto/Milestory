package com.ybritto.milestory.goal.out.repository;

import java.util.List;
import java.util.UUID;

import com.ybritto.milestory.goal.out.entity.GoalJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalJpaRepository extends JpaRepository<GoalJpaEntity, UUID> {

    @EntityGraph(attributePaths = {"category", "checkpoints"})
    List<GoalJpaEntity> findAllByStatusOrderByUpdatedAtDesc(String status);

    @EntityGraph(attributePaths = {"category", "checkpoints"})
    List<GoalJpaEntity> findAllByOrderByUpdatedAtDesc();

    @EntityGraph(attributePaths = {"category", "checkpoints"})
    java.util.Optional<GoalJpaEntity> findById(UUID goalId);
}
