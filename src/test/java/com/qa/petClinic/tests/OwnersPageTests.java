package com.qa.petClinic.tests;

import org.testng.Assert;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.microsoft.playwright.Page;
import com.qa.petClinic.core.PlaywrightInit;
import com.qa.petClinic.pages.HomePage;
import com.qa.petClinic.pages.OwnersPage;
import com.qa.petClinic.utils.CommonFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.qa.petClinic.listener.ExtentReportListener;

@Listeners(ExtentReportListener.class)

public class OwnersPageTests {

	PlaywrightInit pwInit;
	Page page;
	HomePage homePage;
	OwnersPage ownersPage;
	CommonFunctions commonFunction;
	Properties properties;
	
	@BeforeMethod
	public void setup() {
		pwInit = new PlaywrightInit();
		page = pwInit.initPlaywright();
		homePage = new HomePage(page);
		commonFunction = new CommonFunctions(page);
		ownersPage = new OwnersPage(page);

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
	//("Verify that a new owner can be successfully created with valid data")
	public void addNewOwnerFromUi() {
		homePage.navigateToAddNewOwnerPage();
		String actualUrl = commonFunction.getPageURL();
		String addNewOwnersPage = properties.getProperty("addNewOwnersPage.url");
		System.out.println("actual Url: " + actualUrl);
		System.out.println("expected Url: " + addNewOwnersPage);
		Assert.assertEquals(actualUrl, addNewOwnersPage);
		String newOwnerName = ownersPage.enterValidNewOwnerDetails();
		System.out.println("New Owner name: " + newOwnerName);
		ownersPage.submitNewOwnerDetails();
		String navigationAfterSubmission = commonFunction.getPageURL();
		System.out.println("new Url after new owner submission: " + navigationAfterSubmission);
		String ownersListPage = properties.getProperty("ownersListPage.url");
		System.out.println("expected Url after new owner submission: " + ownersListPage);
		Assert.assertEquals(navigationAfterSubmission, ownersListPage);
		boolean newlyAddedOwnerExists = ownersPage.verifyNewOwnerExists(newOwnerName);
        Assert.assertTrue(newlyAddedOwnerExists, "The new owner should exist in the owners list but was not found.");
	}
	
	@Test
	//("Check if invalid name throws error")
	public void invalidNameAdditionThrowsErrorFromUi() {
		homePage.navigateToAddNewOwnerPage();
		String newOwnerName = ownersPage.enterInvalidValidOwnerName();
		boolean invalidUserAdditionFails = ownersPage.verifyInvalidUserNameIsnotSubmitted(newOwnerName);
        Assert.assertTrue(invalidUserAdditionFails, "The submit button should be inactive for invalid first name however was active");
        boolean errorMessageIsDisplayed=  ownersPage.verifyErrorMessageIsDisplayed();
        Assert.assertTrue(errorMessageIsDisplayed, "Error message for first name validation is not displayed.");
	}
	

	@AfterMethod
	public void tearDown() {
		if (page != null && page.context() != null) {
			page.context().browser().close();
		}
	}

}
