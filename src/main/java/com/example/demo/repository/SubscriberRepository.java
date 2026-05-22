package com.example.demo.repository;

import com.example.demo.model.domain.subscriber.Subscriber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber , Long> {

    @Query(value = """
    select s
    from Subscriber s
    left join fetch s.skills
    where s.email = :email
    """)
    Optional<Subscriber> findByEmail(@Param("email") String email);

    @Query("""
    SELECT s.email
    FROM Subscriber s
    WHERE NOT EXISTS (
        SELECT 1
        FROM EmailTask et
        WHERE et.email = s.email
          AND et.sendDate = CURRENT_DATE
    )
    """)
    Page<String> findEmailSubscribersNotSentToday(Pageable pageable);

}
