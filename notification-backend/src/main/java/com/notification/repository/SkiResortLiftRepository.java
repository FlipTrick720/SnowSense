package com.notification.repository;

import com.notification.model.SkiResortLift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiResortLiftRepository extends JpaRepository<SkiResortLift, Long>
{
	List<SkiResortLift> findBySkiResortIdOrderByCreatedAtDesc(Long skiResortId);
}
