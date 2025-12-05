package com.notification.service;

import com.microsoft.playwright.*;
import com.notification.model.SkiResort;
import com.notification.model.SkiResortInfrastructure;
import com.notification.model.SkiResortLift;
import com.notification.model.SkiResortSlope;
import com.notification.repository.SkiResortLiftRepository;
import com.notification.repository.SkiResortRepository;
import com.notification.repository.SkiResortSlopeRepository;
import com.notification.service.skiResortSites.BergfexSkiResortSite;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkiResortService
{
	private final SkiResortRepository _skiResortRepository;
	private final SkiResortLiftRepository _skiResortLiftRepository;
	private final SkiResortSlopeRepository _skiResortSlopeRepository;

	public SkiResortService(SkiResortRepository skiResortRepository, SkiResortLiftRepository skiResortLiftRepository, SkiResortSlopeRepository skiResortSlopeRepository)
	{
		_skiResortRepository = skiResortRepository;
		_skiResortLiftRepository = skiResortLiftRepository;
		_skiResortSlopeRepository = skiResortSlopeRepository;
	}

	/**
	 * Scrape ski resort status data for all ski resorts
	 * Scheduled to run every hour
	 */
	@Scheduled(cron = "0 0 * * * *")  // Every hour at :00
	@EventListener(ApplicationReadyEvent.class)
	public void scrapeSkiResortStatusForAllResorts()
	{
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
			for (SkiResort resort : resorts)
			{
				SkiResortInfrastructure bergfexInfrastructureStatus;
				try
				{
					bergfexInfrastructureStatus = new BergfexSkiResortSite(resort).scrapeAllInfrastructureStatus(page);
				} catch (TimeoutError e)
				{
					System.out.println("Timeout waiting for status table for ski resort: " + resort.getName());
					continue;
				}

				for (SkiResortLift lift : bergfexInfrastructureStatus.getLifts())
				{
					SkiResortLift existingLift = _skiResortLiftRepository.findAll().stream()
							.filter(l -> l.getSkiResort().getId().equals(resort.getId()))
							.filter(l -> l.getName().equalsIgnoreCase(lift.getName()))
							.findFirst()
							.orElse(null);
					if (existingLift != null && existingLift.getIsOpen() != lift.getIsOpen())
					{
						existingLift.setIsOpen(lift.getIsOpen());
						existingLift.setLastStatusChange(lift.getLastStatusChange());
						_skiResortLiftRepository.save(existingLift);
					} else if (existingLift == null)
					{
						_skiResortLiftRepository.save(lift);
					}
				}

				for (SkiResortSlope slope : bergfexInfrastructureStatus.getSlopes())
				{
					SkiResortSlope existingSlope = _skiResortSlopeRepository.findAll().stream()
							.filter(s -> s.getSkiResort().getId().equals(resort.getId()))
							.filter(s -> s.getName().equalsIgnoreCase(slope.getName()))
							.findFirst()
							.orElse(null);
					if (existingSlope != null && existingSlope.getIsOpen() != existingSlope.getIsOpen())
					{
						existingSlope.setIsOpen(slope.getIsOpen());
						existingSlope.setLastStatusChange(slope.getLastStatusChange());
						_skiResortSlopeRepository.save(existingSlope);
					} else if (existingSlope == null)
					{
						_skiResortSlopeRepository.save(slope);
					}
				}

				sleep(2000);
			}

			browser.close();
		}
	}

	private static void sleep(long ms)
	{
		try
		{
			java.util.concurrent.TimeUnit.MILLISECONDS.sleep(ms);
		} catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	public List<SkiResortLift> getAllSkiResortLiftData()
	{
		return _skiResortLiftRepository.findAll();
	}

	public List<SkiResortLift> getSkiResortLiftDataForResort(Long resortId)
	{
		return _skiResortLiftRepository.findBySkiResortIdOrderByCreatedAtDesc(resortId);
	}

	public List<SkiResortSlope> getAllSkiResortSlopeData()
	{
		return _skiResortSlopeRepository.findAll();
	}

	public List<SkiResortSlope> getSkiResortSlopeDataForResort(Long resortId)
	{
		return _skiResortSlopeRepository.findBySkiResortIdOrderByCreatedAtDesc(resortId);
	}
}
