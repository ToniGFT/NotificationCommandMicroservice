package com.workshop.notification.infraestructure.Vehicle.model.entities;

import com.workshop.notification.infraestructure.Vehicle.model.valueobjects.Contact;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import jakarta.validation.constraints.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

    private ObjectId driverId;

    @NotBlank(message = "Driver name cannot be empty")
    private String name;

    @Valid
    private Contact contact;
}
