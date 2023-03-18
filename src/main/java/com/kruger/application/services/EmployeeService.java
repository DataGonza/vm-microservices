package com.kruger.application.services;

import com.kruger.application.enums.EmployeeErrorMessages;
import com.kruger.application.enums.TypesOfVaccine;
import com.kruger.application.enums.VaccinationStatus;
import com.kruger.application.models.Employee;
import com.kruger.application.models.UpdateDataFromUser;
import com.kruger.application.persistence.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final KeycloakService keycloakService;
    private final Set<String> vaccinationStatus;
    private final Set<String> typesOfVaccine;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, KeycloakService keycloakService) {
        this.employeeRepository = employeeRepository;
        this.keycloakService = keycloakService;

        this.vaccinationStatus = Arrays.stream(VaccinationStatus.values())
                .map(VaccinationStatus::getStatus)
                .collect(Collectors.toSet());

        this.typesOfVaccine = Arrays.stream(TypesOfVaccine.values())
                .map(TypesOfVaccine::getVaccine)
                .collect(Collectors.toSet());

    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> queryEmployees(String vaccinationStatus, String typeOfVaccine,
                                         Date dateFromVaccination, Date dateToVaccination) {

        return this.getAllEmployees()
                .stream()
                .filter(employee -> vaccinationStatus == null
                        || Objects.equals(employee.getVaccinationStatus(), vaccinationStatus))
                .filter(employee -> typeOfVaccine == null
                        || Objects.equals(employee.getTypesOfVaccine(), typeOfVaccine))
                .filter(employee -> dateFromVaccination == null
                        || employee.getVaccinationDate() == null
                        || employee.getVaccinationDate().after(dateFromVaccination))
                .filter(employee -> dateToVaccination == null
                        || employee.getVaccinationDate() == null
                        || employee.getVaccinationDate().before(dateToVaccination))
                .collect(Collectors.toList());
    }

    public void saveEmployee(Employee employee) {
        String identifier = employee.getIdentifier();
        Optional<Employee> employeeInBd = employeeRepository.findEmployeeByIdentifier(identifier);
        if (employeeInBd.isPresent()) {
            throw new IllegalStateException(String.format(EmployeeErrorMessages.ALREADY_EXIST.getMessage(), identifier));
        }

        employeeRepository.save(employee);
    }

    public Employee findEmployee(String identifier) {
        return employeeRepository
                .findEmployeeByIdentifier(identifier)
                .orElseThrow(() -> new NoResultException(
                        String.format(EmployeeErrorMessages.NOT_FOUND_MESSAGE.getMessage(), identifier)));
    }

    public void deleteEmployee(String identifier) {
        Employee employee = this.findEmployee(identifier);
        employeeRepository.deleteById(employee.getId());
        keycloakService.deleteEmployee(identifier);
    }

    public void updateEmployee(String identifier, Employee updatedEmployee) {

        Employee employee = this.findEmployee(identifier);

        if (updatedEmployee.getIdentifier() != null && !Objects.equals(updatedEmployee.getIdentifier(), identifier)) {
            throw new IllegalStateException(EmployeeErrorMessages.UPDATE_IDENTIFIER_BANNED.getMessage());
        }

        String firstName = updatedEmployee.getFirstName();
        String middleName = updatedEmployee.getMiddleName();
        String firstSurname = updatedEmployee.getSecondSurname();
        String secondSurname = updatedEmployee.getSecondSurname();
        String email = updatedEmployee.getEmail();
        String address = updatedEmployee.getAddress();
        String phoneNumber = updatedEmployee.getPhoneNumber();
        String vaccinationStatus = updatedEmployee.getVaccinationStatus();
        String typesOfVaccine = updatedEmployee.getTypesOfVaccine();
        Date birthDay = updatedEmployee.getBirthDay();
        Date vaccinationDate = updatedEmployee.getVaccinationDate();
        Integer dosesCount = updatedEmployee.getDosesCount();

        String newFirstName = firstName != null ? firstName : employee.getFirstName();
        String newMiddleName = middleName != null ? middleName : employee.getMiddleName();
        String newFirstSurname = firstSurname != null ? firstSurname : employee.getFirstSurname();
        String newSecondSurname = secondSurname != null ? secondSurname : employee.getSecondSurname();
        String newEmail = email != null ? email : employee.getEmail();
        String newAddress = address != null ? address : employee.getAddress();
        String newPhoneNumber = phoneNumber != null ? phoneNumber : employee.getPhoneNumber();
        Date newBirthDay = birthDay != null ? birthDay : employee.getBirthDay();
        Date newVaccinationDate = vaccinationDate != null ? vaccinationDate : employee.getVaccinationDate();
        Integer newDosesCount = dosesCount != null ? dosesCount : employee.getDosesCount();

        String newVaccinationStatus;
        if (vaccinationStatus != null) {
            this.validateVaccinationStatus(vaccinationStatus);
            newVaccinationStatus = vaccinationStatus;
        }
        else {
            newVaccinationStatus = employee.getVaccinationStatus();
        }

        String newTypeOfVaccine;
        if (typesOfVaccine != null) {
            this.validateTypeOfVaccination(typesOfVaccine);
            newTypeOfVaccine = typesOfVaccine;
        }
        else {
            newTypeOfVaccine = employee.getTypesOfVaccine();
        }

        Employee newEmployee = new Employee(employee.getId(), identifier, newFirstName, newMiddleName, newFirstSurname,
                newSecondSurname, newEmail, newBirthDay, newAddress, newPhoneNumber, newVaccinationStatus,
                newTypeOfVaccine, newVaccinationDate, newDosesCount, employee.getReleased());

        employeeRepository.save(newEmployee);
    }

    public String releaseEmployee(String identifier) {

        Employee employee = this.findEmployee(identifier);

        if (employee.getReleased() != null && employee.getReleased()) {
            throw new IllegalStateException(EmployeeErrorMessages.ALREADY_RELEASED.getMessage());
        }

        String messageResult = keycloakService.generateDefaultUser(employee);
        employee.setReleased(true);
        employeeRepository.save(employee);
        return messageResult;
    }

    @Transactional
    public void updateEmployeeFromUser(String identifier, UpdateDataFromUser updateData) {
        Employee employee =  this.findEmployee(identifier);

        String status = updateData.getVaccinationStatus();
        String vaccine = updateData.getTypesOfVaccine();
        Date vaccinationDate = updateData.getVaccinationDate();
        Integer dosesCount = updateData.getDosesCount();
        if (status != null) {
            this.validateVaccinationStatus(status);
            employee.setVaccinationStatus(status);
            if (status.equals(VaccinationStatus.DONE.getStatus())) {
                this.validateTypeOfVaccination(vaccine);
                employee.setTypesOfVaccine(vaccine);
                this.validateDateAndDoses(vaccinationDate, dosesCount);
                employee.setVaccinationDate(vaccinationDate);
                employee.setDosesCount(dosesCount);
            }
        }


        if (updateData.getAddress() != null ) {
            employee.setAddress(updateData.getAddress());
        }

        if (updateData.getPhoneNumber() != null ) {
            employee.setPhoneNumber(updateData.getPhoneNumber());
        }

        if(updateData.getBirthDay() != null) {
            employee.setBirthDay(updateData.getBirthDay());
        }
    }

    private void validateVaccinationStatus(String vaccinationStatus) {
        if (!this.vaccinationStatus.contains(vaccinationStatus)) {
            throw new IllegalStateException(
                    String.format(EmployeeErrorMessages.BAD_VACCINATION_STATUS.getMessage(),
                            this.vaccinationStatus));
        }
    }

    private void validateTypeOfVaccination(String vaccine) {
        if (!this.typesOfVaccine.contains(vaccine)) {
            throw new IllegalStateException(
                    String.format(EmployeeErrorMessages.BAD_VACCINE_TYPE.getMessage(),
                            this.typesOfVaccine));
        }
    }

    private void validateDateAndDoses(Date vaccinationDate, Integer dosesCount) {
        if (vaccinationDate == null || dosesCount == null) {
            throw new IllegalStateException(
                    EmployeeErrorMessages.BAD_DATE_OR_DOSES.getMessage());
        }
    }
}
