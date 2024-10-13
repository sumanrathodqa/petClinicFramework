package com.qa.petClinic.pages;

import java.util.List;

import com.github.javafaker.Faker;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class OwnersPage {

	private Page page;

	private String inputFirstName = "input[name=\"firstName\"]";
	private String inputLastName = "input[name=\"lastName\"]";
	private String inputAddress = "input[name=\"address\"]";
	private String inputCity = "input[name=\"city\"]";
	private String inputTelephone = "input[name=\"telephone\"]";
	private String submitNewOwnerButton = "button[type=\"submit\"]";
	private String ownerNamesList = "table.table-striped td.ownerFullName a";
	private String invalidFirstnameErrorMessage = "span.help-block";

	public OwnersPage(Page page) {
		this.page = page;
	}

	Faker faker = new Faker();

	public String enterValidNewOwnerDetails() {
		String firstName = removeSpecialCharacters(faker.name().firstName());
		String lastName = removeSpecialCharacters(faker.name().lastName());
		page.fill(inputFirstName, firstName);
		page.fill(inputLastName, lastName);
		page.fill(inputAddress, faker.address().streetAddress());
		page.fill(inputCity, faker.address().city());
		page.fill(inputTelephone, faker.numerify("##########"));
		return firstName;
	}

	public void submitNewOwnerDetails() {
		String currentUrl = page.url();
		page.click(this.submitNewOwnerButton);
		page.waitForURL(url -> !url.equals(currentUrl), new Page.WaitForURLOptions().setTimeout(10000));
		page.waitForLoadState(LoadState.NETWORKIDLE);
	}

	public boolean verifyNewOwnerExists(String ownerName) {
		page.waitForTimeout(500);

		// Scroll down and wait for elements to load
		for (int i = 0; i < 5; i++) { // Scrolls down 5 times
			page.evaluate("window.scrollBy(0, window.innerHeight);");
			page.waitForTimeout(500); // Wait for 0.5 seconds after each scroll
			page.waitForLoadState(LoadState.NETWORKIDLE); // Ensure network is idle
		}

		// Wait for the owner names list to be available
		page.waitForSelector(ownerNamesList);
		page.waitForLoadState(LoadState.NETWORKIDLE); // Wait until the network is idle

		// Retrieve all owner names
		List<String> ownerNames = page.locator(ownerNamesList).allTextContents();

//	    for debugging purposes
//	    // Print the list of owner names
//	    System.out.println("Owner names found on the page:");
//	    for (String name : ownerNames) {
//	        System.out.println(name); // Print each owner name
//	    }
//	    
		System.out.println("Looking for Owner first name in List: " + ownerName);

		boolean isOwnerAddedToList = ownerNames.stream().anyMatch(fullName -> fullName.split(" ")[0].equals(ownerName));

		System.out.println("First name found: " + isOwnerAddedToList);

		return isOwnerAddedToList;
	}

	public String enterInvalidValidOwnerName() {
		String invalidFirstName = faker.numerify("##########");
		page.fill(inputFirstName, invalidFirstName);
		page.fill(inputLastName, faker.name().lastName());
		page.fill(inputAddress, faker.address().streetAddress());
		page.fill(inputCity, faker.address().city());
		page.fill(inputTelephone, faker.numerify("##########"));
		return invalidFirstName;
	}

	public boolean verifyErrorMessageIsDisplayed() {
		Locator errorMessageLocator = page.locator(invalidFirstnameErrorMessage);
		boolean isErrorVisible = errorMessageLocator.isVisible();
		return isErrorVisible;
	}

	public boolean verifyInvalidUserNameIsnotSubmitted(String invalidOwnerName) {
		Locator submitNewOwnerButtonLocator = page.locator(submitNewOwnerButton);
		boolean isDisabled = submitNewOwnerButtonLocator.isDisabled();
		return isDisabled;
	}

	private static String removeSpecialCharacters(String input) {
		return input.replaceAll("[^a-zA-Z]", "");
	}
}
