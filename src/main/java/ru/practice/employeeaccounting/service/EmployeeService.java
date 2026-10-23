package ru.practice.employeeaccounting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice.employeeaccounting.model.Employee;
import ru.practice.employeeaccounting.repository.EmployeeRepository;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> search(String name, Boolean active) {
        String normalizedName = name == null ? "" : name.trim();
        boolean hasName = !normalizedName.isEmpty();

        if (hasName && active != null) {
            return employeeRepository.findByFullNameContainingIgnoreCaseAndActive(normalizedName, active);
        }
        if (hasName) {
            return employeeRepository.findByFullNameContainingIgnoreCase(normalizedName);
        }
        if (active != null) {
            return employeeRepository.findByActive(active);
        }
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmailIgnoreCase(email == null ? "" : email.trim());
    }

    @Transactional(readOnly = true)
    public boolean emailExistsForAnotherEmployee(String email, Long id) {
        String normalizedEmail = email == null ? "" : email.trim();
        if (id == null) {
            return emailExists(normalizedEmail);
        }
        return employeeRepository.existsByEmailIgnoreCaseAndIdNot(normalizedEmail, id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }

    public static class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(Long id) {
            super("Сотрудник с ID " + id + " не найден");
        }
    }
}
