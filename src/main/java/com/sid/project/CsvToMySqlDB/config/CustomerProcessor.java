package com.sid.project.CsvToMySqlDB.config;

import com.sid.project.CsvToMySqlDB.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer>
{

    @Override
    public Customer process(Customer item) throws Exception {
        return item;
    }
}
