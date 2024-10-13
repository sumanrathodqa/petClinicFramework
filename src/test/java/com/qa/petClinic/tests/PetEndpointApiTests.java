package com.qa.petClinic.tests;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.qa.petClinic.core.PlaywrightInit;
import com.qa.petClinic.tests.PetTypesEndpointApiTests.PetType;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.qa.petClinic.listener.ExtentReportListener;

@Listeners(ExtentReportListener.class)

public class PetEndpointApiTests {
	PlaywrightInit pwInit;
	APIRequestContext apiRequestContext;
	Gson gson = new Gson();


	@BeforeTest
	public void setup() {
		pwInit = new PlaywrightInit();
		apiRequestContext = pwInit.initAPIRequestContext();

	}

	// Pet class to map JSON response
	public static class Pets {
		public int id;
		public String name;
		public String birthDate;
		public PetType type;

	}

	@Test
	//("Check API status and response for pet endpoint")
	//("Send a GET request to the /pet endpoint and validate response")
	public void petsApiStatusCheck() {
		// Perform GET request to the API endpoint
		APIResponse response = apiRequestContext.get("api/pets");

		String requestedUrl = response.url();
		System.out.println("Requested URL: " + requestedUrl);

		// Validate the response status code
		int statusCode = response.status();
		Assert.assertEquals(statusCode, 200, "Expected status code 200, but got " + statusCode);

		// Parse JSON response into a list of PetType objects
		String responseBody = response.text();
		Gson gson = new Gson();
		Pets[] petTypesArray = gson.fromJson(responseBody, Pets[].class);
		Assert.assertTrue(petTypesArray.length > 0, "No pet types found in the response");
		System.out.println("Number of pets found: " + petTypesArray.length);

	}

	@Test
	//("Get a pet by ID")
	//("Send a GET request to retrieve a pet by ID")
	public void getPetNameByIdFromApi() {
		int petId = 1;
		APIResponse response = apiRequestContext.get("api/pets/" + petId);
		Assert.assertEquals(response.status(), 200, "Expected status code 200 for getting pet by ID.");
		Pets pet = gson.fromJson(response.text(), Pets.class);
		Assert.assertNotNull(pet, "The response should not be null");
		System.out.println("Retrieved Pet: " + pet.name);
	}

	@Test
	//("Update a pet's name by ID")
	//("Send a PUT request to update a pet's name")
	public void updatePetNameByIdFromApi() {

		// Initialize API request context with base URL using PlaywrightInit
		APIRequestContext apiRequestContext = pwInit.initAPIRequestContext();

		int petId = 1; // ID of the pet to update
		String petName = "Leon" + Math.random();

		String updatedPetData = "{" + "\"name\": \"" + petName + "\"," + "\"birthDate\": \"2024-10-13\","
				+ "\"type\": {" + "   \"name\": \"cat\"," + "   \"id\": 1" + "}" + "}";

		// Create RequestOptions using the builder pattern
		RequestOptions options = RequestOptions.create().setData(updatedPetData) // Set the request body with updated
																					// name
				.setHeader("Content-Type", "application/json"); // Ensure Content-Type is JSON

		// Construct the relative and full URL
		String relativeUrl = "api/pets/" + petId;
		String fullUrl = pwInit.getBaseURL() + relativeUrl;
		System.out.println("Full URL: " + fullUrl); // Log the full URL

		// Make the PUT request to update the pet's name
		APIResponse response = apiRequestContext.put(relativeUrl, options);

		// Assert the response status code
		int statusCode = response.status();
		Assert.assertEquals(statusCode, 204, "Expected status code 204 for successful update.");

		if (statusCode == 204) {
			System.out.println("Pet name updated successfully!");
		} else {
			System.out.println("Error updating pet name: Status code " + statusCode);
		}
	}

	@AfterTest
	public void tearDown() {
		if (apiRequestContext != null) {
			apiRequestContext.dispose(); // Clean up the APIRequestContext
		}
	}
}