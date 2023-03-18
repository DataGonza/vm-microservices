package com.kruger.application.enums;

public enum TypesOfVaccine {

    SPUTNIK("Sputnik"),
    ASTRAZENECA("AstraZeneca"),
    PFIZER("Pfizer"),
    JOHN_JOHN("Johnson&Johnson");

    private final String vaccine;

    TypesOfVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getVaccine() {
        return this.vaccine;
    }
}
