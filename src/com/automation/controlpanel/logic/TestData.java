package com.automation.controlpanel.logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.json.simple.JSONObject;

import com.automation.controlpanel.common.AutomationCommons;

import javax.swing.JComboBox;
import javax.swing.JTextPane;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TestData extends JFrame {

	private JPanel contentPane;
	private JComboBox cmbBox_DataSets;
	private JComboBox cmbBox_TestsList;
	private JTextPane txtpnS;
	private JButton btnNewButton;
	private JSONObject jObj = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestData frame = new TestData();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestData() {
		
		this.jObj = AutomationCommons.getJSONObject(ControlPanel.path);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(19, 133, 398, 150);
		contentPane.add(scrollPane_1);
		
		txtpnS = new JTextPane();
		//txtpnS.setOpaque(true);
		//txtpnS.setBackground(Color.red);
		scrollPane_1.setViewportView(txtpnS);
		
		cmbBox_DataSets = new JComboBox();
		cmbBox_DataSets.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				readDataIntoTextBox();
			}
		});
		cmbBox_DataSets.setBounds(19, 83, 398, 27);
		contentPane.add(cmbBox_DataSets);
		
		cmbBox_TestsList = new JComboBox();
		cmbBox_TestsList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateDataSets();
			}
		});
		cmbBox_TestsList.setBounds(19, 33, 398, 27);
		contentPane.add(cmbBox_TestsList);
		
		JLabel lblDataSetName = new JLabel("Data set name");
		lblDataSetName.setBounds(134, 65, 102, 16);
		contentPane.add(lblDataSetName);
		
		JLabel lblNewLabel = new JLabel("Test name");
		lblNewLabel.setBounds(134, 17, 83, 16);
		contentPane.add(lblNewLabel);
		
		btnNewButton = new JButton("Accept choosen data set as default");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setDefaultDataSet();
			}
		});
		btnNewButton.setBounds(29, 293, 375, 29);
		contentPane.add(btnNewButton);
		String[] testsNames = readTestsList();
		Arrays.sort(testsNames);
		for(int i = 0; i <= testsNames.length - 1; i++)
			cmbBox_TestsList.addItem(testsNames[i]);
		
	}
	
	private void setDefaultDataSet()
	{
		String[] tests = readTestsList();
		String dataSet = cmbBox_DataSets.getSelectedItem().toString();
		
		for(int i = 0; i <= tests.length - 1; i++)
		{
			String data = readDataForTestAndDataSet(tests[0], dataSet);
			changeDataInTest(tests[0], dataSet);
		}
		
		
		
	}
	
	private void changeDataInTest(String test, String data)
	{
		String codePath = AutomationCommons.getParameterValuePairs(jObj).get("TestsCodePath");
		String newData = txtpnS.getText();
		String srcFileName = codePath + test + ".java";
		String srcTmp = codePath + "test_code.java";
		
		String testSource = "";
		
		try {
			FileReader fr = new FileReader(new File(srcFileName));
			BufferedReader br = new BufferedReader(fr);
			
			FileWriter fw = new FileWriter(new File(srcTmp));
			
			String line = "";
			while((line = br.readLine()) != null)
			{
				if(line.contains("@Data"))
				{
					fw.write(line + "\n");
					fw.write(newData);
					while(!line.contains("@End Data"))
					{
						line = br.readLine();
					}
				}
				fw.write(line + "\n");
			}
			
			br.close();
			fr.close();
			
			fw.close();
			
			new File(srcFileName).delete();
			new File(srcTmp).renameTo(new File(srcFileName));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String readDataForTestAndDataSet(String test, String dataSet)
	{
		boolean readingDone = false;
		String allData = "";
		
		String fileName = ControlPanel.path + "test_data.ini";
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line;
		try {
			while((line = br.readLine()) != null && !readingDone)
				{
					if(line.contains("test_name")&&line.contains(test))
					{
						while(true)
						{
							line = br.readLine();
							if(line.contains(dataSet))
								break;
						}
						while(true)
						{
							line = br.readLine();
							if(line == null)break;
							if(line.contains("data_set")||line.contains("test_name"))
							{
								readingDone = true;
								break;
							}
							allData += line + "\n";
						}
					}
					if(line == null)break;
				}
				
				br.close();
				fr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allData;
	}
	
	private void readDataIntoTextBox()
	{
		String allData = "";
		boolean readingDone = false;
		
		String testName = cmbBox_TestsList.getSelectedItem().toString();
		String testSet = cmbBox_DataSets.getSelectedItem().toString();
		
		String fileName = ControlPanel.path + "test_data.ini";
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line;
		try {
			while((line = br.readLine()) != null && !readingDone)
			{
				if(line.contains("test_name")&&line.contains(testName))
				{
					while(true)
					{
						line = br.readLine();
						if(line.contains(testSet))
							break;
					}
					while(true)
					{
						line = br.readLine();
						if(line == null)break;
						if(line.contains("data_set")||line.contains("test_name"))
						{
							readingDone = true;
							break;
						}
						allData += line + "\n";
					}
				}
				if(line == null)break;
			}
			
			txtpnS.setText(allData);
			
			br.close();
			fr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateDataSets()
	{
		String[] dataSets = readDataSetsList();
		
		Arrays.sort(dataSets);
		for(int i = 0; i <= dataSets.length - 1; i++)
			cmbBox_DataSets.addItem(dataSets[i]);
	}
	
	private String[] readTestsList()
	{
		String testNames = "";
		
		String fileName = ControlPanel.path + "test_data.ini";
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line;
		try {
			while((line = br.readLine()) != null)
			{
				if(line.contains("test_name"))
					testNames += line.split(":")[1] + "<-->";
			}
			
			br.close();
			fr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return testNames.split("<-->");
	}
	
	private String[] readDataSetsList()
	{
		String dataSetName = "";
		
		
		int testIdx = 0;
		
		String fileName = ControlPanel.path + "test_data.ini";
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line;
		try {
			while((line = br.readLine()) != null)
			{
				if(line.contains("test_name")&&testIdx == 0)
				{
					testIdx++;
					line = "";
				}
				if(line.contains("data_set"))
					dataSetName += line.split(":")[1] + "<-->";
				if(line.contains("test_name")&&testIdx > 0)
					break;
			}
			
			br.close();
			fr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return dataSetName.split("<-->");
	}
	
	private void appendString(String str) 
	{
	     StyledDocument document = (StyledDocument) txtpnS.getDocument();
	     try {
			document.insertString(document.getLength(), str, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	                                                   
	 }
}
