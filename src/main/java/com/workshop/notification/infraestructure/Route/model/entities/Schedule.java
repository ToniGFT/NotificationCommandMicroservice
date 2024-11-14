package com.workshop.notification.infraestructure.Route.model.entities;

import com.workshop.notification.infraestructure.Route.model.valueobjects.WeekSchedule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @NotNull(message = "El horario entre semana no puede ser nulo")
    @Valid
    private WeekSchedule weekdays;

    @NotNull(message = "El horario de fin de semana no puede ser nulo")
    @Valid
    private WeekSchedule weekends;

}

