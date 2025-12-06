package com.notification.service.skiResortSites;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.notification.model.SkiResort;
import com.notification.model.SkiResortInfrastructure;
import com.notification.model.SkiResortLift;
import com.notification.model.SkiResortSlope;

import java.time.LocalDateTime;
import java.util.List;

public class BergfexSkiResortSite extends SkiResortSite
{
	private final SkiResort _skiResort;

	public BergfexSkiResortSite(SkiResort skiResort)
	{
		super("https://www.bergfex.com/" + skiResort.getBergfexName() + "/schneebericht/");

		_skiResort = skiResort;
	}

	@Override
	public SkiResortInfrastructure scrapeAllInfrastructureStatus(Page page)
	{
		super.scrapeAllInfrastructureStatus(page);

		LocalDateTime now = LocalDateTime.now();
		SkiResortInfrastructure infrastructure = new SkiResortInfrastructure(_skiResort);

		page.waitForSelector(".status-table");

		Locator liftLocator = page.locator(".status-table").first();
		List<Locator> rows = liftLocator.locator("tbody tr").all();
		for (Locator row : rows)
		{
			String name = row.locator("td").all().get(2).innerText().trim();
			String type = row.locator("td").all().get(3).innerText().trim();
			String lengthInMeters = row.locator("td").all().get(4).innerText()
					.replace("m", "")
					.replace(",", "")
					.replace("-", "")
					.trim();
			int lengthInMetersInt = lengthInMeters.isEmpty() ? 0 : Integer.parseInt(lengthInMeters);
			boolean isOpen = row.locator("td.lifte-icon-status i[class*='icon-status']").first().getAttribute("class").contains("icon-status1");

			SkiResortLift lift = SkiResortLift.builder()
					.skiResort(_skiResort)
					.name(name)
					.type(type)
					.lengthInMeters(lengthInMetersInt)
					.isOpen(isOpen)
					.lastStatusChange(now)
					.build();

			infrastructure.addLift(lift);
		}

		Locator slopesLocator = page.locator(".status-table").last();
		rows = slopesLocator.locator("tbody tr").all();
		for (Locator row : rows)
		{
			String name = row.locator("td").all().get(2).innerText().trim();
			String difficultyLevel = row.locator("td.pisten-icon-status i[class*='icon-pisten']").first().getAttribute("title").trim();
			boolean isOpen = row.locator("td.pisten-icon-status i[class*='icon-status']").first().getAttribute("class").contains("icon-status1");

			SkiResortSlope slope = SkiResortSlope.builder()
					.skiResort(_skiResort)
					.name(name)
					.difficultyLevel(difficultyLevel)
					.isOpen(isOpen)
					.lastStatusChange(now)
					.build();

			infrastructure.addSlope(slope);
		}

		return infrastructure;
	}
}
