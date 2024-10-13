package com.qa.petClinic.tests;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.qa.petClinic.core.PlaywrightInit;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.qa.petClinic.listener.ExtentReportListener;

@Listeners(ExtentReportListener.class)

public class PetTypesEndpointApiTests {
	PlaywrightInit pwInit;
	APIRequestContext apiRequestContext;
	private PetType[] petTypes;

	@BeforeTest
	public void setup() {
		pwInit = new PlaywrightInit();
		apiRequestContext = pwInit.initAPIRequestContext();

	}

	// PetType class to map JSON response
	public static class PetType {
		public int id;
		public String name;
	}

	@Test
	// ("Check API status and response for pet types")
	// ("Send a GET request to the /pettypes endpoint and validate response")
	public void petTypesApiStatusCheck() {
		// Perform GET request to the API endpoint
		APIResponse response = apiRequestContext.get("api/pettypes");

		// Retrieve and print the requested URL
		String requestedUrl = response.url();
		System.out.println("Requested URL: " + requestedUrl);

		// Validate the response status code
		int statusCode = response.status();
		Assert.assertEquals(statusCode, 200, "Expected status code 200, but got " + statusCode);

		// Parse JSON response into a list of PetType objects
		String responseBody = response.text();
		Gson gson = new Gson();
		PetType[] petTypesArray = gson.fromJson(responseBody, PetType[].class);

		// Validate that the response contains expected data (at least 1 pet type) and
		// print the number
		Assert.assertTrue(petTypesArray.length > 0, "No pet types found in the response");
		System.out.println("Number of pet types: " + petTypesArray.length);
	}

	// Method to get a PetType by ID
	public PetType getPetTypeById(int id) {
		for (PetType petType : petTypes) {
			if (petType.id == id) {
				return petType;
			}
		}
		return null;
	}

	// Method to get a PetType by Name
	public PetType getPetTypeByName(String name) {
		for (PetType petType : petTypes) {
			if (petType.name.equalsIgnoreCase(name)) {
				return petType;
			}
		}
		return null;
	}

	@AfterTest
	public void tearDown() {
		if (apiRequestContext != null) {
			apiRequestContext.dispose();
		}
	}
}