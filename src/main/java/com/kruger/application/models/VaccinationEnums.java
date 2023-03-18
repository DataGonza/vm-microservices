package com.kruger.application.models;

public interface VaccinationEnums {
    enum VaccinationStatus {
        DONE("vaccinated"),
        NOT_YET("not vaccinated");

        private final String status;

        VaccinationStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    }

    enum TypesOfVaccine {
        SPUTNIK("Sputnik"),
        ASTRAZENECA("AstraZeneca"),
        PFIZER("Pfizer"),
        JOHN_JOHN("Johnson&Johnson"),
        NONE("None");

        private final String vaccine;

        TypesOfVaccine(String status) {
            this.vaccine = status;
        }

        String getVaccine() {
            return this.vaccine;
        }
    }
}
