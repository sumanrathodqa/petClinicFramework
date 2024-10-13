package com.qa.petClinic.tests;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.qa.petClinic.core.PlaywrightInit;
import com.qa.petClinic.pages.HomePage;
import com.qa.petClinic.pages.OwnersPage;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.qa.petClinic.listener.ExtentReportListener;

@Listeners(ExtentReportListener.class)

public class OwnersEndpointApiWithUITests {
	PlaywrightInit pwInit;
	APIRequestContext apiRequestContext;
	Gson gson = new Gson();
	HomePage homePage;
	OwnersPage ownersPage;
	Page page;

	@BeforeTest
	public void setup() {
		pwInit = new PlaywrightInit();
		page = pwInit.initPlaywright();
		apiRequestContext = pwInit.initAPIRequestContext();
		homePage = new HomePage(page);
		ownersPage = new OwnersPage(page);

	}

	// Owners class to map JSON response
	public static class Owners {
		public int id;
		public String firstName;
		public String lastName;
		public String address;
		public String city;
		public String telephone;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

	}

	@Test
	//("Check API status and response for owners endpoint")
	//("Send a GET request to the /owners endpoint and validate response")
	public void ownersApiStatusCheck() {
		// Perform GET request to the API endpoint
		APIResponse response = apiRequestContext.get("api/owners");

		String requestedUrl = response.url();
		System.out.println("Requested URL: " + requestedUrl);

		// Validate the response status code
		int statusCode = response.status();
		Assert.assertEquals(statusCode, 200, "Expected status code 200, but got " + statusCode);

		// Parse JSON response into a list of objects
		String responseBody = response.text();
		Gson gson = new Gson();
		Owners[] ownersArray = gson.fromJson(responseBody, Owners[].class);
		Assert.assertTrue(ownersArray.length > 0, "No owners found in the response");
		System.out.println("Number of pet owners found: " + ownersArray.length);
	}

	@Test
	//("Create and Delete pet owner")
	//("Send a DELETE request to remove a owner's name")
	public void createOwnerFromUiAndDeleteOwnerByIdFromApi() {
		// creating owner from UI
		homePage.navigateToAddNewOwnerPage();
		String newOwnerName = ownersPage.enterValidNewOwnerDetails();
		ownersPage.submitNewOwnerDetails();

		// checking user from API -- Perform GET request to the API endpoint
		APIResponse response = apiRequestContext.get("api/owners");

		String requestedUrl = response.url();
		System.out.println("Requested URL: " + requestedUrl);

		// Validate the response status code
		int statusCode = response.status();
		Assert.assertEquals(statusCode, 200, "Expected status code 200, but got " + statusCode);

		// Parse JSON response into an array of Owners objects
		String responseBody = response.text();
		Gson gson = new Gson();
		Owners[] ownersArray = gson.fromJson(responseBody, Owners[].class);
		Assert.assertTrue(ownersArray.length > 0, "No owners found in the response");
		System.out.println("Number of owners found: " + ownersArray.length);

		// Search for the specific owner by first name and retrieve the ID
		Owners foundOwner = null;
		for (Owners owner : ownersArray) {
			if (owner.getFirstName().equals(newOwnerName)) {
				foundOwner = owner;
				break;
			}
		}

		// Assert that the owner was found
		Assert.assertNotNull(foundOwner, "Owner with first name '" + newOwnerName + "' was not found.");
		System.out.println("Found owner: " + foundOwner.getFirstName() + ", ID: " + foundOwner.getId());

		// delete the owner

		// Construct the relative and full URL
		String relativeUrl = "api/owners/" + foundOwner.getId();
		String fullUrl = pwInit.getBaseURL() + relativeUrl;
		System.out.println("Full URL: " + fullUrl); // Log the full URL

		// Make the Delete request to remove the owner
		APIResponse responseFromDeleteRequest = apiRequestContext.delete(fullUrl);

		// Assert the response status code
		int statusCodeFromDeleteRequest = responseFromDeleteRequest.status();
		Assert.assertEquals(statusCode, 200, "Expected status code 200 for successful update.");

		if (statusCodeFromDeleteRequest == 200) {
			System.out.println(foundOwner.getFirstName() + "Owner deleted successfully!");
		} else {
			System.out.println("Error deleting owner: Status code " + statusCodeFromDeleteRequest);
		}

	}

	@AfterTest
	public void tearDown() {
		if (apiRequestContext != null) {
			apiRequestContext.dispose(); // Clean up the APIRequestContext
		}
	}
}