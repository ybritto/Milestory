package com.ybritto.milestory.goal.out.repository;

import java.util.List;
import java.util.UUID;

import com.ybritto.milestory.goal.out.entity.GoalCategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalCategoryJpaRepository extends JpaRepository<GoalCategoryJpaEntity, UUID> {

    List<GoalCategoryJpaEntity> findAllByOrderBySystemDefinedDescDisplayNameAsc();
}
