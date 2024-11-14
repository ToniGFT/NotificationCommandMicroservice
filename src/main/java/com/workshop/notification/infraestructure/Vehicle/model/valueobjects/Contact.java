package com.workshop.notification.infraestructure.Vehicle.model.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @Email(message = "Email must be in a valid format")
    private String email;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number must be in a valid format")
    private String phone;
}
