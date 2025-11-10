package com.notification.repository;

import com.notification.model.SkiResort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkiResortRepository extends JpaRepository<SkiResort, Long> {
}
