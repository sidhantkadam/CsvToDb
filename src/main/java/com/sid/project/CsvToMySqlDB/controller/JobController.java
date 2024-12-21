package com.sid.project.CsvToMySqlDB.controller;

import com.sid.project.CsvToMySqlDB.entity.Customer;
import com.sid.project.CsvToMySqlDB.service.CustomerService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/runJob")
    public ResponseEntity<String> executeJob() {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException | JobRestartException |
                 JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Job Run Successfully !");
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<List<Customer>> getAllData()
    {
        List<Customer> allCustomers = customerService.getAllCustomers();
        return ResponseEntity.status(HttpStatus.FOUND).body(allCustomers);
    }

    @GetMapping("/getCustomer")
    public ResponseEntity<Customer> getCustomerById(@RequestParam("customer_id") Integer id)
    {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(customer);
    }
}
