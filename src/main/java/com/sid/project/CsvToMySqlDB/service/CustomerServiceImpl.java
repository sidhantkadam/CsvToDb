package com.sid.project.CsvToMySqlDB.service;

import com.sid.project.CsvToMySqlDB.entity.Customer;
import com.sid.project.CsvToMySqlDB.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public List<Customer> getAllCustomers()
    {
        return customerRepo.findAll();
    }

    @Override
    public Customer getCustomerById(Integer id)
    {
        return customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer Not Found !"));
    }
}
