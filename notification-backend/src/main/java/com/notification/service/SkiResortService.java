package com.notification.service;

import com.microsoft.playwright.*;
import com.notification.dto.CreateNotificationRequest;
import com.notification.model.SkiResort;
import com.notification.model.SkiResortInfrastructure;
import com.notification.model.SkiResortLift;
import com.notification.model.SkiResortSlope;
import com.notification.repository.SkiResortLiftRepository;
import com.notification.repository.SkiResortRepository;
import com.notification.repository.SkiResortSlopeRepository;
import com.notification.service.skiResortSites.BergfexSkiResortSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkiResortService
{
	private static final Logger logger = LoggerFactory.getLogger(SkiResortService.class);
	private final SkiResortRepository _skiResortRepository;
	private final SkiResortLiftRepository _skiResortLiftRepository;
	private final SkiResortSlopeRepository _skiResortSlopeRepository;
	private final NotificationService _notificationService;

	public SkiResortService(SkiResortRepository skiResortRepository, SkiResortLiftRepository skiResortLiftRepository, SkiResortSlopeRepository skiResortSlopeRepository, NotificationService notificationService)
	{
		_skiResortRepository = skiResortRepository;
		_skiResortLiftRepository = skiResortLiftRepository;
		_skiResortSlopeRepository = skiResortSlopeRepository;
		_notificationService = notificationService;
	}

	/**
	 * Scrape ski resort status data for all ski resorts
	 * Scheduled to run every hour
	 */
	@Scheduled(cron = "0 0 * * * *") // Every hour at :00
	public void scrapeSkiResortStatusForAllResorts()
	{
		System.out.println("Starting ski resort scraping for all resorts...");
		int successCount = 0;
		int failCount = 0;
		
		try (Playwright playwright = Playwright.create())
		{
			Browser browser = playwright.chromium().launch(
					new BrowserType.LaunchOptions().setHeadless(true)
							.setArgs(java.util.List.of("--ignore-certificate-errors"))
			);
			BrowserContext context = browser.newContext(
				new Browser.NewContextOptions().setIgnoreHTTPSErrors(true)
			);
			Page page = context.newPage();

			List<SkiResort> resorts = _skiResortRepository.findAll();
			System.out.println("Found " + resorts.size() + " resorts to scrape");
			
			for (SkiResort resort : resorts)
			{
				try {
					SkiResortInfrastructure bergfexInfrastructureStatus;
					try
					{
						bergfexInfrastructureStatus = new BergfexSkiResortSite(resort).scrapeAllInfrastructureStatus(page);
					} catch (TimeoutError e)
					{
						System.out.println("Timeout waiting for status table for ski resort: " + resort.getName());
						failCount++;
						continue;
					}

					// Get existing lifts/slopes for this resort only (not all resorts)
					List<SkiResortLift> existingLifts = _skiResortLiftRepository.findBySkiResortIdOrderByCreatedAtDesc(resort.getId());
					List<SkiResortSlope> existingSlopes = _skiResortSlopeRepository.findBySkiResortIdOrderByCreatedAtDesc(resort.getId());

					int newLifts = 0;
					int updatedLifts = 0;
					for (SkiResortLift lift : bergfexInfrastructureStatus.getLiftsWithoutDuplicates())
					{
						SkiResortLift existingLift = existingLifts.stream()
								.filter(l -> l.getName().equalsIgnoreCase(lift.getName()))
								.findFirst()
								.orElse(null);
						if (existingLift != null && existingLift.getIsOpen() != lift.getIsOpen())
						{
							existingLift.setIsOpen(lift.getIsOpen());
							existingLift.setLastStatusChange(lift.getLastStatusChange());
							_skiResortLiftRepository.save(existingLift);
							updatedLifts++;

							CreateNotificationRequest notificationRequest = new CreateNotificationRequest();
							notificationRequest.setTitle("Lift Status Changed at " + resort.getName());
							notificationRequest.setMessage("The lift '" + lift.getName() + "' is now " + (lift.getIsOpen() ? "OPEN" : "CLOSED") + ".");
							_notificationService.createNotification(notificationRequest);

						} else if (existingLift == null)
						{
							_skiResortLiftRepository.save(lift);
							newLifts++;
						}
					}

					int newSlopes = 0;
					int updatedSlopes = 0;
					for (SkiResortSlope slope : bergfexInfrastructureStatus.getSlopes())
					{
						SkiResortSlope existingSlope = existingSlopes.stream()
								.filter(s -> s.getName().equalsIgnoreCase(slope.getName()))
								.findFirst()
								.orElse(null);
						if (existingSlope != null && existingSlope.getIsOpen() != slope.getIsOpen())
						{
							existingSlope.setIsOpen(slope.getIsOpen());
							existingSlope.setLastStatusChange(slope.getLastStatusChange());
							_skiResortSlopeRepository.save(existingSlope);
							updatedSlopes++;

							CreateNotificationRequest notificationRequest = new CreateNotificationRequest();
							notificationRequest.setTitle("Slope Status Changed at " + resort.getName());
							notificationRequest.setMessage("The slope '" + slope.getName() + "' is now " + (slope.getIsOpen() ? "OPEN" : "CLOSED") + ".");
							_notificationService.createNotification(notificationRequest);

						} else if (existingSlope == null)
						{
							_skiResortSlopeRepository.save(slope);
							newSlopes++;
						}
					}

					System.out.println("    " + resort.getName() + ": " + newLifts + " new lifts, " + updatedLifts + " updated lifts, " + newSlopes + " new slopes, " + updatedSlopes + " updated slopes");
					successCount++;
					sleep(2000);
					
				} catch (OutOfMemoryError e) {
					System.err.println("OUT OF MEMORY while scraping " + resort.getName() + " - stopping scrape");
					failCount++;
					break;
				} catch (Exception e) {
					System.err.println("Error scraping " + resort.getName() + ": " + e.getMessage());
					failCount++;
				}
			}

			browser.close();
			System.out.println("Scraping completed: " + successCount + " successful, " + failCount + " failed");
		} catch (OutOfMemoryError e) {
			System.err.println("OUT OF MEMORY during scraping initialization - aborting");
		} catch (Exception e) {
			System.err.println("Fatal error during scraping: " + e.getMessage());
		}
	}

	private static void sleep(long ms) {
		try {
			java.util.concurrent.TimeUnit.MILLISECONDS.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// Get ski resort data

	public List<SkiResort> getAllSkiResorts() {
		return _skiResortRepository.findAll();
	}

	public SkiResort getSkiResortById(Long resortId) {
		return _skiResortRepository
			.findById(resortId)
			.orElseThrow(() ->
				new IllegalArgumentException(
					"Ski resort not found with id: " + resortId
				)
			);
	}

	// Get ski resort lift data

	public List<SkiResortLift> getAllSkiResortLiftData() {
		return _skiResortLiftRepository.findAll();
	}

	public List<SkiResortLift> getSkiResortLiftDataForResort(Long resortId) {
		return _skiResortLiftRepository.findBySkiResortIdOrderByCreatedAtDesc(
			resortId
		);
	}

	// Get ski resort slope data

	public List<SkiResortSlope> getAllSkiResortSlopeData() {
		return _skiResortSlopeRepository.findAll();
	}

	public List<SkiResortSlope> getSkiResortSlopeDataForResort(Long resortId) {
		return _skiResortSlopeRepository.findBySkiResortIdOrderByCreatedAtDesc(
			resortId
		);
	}
}
