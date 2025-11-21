package com.notification.repository;

import com.notification.model.SkiResortFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkiResortFacilityRepository extends JpaRepository<SkiResortFacility, Long>
{
}
