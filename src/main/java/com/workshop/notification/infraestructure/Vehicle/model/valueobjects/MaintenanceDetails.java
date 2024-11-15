package com.workshop.notification.infraestructure.Vehicle.model.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceDetails {

    @NotBlank(message = "Scheduled by field cannot be empty")
    private String scheduledBy;

    @NotNull(message = "Scheduled date is required")
    @FutureOrPresent(message = "Scheduled date must be today or in the future")
    private LocalDate scheduledDate;

    private String details;
}
