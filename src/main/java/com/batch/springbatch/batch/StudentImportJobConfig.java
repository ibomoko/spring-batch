package com.batch.springbatch.batch;

import com.batch.springbatch.entity.Student;
import com.batch.springbatch.model.StudentDTO;
import com.batch.springbatch.repository.StudentRepository;
import com.batch.springbatch.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.FileInputStream;

@Configuration
@RequiredArgsConstructor
public class StudentImportJobConfig {

    private final FileService fileService;
    private final StudentRepository studentRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    @SneakyThrows
    public FlatFileItemReader<StudentDTO> studentFlatFileItemReader(@Value("#{jobParameters[filename]}") String filename) {
        FlatFileItemReader<StudentDTO> reader = new FlatFileItemReader<>();
        reader.setResource(new InputStreamResource(new FileInputStream(fileService.getFile(filename))));
        reader.setName("Student-CSV-Reader");
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    private LineMapper<StudentDTO> lineMapper() {
        DefaultLineMapper<StudentDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("first_name", "last_name", "email", "phone_number");

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new StudentFieldMapper());

        return lineMapper;
    }

    @Bean("studentCsvImportStep")
    @JobScope
    public Step importStudentCsvStep(FlatFileItemReader<StudentDTO> reader, ItemProcessor<StudentDTO, Student> processor, StudentItemWriter writer) {
        return stepBuilderFactory.get("studentCsvImport")
                .<StudentDTO, Student>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean("importStudentsJob")
    public Job runJob(@Qualifier("studentCsvImportStep") Step step) {
        return jobBuilderFactory.get("importStudentItems").start(step).build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }

    @Bean
    @StepScope
    public StudentItemWriter studentItemWriter(){
        return new StudentItemWriter(this.studentRepository);
    }

    @Bean
    @StepScope
    public ItemProcessor<StudentDTO, Student> studentItemProcessor() {
        return new StudentItemProcessor();
    }
}
