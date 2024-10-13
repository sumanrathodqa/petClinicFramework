package com.qa.petClinic.core;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class PlaywrightInit {

	Playwright playwright;
	Browser browser;
	BrowserContext browserContext;
	Page page;
	APIRequestContext apiRequestContext;
	String baseURL;

	// Initialize Playwright for UI testing
	public Page initPlaywright() {
		if (playwright == null) {
			playwright = Playwright.create(); // Initialize Playwright if not already initialized
		}

		browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

		browserContext = browser.newContext();
		page = browserContext.newPage();
		page.navigate("http://localhost:4200/petclinic/welcome");

		return page;
	}

	// Initialize Playwright API request context for API testing
	public APIRequestContext initAPIRequestContext() {
		if (playwright == null) {
			playwright = Playwright.create(); // Initialize Playwright if not already initialized
		}

		// Define the base URL for the API requests
		baseURL = "http://localhost:9966/petclinic/";
		// Create APIRequestContext instance for making HTTP requests
		apiRequestContext = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseURL));
		return apiRequestContext;
	}

	// Getter method for baseURL
	public String getBaseURL() {
		return baseURL;
	}

}
