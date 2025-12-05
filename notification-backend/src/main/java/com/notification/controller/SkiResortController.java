package com.notification.controller;

import com.notification.model.SkiResortLift;
import com.notification.model.SkiResortSlope;
import com.notification.service.SkiResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skiresort")
public class SkiResortController
{
	private final SkiResortService _skiResortService;

	public SkiResortController(SkiResortService skiResortService)
	{
		_skiResortService = skiResortService;
	}

	/**
	 * Trigger ski resort status data scrapping for all ski resorts
	 * GET /api/skiresort/scrape
	 */
	@GetMapping("/scrape")
	public ResponseEntity<Map<String, String>> scrapeSkiResort()
	{
		_skiResortService.scrapeSkiResortStatusForAllResorts();

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "Ski resort data scraped and saved to database. Check terminal for details.");

		return ResponseEntity.ok(response);
	}

	/**
	 * Get all ski resort lift status data from database
	 * GET /api/skiresort/lifts
	 */
	@GetMapping("/lifts")
	public ResponseEntity<List<SkiResortLift>> getAllSkiResortLiftData()
	{
		return ResponseEntity.ok(_skiResortService.getAllSkiResortLiftData());
	}

	/**
	 * Get all ski resort slope status data from database
	 * GET /api/skiresort/slopes
	 */
	@GetMapping("/slopes")
	public ResponseEntity<List<SkiResortSlope>> getAllSkiResortSlopeData()
	{
		return ResponseEntity.ok(_skiResortService.getAllSkiResortSlopeData());
	}

	/**
	 * Get ski resort lift status data for a specific resort
	 * GET /api/skiresort/resort/{resortId}/lifts
	 */
	@GetMapping("/resort/{resortId}/lifts")
	public ResponseEntity<List<SkiResortLift>> getSkiResortLiftsForResort(@PathVariable Long resortId)
	{
		return ResponseEntity.ok(_skiResortService.getSkiResortLiftDataForResort(resortId));
	}

	/**
	 * Get ski resort slope status data for a specific resort
	 * GET /api/skiresort/resort/{resortId}/slopes
	 */
	@GetMapping("/resort/{resortId}/slopes")
	public ResponseEntity<List<SkiResortSlope>> getSkiResortSlopesForResort(@PathVariable Long resortId)
	{
		return ResponseEntity.ok(_skiResortService.getSkiResortSlopeDataForResort(resortId));
	}
}
