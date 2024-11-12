package com.workshop.notification.domain.model.entities;

import com.workshop.notification.domain.model.valueobjects.enums.ActionStatus;
import com.workshop.notification.domain.model.valueobjects.enums.ActionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Action {

    @Id
    private ObjectId actionId;

    @NotNull(message = "Action type cannot be null")
    private ActionType type;

    @NotNull(message = "Action status cannot be null")
    private ActionStatus status;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

    @Size(max = 255, message = "Error message cannot exceed 255 characters")
    private String errormessage;

}
