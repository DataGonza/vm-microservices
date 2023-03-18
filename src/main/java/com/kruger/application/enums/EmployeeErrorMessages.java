package com.kruger.application.enums;

public enum EmployeeErrorMessages {

    NOT_FOUND_MESSAGE("El empleado con cédula: %s no se encuentra en la base de datos"),
    ALREADY_EXIST("El empleado con cédula: %s ya se encuentra en la base de datos"),
    UPDATE_IDENTIFIER_BANNED("Está prohibido actualizar la cédula de identidad"),
    ALREADY_RELEASED("El ya fue empleado fue dado de alta, puede ver sus datos con su" +
            " usuario y contraseña (Numero de cédula)"),
    BAD_VACCINATION_STATUS("El estado de vacunación solo puede ser: %s"),
    BAD_VACCINE_TYPE("El tipo de vacuna es erroneo, solo puede ser: %s"),
    BAD_DATE_OR_DOSES("La fecha de vacunación y/o el número de dosis es(son) incorrecto(s)");

    private final String message;

    EmployeeErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
