package com.ybritto.milestory.goal.out.adapter;

import com.ybritto.milestory.goal.application.port.out.GoalCategoryPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalPersistencePort;
import com.ybritto.milestory.goal.application.port.out.GoalProgressEntryPersistencePort;
import com.ybritto.milestory.goal.application.usecase.ArchiveGoalUseCase;
import com.ybritto.milestory.goal.application.usecase.CreateCustomGoalCategoryUseCase;
import com.ybritto.milestory.goal.application.usecase.CreateGoalUseCase;
import com.ybritto.milestory.goal.application.usecase.GetGoalDetailUseCase;
import com.ybritto.milestory.goal.application.usecase.ListGoalCategoriesUseCase;
import com.ybritto.milestory.goal.application.usecase.ListGoalsUseCase;
import com.ybritto.milestory.goal.application.usecase.PreviewGoalPlanUseCase;
import com.ybritto.milestory.goal.application.usecase.RecordProgressEntryUseCase;
import com.ybritto.milestory.goal.application.usecase.RestoreGoalUseCase;
import com.ybritto.milestory.goal.application.usecase.UpdateGoalUseCase;
import com.ybritto.milestory.goal.domain.GoalProgressStatusService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoalConfiguration {

    @Bean
    Clock goalClock() {
        return Clock.systemUTC();
    }

    @Bean
    PreviewGoalPlanUseCase previewGoalPlanUseCase(
            GoalCategoryPersistencePort goalCategoryPersistencePort,
            Clock goalClock
    ) {
        return new PreviewGoalPlanUseCase(goalCategoryPersistencePort, goalClock);
    }

    @Bean
    ListGoalCategoriesUseCase listGoalCategoriesUseCase(GoalCategoryPersistencePort goalCategoryPersistencePort) {
        return new ListGoalCategoriesUseCase(goalCategoryPersistencePort);
    }

    @Bean
    CreateCustomGoalCategoryUseCase createCustomGoalCategoryUseCase(GoalCategoryPersistencePort goalCategoryPersistencePort) {
        return new CreateCustomGoalCategoryUseCase(goalCategoryPersistencePort);
    }

    @Bean
    CreateGoalUseCase createGoalUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalCategoryPersistencePort goalCategoryPersistencePort,
            Clock goalClock
    ) {
        return new CreateGoalUseCase(goalPersistencePort, goalCategoryPersistencePort, goalClock);
    }

    @Bean
    UpdateGoalUseCase updateGoalUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalCategoryPersistencePort goalCategoryPersistencePort,
            Clock goalClock
    ) {
        return new UpdateGoalUseCase(goalPersistencePort, goalCategoryPersistencePort, goalClock);
    }

    @Bean
    GoalProgressStatusService goalProgressStatusService() {
        return new GoalProgressStatusService();
    }

    @Bean
    GetGoalDetailUseCase getGoalDetailUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalProgressEntryPersistencePort goalProgressEntryPersistencePort,
            GoalProgressStatusService goalProgressStatusService,
            Clock goalClock
    ) {
        return new GetGoalDetailUseCase(
                goalPersistencePort,
                goalProgressEntryPersistencePort,
                goalProgressStatusService,
                goalClock
        );
    }

    @Bean
    ListGoalsUseCase listGoalsUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalProgressEntryPersistencePort goalProgressEntryPersistencePort,
            GoalProgressStatusService goalProgressStatusService,
            Clock goalClock
    ) {
        return new ListGoalsUseCase(
                goalPersistencePort,
                goalProgressEntryPersistencePort,
                goalProgressStatusService,
                goalClock
        );
    }

    @Bean
    RecordProgressEntryUseCase recordProgressEntryUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalProgressEntryPersistencePort goalProgressEntryPersistencePort,
            Clock goalClock
    ) {
        return new RecordProgressEntryUseCase(goalPersistencePort, goalProgressEntryPersistencePort, goalClock);
    }

    @Bean
    ArchiveGoalUseCase archiveGoalUseCase(GoalPersistencePort goalPersistencePort, Clock goalClock) {
        return new ArchiveGoalUseCase(goalPersistencePort, goalClock);
    }

    @Bean
    RestoreGoalUseCase restoreGoalUseCase(
            GoalPersistencePort goalPersistencePort,
            GoalCategoryPersistencePort goalCategoryPersistencePort,
            PreviewGoalPlanUseCase previewGoalPlanUseCase,
            Clock goalClock
    ) {
        return new RestoreGoalUseCase(goalPersistencePort, goalCategoryPersistencePort, previewGoalPlanUseCase, goalClock);
    }
}
