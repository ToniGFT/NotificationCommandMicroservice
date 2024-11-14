package com.workshop.notification.infraestructure.Route.model.aggregates;


import com.workshop.notification.infraestructure.Route.model.entities.Schedule;
import com.workshop.notification.infraestructure.Route.model.entities.Stop;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Route")
public class Route {

    @Id
    private ObjectId routeId;

    @NotEmpty(message = "El nombre de la ruta no puede estar vacío")
    private String routeName;

    @NotEmpty(message = "Debe haber al menos una parada en la ruta")
    private List<@Valid Stop> stops;

    @NotNull(message = "El horario no puede ser nulo")
    @Valid
    private Schedule schedule;

}


