package ru.practice.employeeaccounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice.employeeaccounting.model.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    List<Employee> findByFullNameContainingIgnoreCase(String name);

    List<Employee> findByActive(Boolean active);

    List<Employee> findByFullNameContainingIgnoreCaseAndActive(String name, Boolean active);
}
