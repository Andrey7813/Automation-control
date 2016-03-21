package com.automation.controlpanel.common;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AutomationCommons {
	
	public static String[] getTestsList(JSONObject jObj)
	{
		
		String tmpStr = "";
		int devicesAmount = jObj.size() - 1;
		
		for(int i = 1; i<= devicesAmount; i++)
			{
				JSONObject device = (JSONObject) jObj.get("device" + String.valueOf(i));
				
				JSONArray testItems = (JSONArray) device.get("TESTS");
				for(int j = 0; j <= testItems.size() -1 ; j++)
					tmpStr += testItems.get(j).toString() + ":";
			}
		return tmpStr.split(":");
		
	}
	
	public static String[] getTestsListForDevice(JSONObject jObj, String deviceStr)
	{
		
		String tmpStr = "";
			
		JSONObject device = (JSONObject) jObj.get(deviceStr);
				
		JSONArray testItems = (JSONArray) device.get("TESTS");
		for(int j = 0; j <= testItems.size() -1 ; j++)
			tmpStr += testItems.get(j).toString() + ":";

		return tmpStr.split(":");
		
	}
	
	
	public static JSONObject getJSONObject(String path)
	{
	
		JSONParser parser = new JSONParser();
		
		Object obj;
		try {
			obj = parser.parse(new FileReader(path + "AutomationConfig.json"));
		
			return (JSONObject) obj;
		}catch (IOException | ParseException e1) {
			
			e1.printStackTrace();
			return null;
		}
	}
	
	public static String[] getDevicesComments(JSONObject jObj)
	{
		
		 int devicesAmount = jObj.size() - 1;
		 
		 String[] devices = new String[devicesAmount];
		
		for(int i = 1; i<= devicesAmount; i++)
			{
				JSONObject device = (JSONObject) jObj.get("device" + String.valueOf(i));
				String comment = (String)device.get("COMMENT");
				devices[i - 1] = comment;
			}
		return devices;
	}
	
	public static String[] getDevicesAndComments(JSONObject jObj)
	{
		
		 int devicesAmount = jObj.size() - 1;
		 
		 String[] devices = new String[devicesAmount];
		
		for(int i = 1; i<= devicesAmount; i++)
			{
				JSONObject device = (JSONObject) jObj.get("device" + String.valueOf(i));
				String comment = (String)device.get("COMMENT");
				devices[i - 1] = "device" + String.valueOf(i) + "   :   " + comment;
			}
		return devices;
	}
	
	public static HashMap<String, String> getTestsDevicesPairs(JSONObject jObj)
	{
		HashMap<String, String> pairs = new HashMap<String, String>();
		String deviceString = "device";
		
		JSONObject deviceObjects = (JSONObject) jObj;
		int devicesAmount = deviceObjects.size() - 1;
		for(int i = 1; i<= devicesAmount; i++)
		{
			
			JSONArray testItems = null;
			JSONObject device = null;
			
			device = (JSONObject) deviceObjects.get(deviceString + String.valueOf(i));
			String comment = (String)device.get("COMMENT");
			
			testItems = (JSONArray) device.get("TESTS");
			for(int j = 0; j<= testItems.size() - 1; j++)
				pairs.put((String) testItems.get(j), comment);
		}

		return pairs;
	}
	
	public static HashMap<String, String> getDeviceInfo(JSONObject jObj, String deviceString)
	{
		HashMap<String, String> pairs = new HashMap<String, String>();
		
		JSONObject deviceObjects = (JSONObject) jObj;
		
		JSONObject device = (JSONObject) deviceObjects.get(deviceString);
		
		pairs.put("PLATFORM", (String)device.get("PLATFORM"));
		pairs.put("DEVICE", (String)device.get("DEVICE"));
		pairs.put("OS_VERSION", (String)device.get("OS_VERSION"));
		pairs.put("PORT", (String)device.get("PORT"));
		pairs.put("COMMENT", (String)device.get("COMMENT"));
		pairs.put("SIMULATOR_ID", (String)device.get("SIMULATOR_ID"));
		pairs.put("IOS_APP_PATH", (String)device.get("IOS_APP_PATH"));
		pairs.put("PHOTO_TAKE_BUTTON", (String)device.get("PHOTO_TAKE_BUTTON"));
		pairs.put("PHOTO_CONFIRM_BUTTON", (String)device.get("PHOTO_CONFIRM_BUTTON"));
		

		return pairs;
	}
	
	public static HashMap<String, String> getParameterValuePairs(JSONObject jObj)
	{
		HashMap<String, String> pairs = new HashMap<String, String>();
		
		
		JSONObject deviceObjects = (JSONObject) jObj;
		JSONObject device = null;
			
			device = (JSONObject) deviceObjects.get("configuration");
			pairs.put("AndroidDriverActivity", 		(String)device.get("AndroidDriverActivity"));
			pairs.put("AndroidPassengerActivity", 	(String)device.get("AndroidPassengerActivity"));
			pairs.put("AndroidDriverPackage", 		(String)device.get("AndroidDriverPackage"));
			pairs.put("AndroidPassengerPackage", 	(String)device.get("AndroidPassengerPackage"));
			pairs.put("iOSDriverBundle", 			(String)device.get("iOSDriverBundle"));
			pairs.put("iOSPassengerBundle", 		(String)device.get("iOSPassengerBundle"));
			pairs.put("ReportFileName", 			(String)device.get("ReportFileName"));
			pairs.put("LogPath", 					(String)device.get("LogPath"));
			pairs.put("localhost", 					(String)device.get("localhost"));
			pairs.put("StartersPath", 				(String)device.get("StartersPath"));
			pairs.put("GetLogScript", 				(String)device.get("GetLogScript"));
			pairs.put("adb", 						(String)device.get("adb"));
			pairs.put("deviceconsole", 				(String)device.get("deviceconsole"));
			pairs.put("SimulatorLogs", 				(String)device.get("SimulatorLogs"));
			pairs.put("ScreenShotsPath", 			(String)device.get("ScreenShotsPath"));
			pairs.put("BackupPath", 			    (String)device.get("BackupPath"));
			pairs.put("SaveStatistics", 			(String)device.get("SaveStatistics"));
					

		return pairs;
	}
	
	public static String findDeviceByCommnet(JSONObject jObj, String comment)
	{
		String deviceString = "device";
		
		JSONObject deviceObjects = (JSONObject) jObj;
		int devicesAmount = deviceObjects.size() - 1;
		for(int i = 1; i<= devicesAmount; i++)
		{
			
			JSONArray testItems = null;
			JSONObject device = null;
			
			device = (JSONObject) deviceObjects.get(deviceString + String.valueOf(i));
			String curComment = (String)device.get("COMMENT");
			if(curComment.equals(comment))
				return deviceString + String.valueOf(i);
		}
		return null;
	}
	
	public static void saveJSONObjectToFile(JSONObject jObj, String path) throws IOException
	{
		FileWriter file = new FileWriter(path + "AutomationConfig.json");
		file.write(jObj.toJSONString());
		file.close();
	}

}
