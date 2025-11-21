package com.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ski_resort_facility")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkiResortFacility
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ski_resort_id", nullable = false)
	private SkiResort skiResort;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, name = "facility_type")
	private String facilityType;

	@Column(nullable = false, name = "is_open")
	private Boolean isOpen;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
