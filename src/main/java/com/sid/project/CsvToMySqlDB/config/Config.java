package com.sid.project.CsvToMySqlDB.config;

import com.sid.project.CsvToMySqlDB.entity.Customer;
import com.sid.project.CsvToMySqlDB.repository.CustomerRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Config {

    @Autowired
    private CustomerRepo customerRepo;

    @Bean
    public FlatFileItemReader<Customer> itemReader() {
        FlatFileItemReader<Customer> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        fileItemReader.setName("csvReader");
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setStrict(false);
        fileItemReader.setLineMapper(lineMapper());
        return fileItemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
        tokenizer.setStrict(false);

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CustomerProcessor customerProcessor() {
        return new CustomerProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customer> itemWriter() {
        RepositoryItemWriter<Customer> itemWriter = new RepositoryItemWriter<>();

        itemWriter.setRepository(customerRepo);
        itemWriter.setMethodName("save");
        return itemWriter;
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step", jobRepository).
                <Customer, Customer>chunk(10, platformTransactionManager)
                .reader(itemReader())
                .processor(customerProcessor())
                .writer(itemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    private TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
        return simpleAsyncTaskExecutor;
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("job", jobRepository)
                .flow(step(jobRepository, platformTransactionManager))
                .end().build();
    }
}


