package com.automation.controlpanel.logic;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.automation.controlpanel.common.AutomationCommons;
import com.automation.controlpanel.common.ColumnSorter;
import com.automation.controlpanel.common.RowTable;

import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.ItemEvent;
import javax.swing.JButton;

public class DeviceCreateEditView extends JFrame {

	private JPanel contentPane;

	private JSONObject jObj = null;
	
	private RowTable table;
	private DefaultTableModel dtm;
	
	JComboBox comboBox_devices_and_commons;
	private JButton btnSaveChanges;
	
	/**
	 * Create the frame.
	 */
	public DeviceCreateEditView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 400);
		setTitle("Device Add, Edit, View");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		this.jObj = AutomationCommons.getJSONObject(ControlPanel.path);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 59, 888, 243);
		contentPane.add(scrollPane);
		
		dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Parameter", "Value"};
		dtm.setColumnIdentifiers(header);
		table = new RowTable(dtm);
		table.setCellSelectionEnabled(true);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tableClicked();
			}
		});
		
		String[] devicesAndComments = AutomationCommons.getDevicesAndComments(jObj);
		
		comboBox_devices_and_commons = new JComboBox();
		comboBox_devices_and_commons.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cmbBoxChanged();
			}
		});
		Arrays.sort(devicesAndComments);
		for(int i = 0; i <= devicesAndComments.length - 1; i++)
			comboBox_devices_and_commons.addItem(devicesAndComments[i]);
		comboBox_devices_and_commons.setBounds(53, 20, 752, 27);
		contentPane.add(comboBox_devices_and_commons);
		
		btnSaveChanges = new JButton("Save changes");
		btnSaveChanges.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				buttonSaveChanges();
			}
		});
		btnSaveChanges.setBounds(403, 324, 117, 29);
		contentPane.add(btnSaveChanges);
		

	}
	
	private void buttonSaveChanges()
	{
		String deviceStr = comboBox_devices_and_commons.getSelectedItem().toString().split("   :   ")[0];
		
		JSONObject device = (JSONObject) jObj.get(deviceStr);
		JSONArray testItems = (JSONArray) device.get("TESTS");
		testItems.clear();
		
		int rowsNumber =  dtm.getRowCount();
		String value = "";
		String key = "";
		
		for(int i = 0; i <= rowsNumber - 1; i++)
		{
			key = dtm.getValueAt(i, 0).toString();
			value = dtm.getValueAt(i, 1).toString();
			
			if(key.equals("TESTS")||key.equals(""))
			{
				testItems.add(value);
			}else
				device.put(key, value);
			
			key = "";
			value = "";
		}
		
		device.put("TESTS", testItems);
		jObj.put(deviceStr, device);
		
		try {
			AutomationCommons.saveJSONObjectToFile(jObj, ControlPanel.path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cmbBoxChanged();
	}
	
	private void cmbBoxChanged()
	{
		String device = comboBox_devices_and_commons.getSelectedItem().toString().split("   :   ")[0];
		HashMap<String, String> pairs = AutomationCommons.getDeviceInfo(jObj, device);
		String[] tests = AutomationCommons.getTestsListForDevice(jObj, device);
		List<String> hmParameter = new ArrayList<String>(pairs.keySet());
		List<String> hmValue = new ArrayList<String>(pairs.values());
		
		for (int i = dtm.getRowCount() - 1; i >= 0; i--)dtm.removeRow(i);
		
		for(int i = 0; i <= hmParameter.size() - 1; i++)
		{
			 dtm.addRow(new Object[] { hmParameter.get(i), hmValue.get(i)});
	         if(i%2 != 0)
	        	   table.setRowColor(i, Color.LIGHT_GRAY);
		}
		
		int prevCount = hmParameter.size() - 1;
		
		
		for(int i = 0; i <= tests.length - 1; i++)
		{
			if(i == 0)
				dtm.addRow(new Object[] { "TESTS", tests[i]});
			else
				dtm.addRow(new Object[] { "", tests[i]});
	        
			if(i%2 != 0)
				table.setRowColor(prevCount + i, Color.LIGHT_GRAY);
		}
		
		
		
		Vector data = dtm.getDataVector();
	    //Collections.sort(data, new ColumnSorter(0));
	    dtm.fireTableStructureChanged();
	}

	private void tableClicked()
	{
		int column = table.getSelectedColumn();
		int row = table.getSelectedRow();
		table.editCellAt(row, column);
		//table.setValueAt("", row, column);
		}
	
}
