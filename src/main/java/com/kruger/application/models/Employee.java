package com.kruger.application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kruger.application.models.VaccinationEnums.VaccinationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer identifier;
    private String firstName;
    private String middleName;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private Date birthDay;
    private String address;
    private String phoneNumber;
    private String vaccinationStatus;
    private String typesOfVaccine;
    private Date vaccinationDate;
    private Integer dosesCount;
}
