package com.notification.service;

import com.microsoft.playwright.*;
import com.notification.model.*;
import com.notification.repository.SkiResortFacilityRepository;
import com.notification.repository.SkiResortRepository;
import com.notification.service.skiresortsites.Bergfex;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkiResortService
{
	private final SkiResortRepository skiResortRepository;
	private final SkiResortFacilityRepository skiResortFacilityRepository;

	public SkiResortService(SkiResortRepository skiResortRepository, SkiResortFacilityRepository skiResortFacilityRepository)
	{
		this.skiResortRepository = skiResortRepository;
		this.skiResortFacilityRepository = skiResortFacilityRepository;
	}

	/**
	 * Scrape weather data for all ski resorts
	 * Scheduled to run every hour at minute 0
	 */
	//@Scheduled(cron = "0 0 * * * *")  // Every hour at :00
	@Scheduled(cron = "0 */10 * * * *")
	public void scrapeSkiResortStatusForAllResorts()
	{
		try (Playwright playwright = Playwright.create())
		{
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
			Page page = browser.newPage();

			List<SkiResort> resorts = skiResortRepository.findAll();
			int i = 0;
			for (SkiResort resort : resorts)
			{
				System.out.println("Scraping infrastructure status for: " + resort.getName());
				SkiResortInfrastructure bergfexInfrastructureStatus = new Bergfex(resort.getBergfexName()).scrapeAllInfrastructureStatus(page);

				ArrayList<SkiResortFacility> facilities = convertInfrastructureToFacilities(resort, bergfexInfrastructureStatus);
				skiResortFacilityRepository.saveAll(facilities);

				i ++;
				if (i >= 1)
				{
					break;
				}
			}

			// Close the browser
			browser.close();
		}
	}

	private ArrayList<SkiResortFacility> convertInfrastructureToFacilities(SkiResort resort, SkiResortInfrastructure infrastructure)
	{
		ArrayList<SkiResortFacility> facilities = new ArrayList<>();

		for (Lift lift : infrastructure.getLifts())
		{
			SkiResortFacility facility = new SkiResortFacility();
			facility.setSkiResort(resort);
			facility.setName(lift.getName());
			facility.setFacilityType("Lift");
			facility.setIsOpen(lift.isOpen());
			facilities.add(facility);
		}

		for (Slope slope : infrastructure.getSlopes())
		{
			SkiResortFacility facility = new SkiResortFacility();
			facility.setSkiResort(resort);
			facility.setName(slope.getName());
			facility.setFacilityType("Slope");
			facility.setIsOpen(slope.isOpen());
			facilities.add(facility);
		}

		return facilities;
	}
}
