package com.notification.service;

import com.microsoft.playwright.*;
import com.notification.model.SkiResortInfrastructure;
import com.notification.service.skiresortsites.Bergfex;
import com.notification.service.skiresortsites.Hintertux;

public class SkiResortService
{
	public SkiResortService()
	{

	}

	public void run()
	{
		try (Playwright playwright = Playwright.create())
		{
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
			Page page = browser.newPage();

			//SkiResortInfrastructure hintertuxInfrastructureStatus = new Hintertux().scrapeAllInfrastructureStatus(page);
			//System.out.println(hintertuxInfrastructureStatus);

			SkiResortInfrastructure bergfexInfrastructureStatus = new Bergfex("https://www.bergfex.com/hintertux/schneebericht/").scrapeAllInfrastructureStatus(page);
			System.out.println(bergfexInfrastructureStatus);

			bergfexInfrastructureStatus = new Bergfex("https://www.bergfex.pl/stubaier-gletscher/schneebericht/").scrapeAllInfrastructureStatus(page);
			System.out.println(bergfexInfrastructureStatus);

			bergfexInfrastructureStatus = new Bergfex("https://www.bergfex.com/soelden/schneebericht/").scrapeAllInfrastructureStatus(page);
			System.out.println(bergfexInfrastructureStatus);

			bergfexInfrastructureStatus = new Bergfex("https://www.bergfex.at/pitztalergletscher/schneebericht/").scrapeAllInfrastructureStatus(page);
			System.out.println(bergfexInfrastructureStatus);

			bergfexInfrastructureStatus = new Bergfex("https://www.bergfex.at/kaunertal/schneebericht/").scrapeAllInfrastructureStatus(page);
			System.out.println(bergfexInfrastructureStatus);

			// Close the browser
			browser.close();
		}
	}
}
