package com.example.demo.repository;

import com.example.demo.model.domain.subscriber.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber , Long> {
    Optional<Subscriber> findByEmail(String email);
}
