package com.workshop.notification.domain.repository;

import com.workshop.notification.domain.model.aggregates.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCommandRepository extends ReactiveMongoRepository<Notification, ObjectId> {
}
