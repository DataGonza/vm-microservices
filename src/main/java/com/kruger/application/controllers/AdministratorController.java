package com.kruger.application.controllers;

import com.kruger.application.models.Employee;
import com.kruger.application.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/employee")
public class AdministratorController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getEmployees(
            @RequestParam(required = false) String vaccinationStatus,
            @RequestParam(required = false) String typesOfVaccine,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")  Date dateFromVaccination,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")  Date dateToVaccination) {

        if (vaccinationStatus == null && typesOfVaccine == null
                && dateFromVaccination == null && dateToVaccination == null) {
            return employeeService.getAllEmployees();
        }
        else {
            return employeeService.queryEmployees(vaccinationStatus, typesOfVaccine,
                    dateFromVaccination, dateToVaccination);
        }
    }

    @GetMapping("/{identifier}")
    public Employee findEmployee(@PathVariable String identifier) {
        return employeeService.findEmployee(identifier);
    }

    @PostMapping
    public void saveEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
    }

    @DeleteMapping("/{identifier}")
    public void deleteEmployee(@PathVariable String identifier) {
        employeeService.deleteEmployee(identifier);
    }

    @PutMapping("/{identifier}")
    public void updateEmployee(@PathVariable String identifier, @RequestBody Employee updatedEmployee) {
        employeeService.updateEmployee(identifier, updatedEmployee);
    }

    @PostMapping("/release/{identifier}")
    public String releaseEmployee(@PathVariable String identifier) {
        return employeeService.releaseEmployee(identifier);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    List<String> handleConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleNoResultException(NoResultException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleIllegalStateException(IllegalStateException e) {
        return e.getMessage();
    }
}
