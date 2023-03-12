package com.juaracoding.DBLaundry.repo;


import com.juaracoding.DBLaundry.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

}