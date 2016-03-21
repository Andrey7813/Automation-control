package com.automation.controlpanel.logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.automation.controlpanel.common.AutomationCommons;
import com.automation.controlpanel.common.RowTable;
import com.automation.controlpanel.common.TestResultsSummary;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JButton;

public class UpdateResultTable extends JFrame {

	private JPanel contentPane;
		
	private RowTable table;
	private DefaultTableModel dtm;
	private JScrollPane scrollPane_1;
	private JTextPane textPane;
	private JButton btnUpdateAndSave;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateResultTable frame = new UpdateResultTable();
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
	public UpdateResultTable() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		setTitle("Update Result Table");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 890, 276);
		contentPane.add(scrollPane);
		
		dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Test Name", "PASSED", "FAILED"};
		dtm.setColumnIdentifiers(header);
		table = new RowTable(dtm);
		table.getColumnModel().getColumn(0).setPreferredWidth(700);
		table.getColumnModel().getColumn(1).setPreferredWidth(95);
		table.getColumnModel().getColumn(2).setPreferredWidth(95);
		table.setCellSelectionEnabled(true);
		scrollPane.setViewportView(table);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(5, 300, 890, 311);
		contentPane.add(scrollPane_1);
		
		textPane = new JTextPane();
		textPane.setForeground(Color.GRAY);
		textPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPane.setForeground(Color.BLACK);
				textPane.setEnabled(true);
				textPane.setEditable(true);
				textPane.setText("");
				
			}
		});
		textPane.setText("Please, copy to this field new test results in format : test_name PASSED/FAILED");
		//textPane.setEnabled(false);
		scrollPane_1.setViewportView(textPane);
		
		btnUpdateAndSave = new JButton("Update and Save");
		btnUpdateAndSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				buttonUpdateInfo();
			}
		});
		btnUpdateAndSave.setBounds(401, 630, 147, 29);
		contentPane.add(btnUpdateAndSave);
		
		fillTestTable();
	}
	
	
	private void fillTestTable()
	{
		List<String> tests;
		try {
			tests = Files.readAllLines(Paths.get(ControlPanel.path + "tests_done.csv"));
			for (int i = dtm.getRowCount() - 1; i >= 0; i--)dtm.removeRow(i);
			
			int i = 0;
			
			for(String str : tests)
			{
				String[] sbuf = str.split(",");
				dtm.addRow(new Object[] { sbuf[0], sbuf[1], sbuf[2]});
		         if(i%2 != 0)
		        	   table.setRowColor(i, Color.LIGHT_GRAY);
		         i++;
				
			}
			Vector data = dtm.getDataVector();
		    //Collections.sort(data, new ColumnSorter(0));
		    dtm.fireTableStructureChanged();
		    
		    table.getColumnModel().getColumn(0).setPreferredWidth(700);
			table.getColumnModel().getColumn(1).setPreferredWidth(95);
			table.getColumnModel().getColumn(2).setPreferredWidth(95);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void buttonUpdateInfo()
	{
		String[] testSet = textPane.getText().split("\n");
		
		TestResultsSummary TRS = new TestResultsSummary(ControlPanel.path + "tests_done.csv");
		
		for(int i = 0; i <= testSet.length - 1; i++)
		{
			if(testSet[i].split("[+ +\t]")[1].equals("PASSED"))
				TRS.updateTest(testSet[i].split("[+ +\t]")[0], true);
			else
				TRS.updateTest(testSet[i].split("[+ +\t]")[0], false);
		}
		
		TRS.saveTableToFile(ControlPanel.path + "tests_done.csv");

		
		fillTestTable();
	}

}
