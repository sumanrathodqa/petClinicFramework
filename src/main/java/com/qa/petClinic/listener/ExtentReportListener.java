package com.qa.petClinic.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

	 private static ExtentReports extent;
	    private static ExtentTest test;

	    @Override
	    public void onStart(ITestContext context) {
	        // Setup report configuration
	        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("ExtentReport.html");
	        extent = new ExtentReports();
	        extent.attachReporter(sparkReporter);
	        extent.setSystemInfo("OS", "Windows");
	        extent.setSystemInfo("Browser", "Chrome");
	    }

	    @Override
	    public void onTestStart(ITestResult result) {
	        // Create a test in the report
	        test = extent.createTest(result.getMethod().getMethodName());
	        
	    }

	    @Override
	    public void onTestSuccess(ITestResult result) {
	        // Log success to the report
	        test.pass("Test passed successfully");
	    }

	    @Override
	    public void onTestFailure(ITestResult result) {
	        // Log failure to the report
	        test.fail("Test failed: " + result.getThrowable());
	    }

	    @Override
	    public void onTestSkipped(ITestResult result) {
	        // Log skipped tests to the report
	        test.skip("Test skipped: " + result.getMethod().getMethodName());
	    }

	    @Override
	    public void onFinish(ITestContext context) {
	        // Flush the report at the end of the tests
	        extent.flush();
	    }
	}