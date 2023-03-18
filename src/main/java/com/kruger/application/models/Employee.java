package com.kruger.application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kruger.application.enums.VaccinationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @NotNull(message = "La cédula de identidad es obligatoria")
    @Pattern(regexp = "^[0-9]{10}$", message = "Necesita proveer un cédula valida de 10 digitos")
    private String identifier;

    @NotNull(message = "El primer nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-z]+$", message = "Los nombres solo pueden contener letras")
    private String firstName;

    @NotNull(message = "El segundo nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-z]+$", message = "Los nombres solo pueden contener letras")
    private String middleName;

    @NotNull(message = "El primer apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-z]+$", message = "Los nombres solo pueden contener letras")
    private String firstSurname;

    @NotNull(message = "El segundo apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-z]+$", message = "Los nombres solo pueden contener letras")
    private String secondSurname;

    @NotNull(message = "El correo es obligatorio")
    @Pattern(regexp = "^\\w+@\\w+[.]\\w+$", message = "Ingrese un correo valido")
    private String email;
    private Date birthDay;
    private String address;
    private String phoneNumber;
    private String vaccinationStatus;
    private String typesOfVaccine;
    private Date vaccinationDate;
    private Integer dosesCount;
    private Boolean released;
}
