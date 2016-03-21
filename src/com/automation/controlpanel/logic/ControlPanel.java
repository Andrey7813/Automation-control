package com.automation.controlpanel.logic;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import java.awt.Color;

import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.automation.controlpanel.common.*;
import javax.swing.border.BevelBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;

public class ControlPanel extends JFrame {

	private JPanel contentPane;
	private JSONObject jObj = null;
	JComboBox comboBox_device;
	JComboBox comboBox_test;
	
	final static public String path = "/Users/ateplyakov/Automation/Control/";
	private JLabel lblChooseParameter;
	private JComboBox comboBox_config;
	private JLabel lblNewLabel;
	private JTextField textField_value;
	private JLabel lblNewLabel_1;
	private JComboBox comboBox_device_from;
	private JLabel lblNewLabel_2;
	private JComboBox comboBox_device_to;
	private JButton btnNewButton_2;
	private JLabel lblNewLabel_3;
	private JLabel lblToThisDevice;
	private JComboBox comboBox_device_to_add_test;
	private JTextField textField_test_name;
	private JButton btnNewButton_3;
	private JScrollPane scrollPane_1;
	private RowTable table;
	private DefaultTableModel dtm;
	private JButton btnNewButton_4;
	private JButton btnNewButton_5;
	private JButton btnNewButton_6;
	private JButton btnDeleteTest;
	private JButton btnRefresh;
	private JButton btnUpdateResultTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ControlPanel frame = new ControlPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public ControlPanel() throws BadLocationException {
		setTitle("Automation tests control panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.jObj = AutomationCommons.getJSONObject(path);
		String[] test  = AutomationCommons.getTestsList(this.jObj);
		
		comboBox_test = new JComboBox();
    	Arrays.sort(test);
		for(int i = 0; i <= test.length - 1; i++)
			comboBox_test.addItem(test[i]);
		comboBox_test.setBounds(6, 19, 416, 27);
		contentPane.add(comboBox_test);
		
		JLabel lblAssignTestTo = new JLabel("Assign test to...");
		lblAssignTestTo.setBounds(242, 6, 169, 16);
		contentPane.add(lblAssignTestTo);
		
		
		
		comboBox_device = new JComboBox();
		String[] devices = AutomationCommons.getDevicesComments(this.jObj);
		Arrays.sort(devices);
		for(int i = 0; i <= devices.length - 1; i++)
			comboBox_device.addItem(devices[i]);
		comboBox_device.setBounds(434, 19, 387, 27);
		contentPane.add(comboBox_device);
		
				
		JLabel lblDevice = new JLabel("device");
		lblDevice.setBounds(448, 6, 61, 16);
		contentPane.add(lblDevice);
		
		JButton btnNewButton = new JButton("Assign");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					assignButtonPressed();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(840, 16, 110, 27);
		contentPane.add(btnNewButton);
		
		
		
		lblChooseParameter = new JLabel("Choose parameter...");
		lblChooseParameter.setBounds(252, 148, 132, 16);
		contentPane.add(lblChooseParameter);
		
		textField_value = new JTextField();
		//textField_value.setText(pairs.get(comboBox_config.getSelectedItem().toString()));
		textField_value.setBounds(434, 162, 387, 26);
		contentPane.add(textField_value);
		textField_value.setColumns(10);
		
		comboBox_config = new JComboBox();
		comboBox_config.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				parameterChanged();
			}
		});
		HashMap<String, String> pairs = AutomationCommons.getParameterValuePairs(this.jObj);
		List<String> hmParameter = new ArrayList<String>(pairs.keySet());
		hmParameter.sort(null);
		for(int i = 0; i <= hmParameter.size() - 1; i++)
			comboBox_config.addItem(hmParameter.get(i));
		comboBox_config.setBounds(6, 163, 416, 27);
		contentPane.add(comboBox_config);
		
		lblNewLabel = new JLabel("and set value");
		lblNewLabel.setBounds(450, 147, 97, 16);
		contentPane.add(lblNewLabel);
		
		
		
		JButton btnNewButton_1 = new JButton("Set value");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					changeValueButtonPressed();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(833, 159, 117, 29);
		contentPane.add(btnNewButton_1);
		
		lblNewLabel_1 = new JLabel("Move all test from this device to...");
		lblNewLabel_1.setBounds(183, 76, 228, 16);
		contentPane.add(lblNewLabel_1);
		
		comboBox_device_from = new JComboBox();
		for(int i = 0; i <= devices.length - 1; i++)
			comboBox_device_from.addItem(devices[i]);
		comboBox_device_from.setBounds(6, 90, 416, 27);
		contentPane.add(comboBox_device_from);
		
		lblNewLabel_2 = new JLabel("to this device");
		lblNewLabel_2.setBounds(444, 76, 103, 16);
		contentPane.add(lblNewLabel_2);
		
		comboBox_device_to = new JComboBox();
		for(int i = 0; i <= devices.length - 1; i++)
			comboBox_device_to.addItem(devices[i]);
		comboBox_device_to.setBounds(434, 90, 387, 27);
		contentPane.add(comboBox_device_to);
		
		btnNewButton_2 = new JButton("Move");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					moveAllTestsToAnotherDevice();
				} catch (IOException | BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_2.setBounds(833, 89, 117, 29);
		contentPane.add(btnNewButton_2);
		
		lblNewLabel_3 = new JLabel("Add new test with name...");
		lblNewLabel_3.setBounds(197, 217, 202, 16);
		contentPane.add(lblNewLabel_3);
		
		lblToThisDevice = new JLabel("to this device");
		lblToThisDevice.setBounds(448, 217, 99, 16);
		contentPane.add(lblToThisDevice);
		
		comboBox_device_to_add_test = new JComboBox();
		for(int i = 0; i <= devices.length - 1; i++)
			comboBox_device_to_add_test.addItem(devices[i]);
		comboBox_device_to_add_test.setBounds(434, 232, 387, 27);
		contentPane.add(comboBox_device_to_add_test);
		
		textField_test_name = new JTextField();
		textField_test_name.setBounds(6, 231, 416, 26);
		contentPane.add(textField_test_name);
		
		
		btnNewButton_3 = new JButton("Add test");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					addNewTestButtonPressed();
				} catch (IOException | BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_3.setBounds(833, 212, 117, 29);
		contentPane.add(btnNewButton_3);
		
				
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 300, 938, 260);
		contentPane.add(scrollPane_1);
		
	
		dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Parameter", "Value"};
		dtm.setColumnIdentifiers(header);
		table = new RowTable(dtm);
		scrollPane_1.setViewportView(table);
		
		btnNewButton_4 = new JButton("Show tests and devices");
		btnNewButton_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					refreshTextViewWithTestDevicePairs();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_4.setBounds(6, 578, 202, 29);
		contentPane.add(btnNewButton_4);
		
		btnNewButton_5 = new JButton("Show configuration");
		btnNewButton_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					refreshTextViewWithConfiguration();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_5.setBounds(221, 578, 178, 29);
		contentPane.add(btnNewButton_5);
		
		btnNewButton_6 = new JButton("Add, Edit, View Device");
		btnNewButton_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DeviceCreateEditView dcevFrame = new DeviceCreateEditView();
				dcevFrame.setVisible(true);
			}
		});
		btnNewButton_6.setBounds(406, 578, 184, 29);
		contentPane.add(btnNewButton_6);
		
		btnDeleteTest = new JButton("Delete test");
		btnDeleteTest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					buttonDeleteTestFromDevice();
				} catch (IOException | BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnDeleteTest.setBounds(833, 253, 117, 29);
		contentPane.add(btnDeleteTest);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				buttonRefresh();
			}
		});
		btnRefresh.setBounds(601, 578, 117, 29);
		contentPane.add(btnRefresh);
		
		btnUpdateResultTable = new JButton("Update Result Table");
		btnUpdateResultTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UpdateResultTable dcevFrame = new UpdateResultTable();
				dcevFrame.setVisible(true);
			}
		});
		btnUpdateResultTable.setBounds(724, 578, 159, 29);
		contentPane.add(btnUpdateResultTable);
		refreshTextViewWithTestDevicePairs();
		
				
		
	}
	
	
	
	public void assignButtonPressed() throws IOException, BadLocationException
	{
		String test = comboBox_test.getSelectedItem().toString();
		String comment = comboBox_device.getSelectedItem().toString();
		
		//Looking for test and delete it
		removeTestFronJSON(test);
		
		//Find device
		String device = AutomationCommons.findDeviceByCommnet(this.jObj, comment);
		
		//Add test to device
		addTestToDevice(device, test);
		
		AutomationCommons.saveJSONObjectToFile(this.jObj, this.path);
		AutomationCommons.getJSONObject(this.path);
		
		refreshTextViewWithTestDevicePairs();
		
	}
	
	public void parameterChanged()
	{
		HashMap<String, String> pairs = AutomationCommons.getParameterValuePairs(this.jObj);
		textField_value.setText(pairs.get(comboBox_config.getSelectedItem().toString()));
	}
	
	public void changeValueButtonPressed() throws IOException, BadLocationException
	{
		String parameter = comboBox_config.getSelectedItem().toString();
		String value = textField_value.getText();
		
		changeValueInConfiguration(parameter, value);
		AutomationCommons.saveJSONObjectToFile(this.jObj, this.path);
		AutomationCommons.getJSONObject(this.path);
		refreshTextViewWithConfiguration();
	}
	
	public void refreshTextViewWithTestDevicePairs() throws BadLocationException
	{
		HashMap<String, String> pairs = AutomationCommons.getTestsDevicesPairs(this.jObj);
		List<String> hmTests = new ArrayList<String>(pairs.keySet());
		List<String> hmDevices = new ArrayList<String>(pairs.values());
		for (int i = dtm.getRowCount() - 1; i >= 0; i--)dtm.removeRow(i);
		for(int i = 0; i <= hmTests.size() - 1; i++)
		{
			 dtm.addRow(new Object[] { hmTests.get(i), hmDevices.get(i)});
	         if(i%2 != 0)
	        	   table.setRowColor(i, Color.LIGHT_GRAY);
		}
		Vector data = dtm.getDataVector();
	    Collections.sort(data, new ColumnSorter(0));
	    dtm.fireTableStructureChanged();
			
	}
	
	private void refreshTextViewWithConfiguration() throws BadLocationException
	{
		HashMap<String, String> pairs = AutomationCommons.getParameterValuePairs(this.jObj);
		List<String> hmParameters = new ArrayList<String>(pairs.keySet());
		List<String> hmValues = new ArrayList<String>(pairs.values());
		for (int i = dtm.getRowCount() - 1; i >= 0; i--)dtm.removeRow(i);
		
		for(int i = 0; i <= hmParameters.size() - 1; i++)
		{
	           dtm.addRow(new Object[] { hmParameters.get(i), hmValues.get(i)});
	           if(i % 2 != 0)
	        	   table.setRowColor(i, Color.LIGHT_GRAY);
		}
		Vector data = dtm.getDataVector();
	    Collections.sort(data, new ColumnSorter(0));
	    dtm.fireTableStructureChanged();
	}
	
	
	
	public void addNewTestButtonPressed() throws IOException, BadLocationException
	{
		String testName = textField_test_name.getText();
		String deviceComment = comboBox_device_to_add_test.getSelectedItem().toString();
		String device = AutomationCommons.findDeviceByCommnet(this.jObj, deviceComment);
		
		addTestToDevice(device, testName);
		AutomationCommons.saveJSONObjectToFile(this.jObj, this.path);
		AutomationCommons.getJSONObject(this.path);
		refreshTextViewWithTestDevicePairs();
		
	}
	
	private void buttonRefresh()
	{
		AutomationCommons.getJSONObject(this.path);
		try {
			refreshTextViewWithTestDevicePairs();
			refreshTextViewWithConfiguration();
			refreshTestsList();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void refreshTestsList()
	{
		String[] test  = AutomationCommons.getTestsList(this.jObj);
			
    	Arrays.sort(test);
    	comboBox_test.removeAllItems();
		for(int i = 0; i <= test.length - 1; i++)
			comboBox_test.addItem(test[i]);
	}
	
	private void buttonDeleteTestFromDevice() throws IOException, BadLocationException
	{
		String testName = textField_test_name.getText();
		String deviceComment = comboBox_device_to_add_test.getSelectedItem().toString();
		String device = AutomationCommons.findDeviceByCommnet(this.jObj, deviceComment);
		
		deleteTestFromDevice(device, testName);
		
		AutomationCommons.saveJSONObjectToFile(this.jObj, this.path);
		AutomationCommons.getJSONObject(this.path);
		refreshTextViewWithTestDevicePairs();
		
	}
	
	private void deleteTestFromDevice(String device, String test)
	{
		JSONObject deviceObjects = (JSONObject) this.jObj;
		
		JSONObject jsDevice =  (JSONObject) deviceObjects.get(device);
		
		jsDevice.remove(test);
	}
	
	private void addTestToDevice(String device, String test)
	{
		JSONObject deviceObjects = (JSONObject) this.jObj;
		
		JSONObject jsDevice =  (JSONObject) deviceObjects.get(device);
		
		JSONArray testItems = (JSONArray) jsDevice.get("TESTS");
		testItems.add(test);
		
	}
	
	private void moveAllTestsToAnotherDevice() throws IOException, BadLocationException
	{
		String comment_from = comboBox_device_from.getSelectedItem().toString();
		String comment_to = comboBox_device_to.getSelectedItem().toString();
		
		String[] empty_array = new String[]{};
		
		String device_from = AutomationCommons.findDeviceByCommnet(this.jObj, comment_from);
		String device_to = AutomationCommons.findDeviceByCommnet(this.jObj, comment_to);
		
		JSONObject device = (JSONObject) this.jObj.get(device_from);
		JSONArray testItems = (JSONArray) device.get("TESTS");
		device.remove("TESTS");
		device.put("TESTS", new JSONArray());
		
		device = (JSONObject) this.jObj.get(device_to);
		device.put("TESTS", testItems);
		
		
		AutomationCommons.saveJSONObjectToFile(this.jObj, this.path);
		AutomationCommons.getJSONObject(this.path);
		refreshTextViewWithTestDevicePairs();
		
	}
	
	
	@SuppressWarnings("unused")
	private void changeValueInConfiguration(String parameter, String value)
	{
		JSONObject deviceObjects = (JSONObject) this.jObj;
		
		JSONObject jsDevice =  (JSONObject) deviceObjects.get("configuration");
//		JSONObject jsParameter =  (JSONObject) deviceObjects.get(parameter);
		jsDevice.replace(parameter, value);
	}
	
	
	
	private void removeTestFronJSON(String test)
	{
		//Looking for test and delete it
		int devicesAmount = this.jObj.size() - 1;
				 
		String[] devices = new String[devicesAmount];
				
		for(int i = 1; i<= devicesAmount; i++)
			{
				JSONObject device = (JSONObject) this.jObj.get("device" + String.valueOf(i));
				JSONArray testItems = (JSONArray) device.get("TESTS");
						
				for(int j = 0; j<= testItems.size() - 1; j++)
					if(((String) testItems.get(j)).equals(test))
						testItems.remove(j);
			}
	}

	

}


