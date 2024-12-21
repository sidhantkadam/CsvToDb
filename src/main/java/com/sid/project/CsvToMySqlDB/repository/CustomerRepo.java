package com.sid.project.CsvToMySqlDB.repository;

import com.sid.project.CsvToMySqlDB.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer>
{

}
