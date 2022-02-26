package com.jaejoon.batch.spring_batch_study.job;

import com.jaejoon.batch.spring_batch_study.tasklet.SimpleTasklet;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SimpleJobTest {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob2() {
        return jobBuilderFactory.get("simpleJob2")
            .start(simpleStepStart())
            .next(jobExecutionDecider1())
            .from(jobExecutionDecider1())
                .on("NOTNULL")
                .to(simpleStep2())
                .on("STOPPED")
                .end()
            .from(jobExecutionDecider1())
                .on("NULL")
                .to(simpleStep4())
            .next(simpleStep3())
            .end()
            .build();


    }

    @Bean
    public Step simpleStepStart() {
        return stepBuilderFactory.get("simpleStepStart")
            .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
            .build();
    }

    @Bean
    public Step simpleStep2() {
        return stepBuilderFactory.get("simpleStep2")
            .tasklet((contribution, chunkContext) -> {
                    System.out.println("중지");
                    contribution.setExitStatus(ExitStatus.STOPPED);
                    return RepeatStatus.FINISHED;
                }
            )
            .build();
    }

    @Bean
    public Step simpleStep3() {
        return stepBuilderFactory.get("simpleStep3")
            .tasklet(new SimpleTasklet())
            .build();
    }

    @Bean
    public Step simpleStep4() {
        return stepBuilderFactory.get("simpleStep4")
            .tasklet((contribution, chunkContext) -> {
                    System.out.println("SIMPLE STEP 4");
                    return RepeatStatus.FINISHED;
                }
            )
            .build();
    }

    @Bean
    public JobExecutionDecider jobExecutionDecider1() {
        return (jobExecution, stepExecution) -> {
            Random random = new Random();
            int randomNumber = random.nextInt(50) + 1;
            log.info("랜덤숫자 {}", randomNumber);
            if (randomNumber % 2 == 0) {
                return new FlowExecutionStatus("NOTNULL");
            }
            return new FlowExecutionStatus("NULL");
        };
    }
}
