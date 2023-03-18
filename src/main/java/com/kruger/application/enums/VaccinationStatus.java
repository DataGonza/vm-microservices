package com.kruger.application.enums;

public enum VaccinationStatus {
    DONE("vacunado"),
    NOT_YET("sin vacunar");

    private final String status;

    VaccinationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
