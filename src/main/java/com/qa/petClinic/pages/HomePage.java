package com.qa.petClinic.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class HomePage {

	private Page page;

	private String pageTitle = "h1.title";
	private String ownersDropdown = "a.dropdown-toggle[aria-expanded=\"false\"] > span.glyphicon-user";
	private String ownersSearch = "a[routerlink=\"/owners\"] > span.glyphicon-search";
	private String ownersAddNew = "a[routerlink=\"/owners/add\"] > span.glyphicon-plus";

	public HomePage(Page page) {
		this.page = page;
	}

	public String checkPageTitle() {
		String pageTitleText = page.locator(this.pageTitle).innerText();
		System.out.println("Page title: " + pageTitleText);
		return pageTitleText;
	}

	public void navigateToOwnersPage() {
		page.click(this.ownersDropdown);
		page.waitForSelector(this.ownersSearch,
				new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
		page.click(this.ownersSearch);
	}
	
	public void navigateToAddNewOwnerPage() {
		page.click(this.ownersDropdown);
		page.waitForSelector(this.ownersAddNew,
				new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
		page.click(this.ownersAddNew);
	}

}
