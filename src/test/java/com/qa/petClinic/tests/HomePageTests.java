package com.qa.petClinic.tests;

import org.testng.annotations.Test;

import org.testng.Assert;
import org.testng.annotations.*;
import com.microsoft.playwright.Page;
import com.qa.petClinic.core.PlaywrightInit;
import com.qa.petClinic.pages.HomePage;
import com.qa.petClinic.utils.CommonFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.annotations.Listeners;
import com.qa.petClinic.listener.ExtentReportListener;

@Listeners(ExtentReportListener.class)

public class HomePageTests {

	PlaywrightInit pwInit;
	Page page;
	HomePage homePage;
	CommonFunctions commonFunction;
	Properties properties;

	@BeforeMethod
	public void setup() {
		pwInit = new PlaywrightInit();
		page = pwInit.initPlaywright();
		homePage = new HomePage(page);
		commonFunction = new CommonFunctions(page);

		// Load properties file
		properties = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("test-data.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find test-data.properties");
				return;
			}
			// Load the properties file
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	// ("Check if correct title of the home page is displayed")
	public void homePageTitleCheck() {
		String actualTitle = homePage.checkPageTitle();
		String expectedTitle = properties.getProperty("homePage.title");
		Assert.assertEquals(actualTitle, expectedTitle);
		System.out.println("actual Title: " + actualTitle);
		System.out.println("expected Title: " + expectedTitle);
	}

	@Test
	// ("Check if the url of home page is valid")
	public void homePageUrlCheck() {
		String actualUrl = commonFunction.getPageURL();
		String expectedUrl = properties.getProperty("homePage.url");
		Assert.assertEquals(actualUrl, expectedUrl);
		System.out.println("actual Url: " + actualUrl);
		System.out.println("expected Url: " + expectedUrl);

	}

	@Test
	// ("Check if the navigation to Owners page works from Home page")
	public void checkNavigateToOwnersPage() {
		homePage.navigateToOwnersPage();
		String actualUrl = commonFunction.getPageURL();
		String ownersPageUrl = properties.getProperty("ownersListPage.url");
		Assert.assertEquals(actualUrl, ownersPageUrl);
		System.out.println("actual Url: " + actualUrl);
		System.out.println("ownersPageUrl: " + ownersPageUrl);
	}

	@AfterMethod
	public void tearDown() {
		if (page != null && page.context() != null) {
			page.context().browser().close();
		}
	}

}
