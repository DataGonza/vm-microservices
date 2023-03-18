package com.kruger.application.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDataFromUser {
    private Date birthDay;
    private String address;
    private String phoneNumber;
    private String vaccinationStatus;
    private String typesOfVaccine;
    private Date vaccinationDate;
    private Integer dosesCount;
    private Boolean released;
}
