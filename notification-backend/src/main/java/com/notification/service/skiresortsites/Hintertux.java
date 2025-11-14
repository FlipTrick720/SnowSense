package com.notification.service.skiresortsites;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.notification.model.Lift;
import com.notification.model.SkiResortInfrastructure;

public class Hintertux extends SkiResortSite
{
	public Hintertux()
	{
		super("https://www.hintertuxergletscher.at/en/skiing/skiing-region-current-information/hintertux-lifts/");
	}

	@Override
	public SkiResortInfrastructure scrapeAllInfrastructureStatus(Page page)
	{
		super.scrapeAllInfrastructureStatus(page);

		SkiResortInfrastructure infrastructure = new SkiResortInfrastructure("Hintertux");

		// Wait for a specific element to load
		page.waitForSelector(".anlagen.lifte");

		// Extract the content of the dynamic element
		for (Locator liftLocator : page.locator(".anlagen.lifte").locator(":scope > *").all())
		{
			Locator nameLocator = liftLocator.locator(".anlage-name");
			String name = nameLocator.innerText();

			Locator statusLocator = liftLocator.locator(".anlage-status.live-status");
			String status = statusLocator.getAttribute("aria-label");

			Lift lift = new Lift(name, status.equalsIgnoreCase("open"));
			infrastructure.addLift(lift);
		}

		return infrastructure;
	}
}
