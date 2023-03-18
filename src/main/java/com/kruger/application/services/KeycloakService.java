package com.kruger.application.services;

import com.kruger.application.enums.EmployeeErrorMessages;
import com.kruger.application.models.Employee;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {

    private final Keycloak keycloak;

    public KeycloakService() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180")
                .realm("kruger")
                .clientId("vm-microservices")
                .username("dgonzalez")
                .password("admin")
                .build();
    }

    public String generateDefaultUser(Employee employee) {

        String identifier = employee.getIdentifier();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(identifier);
        user.setFirstName(employee.getFirstName());
        user.setLastName(employee.getFirstSurname());
        user.setEmail(employee.getEmail());
        user.setRealmRoles(List.of("user"));
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(identifier);

        user.setCredentials(Collections.singletonList(credential));

        Response response = keycloak.realm("kruger").users().create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);

        RealmResource realmResource = keycloak.realm("kruger");
        RoleRepresentation role = realmResource.roles().get("user").toRepresentation();
        realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(role));

        return String.format(
                "El empleado se dio de alta exitosamente, puede ver sus datos con usuario y contraseña %s", identifier);

    }
    public void deleteEmployee(String identifier) {
        UsersResource usersResource = keycloak.realm("kruger").users();
        List<UserRepresentation> userRepresentations = usersResource.search(identifier);
        if (userRepresentations.isEmpty()) {
            throw new IllegalStateException(
                    String.format(EmployeeErrorMessages.NOT_FOUND_MESSAGE.getMessage(), identifier));
        }
        UserRepresentation userToDelete = userRepresentations.get(0);
        usersResource.get(userToDelete.getId()).remove();
    }
}
