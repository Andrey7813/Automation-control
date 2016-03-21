package com.automation.controlpanel.common;

public class TestResultsTable {
	
	private String testName;
	private int passedTests;
	private int failedTests;

	public TestResultsTable(String testName, int passedTests, int faledTests)
	{
		this.testName = testName;
		this.passedTests = passedTests;
		this.failedTests = faledTests;
	}
	
	public String getTestName(){return this.testName;}
	
	public int getPassedAmount(){return this.passedTests;}
	
	public int getFailedAmount(){return this.failedTests;}
	
    public void setPassedAmount(int passedTests){ this.passedTests = passedTests;}
	
	public void setFailedAmount(int failedTests){this.failedTests = failedTests;}
	
    public void increasePassedAmount(){ this.passedTests++;}
	
	public void increeaseFailedAmount(){this.failedTests++;}
}
