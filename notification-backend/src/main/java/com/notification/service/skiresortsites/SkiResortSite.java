package com.notification.service.skiresortsites;

import com.microsoft.playwright.Page;
import com.notification.model.SkiResortInfrastructure;

public class SkiResortSite
{
	protected final String _url;

	SkiResortSite(String url)
	{
		_url = url;
	}
	public SkiResortInfrastructure scrapeAllInfrastructureStatus(Page page)
	{
		page.navigate(_url);
		return null;
	}
}
