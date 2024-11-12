package com.workshop.notification.domain.model.entities;

import com.workshop.notification.domain.model.valueobjects.Contact;
import com.workshop.notification.domain.model.valueobjects.enums.RecipientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "outbox_event")
public class Recipient {

    @Id
    private ObjectId recipientId;

    @NotNull(message = "Recipient type is required")
    private RecipientType recipientType;

    @NotNull(message = "Contact information is required")
    private Contact contact;
}
