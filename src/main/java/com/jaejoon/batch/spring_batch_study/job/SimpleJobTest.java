package com.jaejoon.batch.spring_batch_study.job;

import com.jaejoon.batch.spring_batch_study.tasklet.SimpleTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
            .start(simpleStep2())
            .build();
    }

    @Bean
    public Step simpleStep2() {
        return stepBuilderFactory.get("simpleStep2")
            .tasklet(new SimpleTasklet())
            .build();
    }
}
