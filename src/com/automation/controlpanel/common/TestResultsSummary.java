package com.automation.controlpanel.common;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class TestResultsSummary {
	
	private List<TestResultsTable> testResultTable;
	
	public boolean ifTestPresentInTable(String testName)
	{
		for(TestResultsTable trt : testResultTable)
		{
			if(trt.getTestName().equals(testName))
				return true;
		}
		return false;
	}
	
	private void addNewTest(String testName, int passed, int failed)
	{
		TestResultsTable trt = new TestResultsTable(testName, passed, failed);
		testResultTable.add(trt);
		
	}
	
	private void updateTableElementByTestName(String testName, boolean ifTestPassed)
	{
		int count = 0;
		
		for(TestResultsTable trt : testResultTable)
		{
			if(trt.getTestName().equals(testName))
			{
				testResultTable.remove(count);
				if(ifTestPassed)
					trt.increasePassedAmount();
				else
					trt.increeaseFailedAmount();
				testResultTable.add(count, trt);
				return;
			}
			count++;
		}
		
	}

	public TestResultsSummary(String fileName)
	{
		List<String> tests;
		
		testResultTable = new ArrayList<TestResultsTable>();
		
		try {
			tests = Files.readAllLines(Paths.get(fileName));
						
			for(String str : tests)
			{
				String[] sbuf = str.split(",");
				addNewTest(sbuf[0], Integer.parseInt(sbuf[1]), Integer.parseInt(sbuf[2]));
				
			}
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
	
	}
	
	public int getTableSize(){return testResultTable.size();}
	
	public void updateTest(String testName, boolean isTestPassed)
	{
		if(!ifTestPresentInTable(testName))
		{
			if(isTestPassed)
				addNewTest(testName, 1, 0);
			else
				addNewTest(testName, 0, 1);
		}else
		{
			updateTableElementByTestName(testName, isTestPassed);
		}
	}
	
	public void saveTableToFile(String fileName)
	{
		try {
			Files.delete(Paths.get(fileName));
			FileWriter file = new FileWriter(fileName);
			
			for(TestResultsTable trt : testResultTable)
			{
				file.write(trt.getTestName() + ","
						+ String.valueOf(trt.getPassedAmount()) + ","
						+ String.valueOf(trt.getFailedAmount()) + "\n");
			}
			
			file.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
