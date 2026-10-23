package ru.practice.employeeaccounting.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.practice.employeeaccounting.model.Employee;
import ru.practice.employeeaccounting.service.EmployeeService;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping({"/", "/employees/"})
    public String home() {
        return "redirect:/employees";
    }

    @GetMapping("/employees")
    public String list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String active,
                       Model model) {
        Boolean activeFilter = parseActive(active);
        model.addAttribute("employees", employeeService.search(name, activeFilter));
        model.addAttribute("name", name == null ? "" : name);
        model.addAttribute("active", active == null ? "" : active);
        return "employees-list";
    }

    @GetMapping("/employees/add")
    public String addForm(Model model) {
        Employee employee = new Employee();
        employee.setActive(true);
        model.addAttribute("employee", employee);
        model.addAttribute("formAction", "/employees/add");
        model.addAttribute("formTitle", "Добавление сотрудника");
        return "employee-form";
    }

    @PostMapping("/employees/add")
    public String add(@Valid @ModelAttribute("employee") Employee employee,
                      BindingResult bindingResult, Model model) {
        checkEmail(employee, null, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/employees/add");
            model.addAttribute("formTitle", "Добавление сотрудника");
            return "employee-form";
        }
        try {
            employeeService.save(employee);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.rejectValue("email", "duplicate", "Сотрудник с таким email уже существует");
            model.addAttribute("formAction", "/employees/add");
            model.addAttribute("formTitle", "Добавление сотрудника");
            return "employee-form";
        }
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getById(id));
        model.addAttribute("formAction", "/employees/edit/" + id);
        model.addAttribute("formTitle", "Редактирование сотрудника");
        return "employee-form";
    }

    @PostMapping("/employees/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("employee") Employee employee,
                       BindingResult bindingResult, Model model) {
        employee.setId(id);
        checkEmail(employee, id, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/employees/edit/" + id);
            model.addAttribute("formTitle", "Редактирование сотрудника");
            return "employee-form";
        }
        try {
            employeeService.save(employee);
        } catch (DataIntegrityViolationException exception) {
            bindingResult.rejectValue("email", "duplicate", "Сотрудник с таким email уже существует");
            model.addAttribute("formAction", "/employees/edit/" + id);
            model.addAttribute("formTitle", "Редактирование сотрудника");
            return "employee-form";
        }
        return "redirect:/employees";
    }

    @PostMapping("/employees/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.delete(id);
        } catch (EmployeeService.EmployeeNotFoundException exception) {
            redirectAttributes.addFlashAttribute("notice", exception.getMessage());
        }
        return "redirect:/employees";
    }

    private void checkEmail(Employee employee, Long id, BindingResult bindingResult) {
        if (employee.getEmail() != null && !employee.getEmail().isBlank()
                && employeeService.emailExistsForAnotherEmployee(employee.getEmail(), id)) {
            bindingResult.rejectValue("email", "duplicate", "Сотрудник с таким email уже существует");
        }
    }

    private Boolean parseActive(String active) {
        if ("true".equals(active)) {
            return true;
        }
        if ("false".equals(active)) {
            return false;
        }
        return null;
    }
}
