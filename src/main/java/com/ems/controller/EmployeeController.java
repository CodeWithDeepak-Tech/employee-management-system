package com.ems.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ems.entity.Employee;
import com.ems.export.EmployeeExcelExporter;
import com.ems.export.EmployeePdfExporter;
import com.ems.service.EmployeeService;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ==========================
    // Display Employee List + Search + Dashboard
    // ==========================
    @GetMapping("/employees")
public String listEmployees(

        @RequestParam(value = "keyword", required = false) String keyword,

        @RequestParam(defaultValue = "0") int page,

        @RequestParam(defaultValue = "10") int size,

        @RequestParam(value = "success", required = false) String success,

        Model model) {

    Pageable pageable = PageRequest.of(page, size);

    Page<Employee> employeePage =
            employeeService.getEmployeesPaginated(keyword, pageable);

            System.out.println("Current Page = " + page);
System.out.println("Employees Returned = " + employeePage.getContent().size());
System.out.println("Total Pages = " + employeePage.getTotalPages());

    model.addAttribute("employees", employeePage.getContent());

    model.addAttribute("currentPage", page);

    model.addAttribute("totalPages", employeePage.getTotalPages());

    model.addAttribute("totalItems", employeePage.getTotalElements());

    model.addAttribute("size", size);

    model.addAttribute("keyword", keyword);

    model.addAttribute("success", success);

    // Dashboard Statistics
    List<Employee> allEmployees = employeeService.getAllEmployees();
    model.addAttribute("employeeCount", allEmployees.size());

    long departmentCount = allEmployees.stream()
            .map(Employee::getDepartment)
            .distinct()
            .count();

    double averageSalary = allEmployees.stream()
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0);

    double highestSalary = allEmployees.stream()
            .mapToDouble(Employee::getSalary)
            .max()
            .orElse(0);

    model.addAttribute("departmentCount", departmentCount);
    model.addAttribute("averageSalary", averageSalary);
    model.addAttribute("highestSalary", highestSalary);

    return "employees";
}


    // ==========================
    // Show Add Employee Form
    // ==========================
    @GetMapping("/employees/new")
    public String createEmployeeForm(Model model) {

        model.addAttribute("employee", new Employee());

        return "create_employee";
    }

    // ==========================
    // Save Employee
    // ==========================
    @PostMapping("/employees")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {

        employeeService.saveEmployee(employee);

        return "redirect:/employees?success=added";
}   

    // ==========================
    // Show Edit Employee Form
    // ==========================
    @GetMapping("/employees/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id,
                                       Model model) {

        model.addAttribute("employee",
                employeeService.getEmployeeById(id));

        return "edit_employee";
    }

    // ==========================
    // Update Employee
    // ==========================
    @PostMapping("/employees/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @ModelAttribute("employee") Employee employee) {

        Employee existingEmployee =
                employeeService.getEmployeeById(id);

        existingEmployee.setId(id);
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setSalary(employee.getSalary());

        employeeService.updateEmployee(existingEmployee);

        return "redirect:/employees?success=updated";
    }

    // ==========================
    // Delete Employee
    // ==========================
    @GetMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployeeById(id);

        return "redirect:/employees?success=deleted";
    }

    @GetMapping("/employees/export/excel")
public ResponseEntity<InputStreamResource> exportExcel() throws IOException {

    List<Employee> employees = employeeService.getAllEmployees();

    InputStreamResource file =
            new InputStreamResource(EmployeeExcelExporter.export(employees));

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=employees.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
}

@GetMapping("/employees/export/pdf")
public ResponseEntity<InputStreamResource> exportPdf() {

    List<Employee> employees = employeeService.getAllEmployees();

    InputStreamResource file =
            new InputStreamResource(EmployeePdfExporter.export(employees));

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=employees.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(file);

}

@GetMapping("/abc")
@ResponseBody
public String abc() {
    return "Controller Working";
}
@GetMapping("/testpdf")
@ResponseBody
public String testPdf() {
    return "PDF Mapping Working";
}
   

}