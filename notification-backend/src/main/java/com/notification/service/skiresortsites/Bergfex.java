package com.notification.service.skiresortsites;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.notification.model.Lift;
import com.notification.model.SkiResortInfrastructure;
import com.notification.model.Slope;

import java.util.List;

public class Bergfex extends SkiResortSite
{
	public Bergfex(String berfexName)
	{
		super("https://www.bergfex.com/" + berfexName + "/schneebericht/");
	}

	@Override
	public SkiResortInfrastructure scrapeAllInfrastructureStatus(Page page)
	{
		super.scrapeAllInfrastructureStatus(page);

		SkiResortInfrastructure infrastructure = new SkiResortInfrastructure("Hintertux");

		page.waitForSelector(".status-table");

		Locator liftLocator = page.locator(".status-table").first();
		List<Locator> rows = liftLocator.locator("tbody tr").all();
		for (Locator row : rows)
		{
			String name = row.locator("td").all().get(2).innerText().trim();
			boolean isOpen = row.locator("td.lifte-icon-status i[class*='icon-status']").first().getAttribute("class").contains("icon-status1");

			Lift lift = new Lift(name, isOpen);
			infrastructure.addLift(lift);
		}

		Locator slopesLocator = page.locator(".status-table").last();
		rows = slopesLocator.locator("tbody tr").all();
		for (Locator row : rows)
		{
			String name = row.locator("td").all().get(2).innerText().trim();
			boolean isOpen = row.locator("td.pisten-icon-status i[class*='icon-status']").first().getAttribute("class").contains("icon-status1");

			Slope slope = new Slope(name, isOpen);
			infrastructure.addSlope(slope);
		}

		return infrastructure;
	}
}
