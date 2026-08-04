package com.ems.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Search by First Name
    List<Employee> findByFirstNameContainingIgnoreCase(String keyword);

    // Search with Pagination
    Page<Employee> findByFirstNameContainingIgnoreCase(
            String keyword,
            Pageable pageable);

}