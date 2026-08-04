package com.ems.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ems.entity.Employee;

public interface EmployeeService {

    // Get all employees
    List<Employee> getAllEmployees();

    // Save employee
    Employee saveEmployee(Employee employee);

    // Get employee by ID
    Employee getEmployeeById(Long id);

    // Update employee
    Employee updateEmployee(Employee employee);

    // Delete employee
    void deleteEmployeeById(Long id);

    // Search employees
    List<Employee> searchEmployee(String keyword);

    // Search with Pagination
    Page<Employee> getEmployeesPaginated(String keyword, Pageable pageable);

}