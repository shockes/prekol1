package ru.practice.employeeaccounting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Введите ФИО")
    @Size(min = 2, max = 100, message = "ФИО должно содержать от 2 до 100 символов")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Введите email")
    @Email(message = "Введите корректный email")
    @Size(max = 120, message = "Email не должен превышать 120 символов")
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank(message = "Введите телефон")
    @Pattern(regexp = "^\\+?[0-9()\\-\\s]{7,20}$", message = "Введите корректный телефон (например, +7 (999) 123-45-67)")
    @Column(nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "Введите должность")
    @Size(max = 80, message = "Должность не должна превышать 80 символов")
    @Column(nullable = false, length = 80)
    private String position;

    @NotNull(message = "Введите зарплату")
    @DecimalMin(value = "0.01", message = "Зарплата должна быть больше нуля")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @NotNull(message = "Укажите активность сотрудника")
    @Column(nullable = false)
    private Boolean active = true;

    public Employee() {
    }

    public Employee(String fullName, String email, String phone, String position, BigDecimal salary, Boolean active) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
        this.active = active == null ? true : active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
