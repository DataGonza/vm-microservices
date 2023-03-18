package com.kruger.application.controllers;

import com.kruger.application.models.Employee;
import com.kruger.application.models.UpdateDataFromUser;
import com.kruger.application.services.EmployeeService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Employee getInformation(@AuthenticationPrincipal KeycloakPrincipal<KeycloakSecurityContext> principal) {
        String identifier = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        return employeeService.findEmployee(identifier);
    }

    @PutMapping
    public void updateInformation(@AuthenticationPrincipal KeycloakPrincipal<KeycloakSecurityContext> principal,
                                      @RequestBody UpdateDataFromUser updateData) {
        String identifier = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        employeeService.updateEmployeeFromUser(identifier, updateData);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleIllegalStateException(IllegalStateException e) {
        return e.getMessage();
    }
}
