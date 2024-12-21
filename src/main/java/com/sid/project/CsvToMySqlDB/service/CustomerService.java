package com.sid.project.CsvToMySqlDB.service;

import com.sid.project.CsvToMySqlDB.entity.Customer;

import java.util.List;

public interface CustomerService
{
    List<Customer> getAllCustomers();

    Customer getCustomerById(Integer id);
}
