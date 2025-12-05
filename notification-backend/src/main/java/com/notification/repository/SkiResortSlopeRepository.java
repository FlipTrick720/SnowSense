package com.notification.repository;

import com.notification.model.SkiResortSlope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiResortSlopeRepository extends JpaRepository<SkiResortSlope, Long>
{
	List<SkiResortSlope> findBySkiResortIdOrderByCreatedAtDesc(Long skiResortId);
}
