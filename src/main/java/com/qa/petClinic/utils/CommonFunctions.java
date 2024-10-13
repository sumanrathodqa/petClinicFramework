package com.qa.petClinic.utils;

import com.microsoft.playwright.Page;

public class CommonFunctions {

	private Page page;

	public CommonFunctions(Page page) {
		this.page = page;
	}

	public String getPageURL() {
		String pageUrl = this.page.url();
		return pageUrl;
	}
	
	

}
