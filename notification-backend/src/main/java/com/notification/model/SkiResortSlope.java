package com.notification.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ski_resort_slope")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SkiResortSlope
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ski_resort_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private SkiResort skiResort;

	@Column(nullable = false)
	private String name;

	@Column(name = "difficulty_level")
	private String difficultyLevel;

	@Column(nullable = false, name = "is_open")
	private Boolean isOpen;

	@Column(name = "last_status_change")
	private LocalDateTime lastStatusChange;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate()
	{
		createdAt = LocalDateTime.now();
		lastStatusChange = createdAt;
	}
}
