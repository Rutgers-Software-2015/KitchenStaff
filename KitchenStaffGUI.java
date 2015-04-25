package KitchenStaff;
/**
 * This .java file creates the gui for the KitchenStaff.
 * author Rahul Tandon
 **/

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.border.Border;

import Login.LoginWindow;
import Shared.ADT.*;
import Shared.ADT.MenuItem;
import Shared.Gradients.*;
import Shared.Notifications.NotificationGUI;

import javax.swing.ButtonGroup;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;

import Shared.Communicator.*;
public class KitchenStaffGUI  extends JFrame implements ActionListener {

	//Parent Panels
	private JPanel rootPanel,titlePanel,buttonPanel;
	private GradientPanel backgroundPanel,buttonPanelBackground;
	private GradientPanel card1,card2,card3;
	//Swing Objects
	private GradientButton logOutButton,emergencyButton,SendMessageButton,OrderReadyButton,helpButton;
	private JButton payWithCash,payWithCard;
	private JLabel titleLabel,dateAndTime;
	//Other Variables
	private Timer timer,timer3,timer4;
	private JTable CurrentOrder,OrdersTable,StockTable,MessageTable;
	private JPanel panel;
	private JPanel panel_1;
	Font myFont = new Font("SansSerif", Font.PLAIN, 18);
	private JPanel panel_2;
	private JPanel panel_3;
	private DefaultTableModel ModelCurr,ModelOrders;
	public Queue<Order> Current;
	public Queue<TableOrder> temp;
	private NotificationGUI notification;
	KitchenStaffCommunicator commun=new KitchenStaffCommunicator();
	public KitchenStaffGUI()
	{
		super();
		init();
	}


	public void init()
	{
		
		//Used for testing till database is fully functioning.

		
		this.setTitle("KitchenStaff Interface");
		this.setResizable(true);
		this.setSize(1200,700);
		this.frameManipulation();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setResizable(false);
		getContentPane().add(rootPanel);
		
		addWindowListener(new WindowAdapter() // To open main window again if you hit the corner "X"
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                new LoginWindow();
                notification.close();
                timer.stop();
                timer3.stop();
                commun.dis();
                dispose();
            }
        });

		this.setVisible(true);
	}

	public void frameManipulation()
	{
		rootPanel = new JPanel();
		rootPanel.setLayout(null);
		notification = new NotificationGUI(1,"KitchenStaff");
		rootPanel.add(notification);
		setBackgroundPanel();
		setTitlePanel();
		setCardPanel();
		setButtonPanel();
		setRootPanel();

		
		try{
			FillInventory();
			FillWaitingOrders();
			}
			catch(SQLException e)
			{
				
			};	

		
		
		
	}
	
	private void setRootPanel()
	{
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(770, 75, 400, 580);
		rootPanel.add(panel_1);
		panel_1.setLayout(null);
		
				JScrollPane CurrentOrderPanel = new JScrollPane();
				CurrentOrderPanel.setBounds(0, 35, 400, 545);
				CurrentOrderPanel.setBorder(BorderFactory.createLineBorder(Color.black));
				CurrentOrderPanel.setBackground(Color.white);
				CurrentOrderPanel.setOpaque(false);
				panel_1.add(CurrentOrderPanel);
				
							
							CurrentOrder = new JTable();
							CurrentOrder.setBackground(Color.white);
							CurrentOrder.setFont(new Font("Tahoma", Font.PLAIN, 15));
							CurrentOrder.setModel(new DefaultTableModel(
								new Object[][] {
									{null, null, null, null},
								},
								new String[] {
									"Table ID", "MenuItem", "Quantity", "Special Instructions"
								}
							) {
								Class[] columnTypes = new Class[] {
									Integer.class, MenuItem.class, Integer.class, String.class
								};
								public Class getColumnClass(int columnIndex) {
									return columnTypes[columnIndex];
								}
								boolean[] columnEditables = new boolean[] {
									false, false, false, false
								};
								public boolean isCellEditable(int row, int column) {
									return columnEditables[column];
								}
							});
							CurrentOrder.getColumnModel().getColumn(0).setResizable(false);
							CurrentOrder.getColumnModel().getColumn(0).setPreferredWidth(50);
							CurrentOrder.getColumnModel().getColumn(1).setResizable(false);
							CurrentOrder.getColumnModel().getColumn(1).setPreferredWidth(110);
							CurrentOrder.getColumnModel().getColumn(2).setResizable(false);
							CurrentOrder.getColumnModel().getColumn(2).setPreferredWidth(55);
							CurrentOrder.getColumnModel().getColumn(3).setResizable(false);
							CurrentOrder.getColumnModel().getColumn(3).setPreferredWidth(125);
							
							CurrentOrderPanel.setViewportView(CurrentOrder);
							
							JLabel lblOrders = new JLabel("Orders");
							lblOrders.setFont(new Font("Tahoma", Font.BOLD, 16));
							lblOrders.setHorizontalAlignment(SwingConstants.CENTER);
							lblOrders.setBounds(0, 0, 400, 35);
							panel_1.add(lblOrders);
		
		panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setBounds(280, 75, 400,580);
		rootPanel.add(panel_3);
		panel_3.setLayout(null);
		
		JScrollPane StockPanel = new JScrollPane();
		StockPanel.setBounds(0, 35, 400, 545);
		StockPanel.setBackground(Color.white);
		StockPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel_3.add(StockPanel);
		StockPanel.setToolTipText("");
		StockPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
			
			StockTable = new JTable();
			StockTable.setBackground(Color.white);
			StockTable.setFont(new Font("Tahoma", Font.PLAIN, 15));
			StockTable.setRowSelectionAllowed(false);
			StockTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},

				},
				new String[] {
					"Ingredient", "Quantity"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, Integer.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
					false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			StockTable.getColumnModel().getColumn(0).setResizable(false);
			StockTable.getColumnModel().getColumn(0).setPreferredWidth(120);
			StockTable.getColumnModel().getColumn(1).setResizable(false);
			StockTable.getColumnModel().getColumn(1).setMaxWidth(100);
			StockPanel.setViewportView(StockTable);
			
			JLabel lblInventoryAvailable = new JLabel("Inventory Available");
			lblInventoryAvailable.setFont(new Font("Tahoma", Font.BOLD, 16));
			lblInventoryAvailable.setHorizontalAlignment(SwingConstants.CENTER);
			lblInventoryAvailable.setBounds(0, 0, 400, 35);
			panel_3.add(lblInventoryAvailable);
			
		rootPanel.add(titlePanel);

								
		rootPanel.add(buttonPanel);
		
		// Create Background Panel
		backgroundPanel = new GradientPanel();
		backgroundPanel.setBounds(0, 0, 1194, 672);
		rootPanel.add(backgroundPanel);
		backgroundPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		backgroundPanel.setGradient(new Color(255,255,255), new Color(255,110,110));
		backgroundPanel.setLayout(null);
		
		rootPanel.add(buttonPanelBackground);
	
	}
	
	private void setBackgroundPanel()
	{
		// Create Button Background Panel
		buttonPanelBackground = new GradientPanel();
		buttonPanelBackground.setGradient(new Color(255,220,220), new Color(255,110,110));
		buttonPanelBackground.setBounds(0, 55, 250, 617);
		buttonPanelBackground.setBorder(new LineBorder(new Color(0, 0, 0)));
	}
	
	//************************************************************
	//DO NOT edit the following function except for the title name
	//************************************************************
	
	private void setTitlePanel()
	{
		// Create Title Panel
		titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, 1194, 56);
		titlePanel.setLayout(null);
		titlePanel.setOpaque(false);
		// Set Title
		titleLabel = new JLabel("KitchenStaff Interface");
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(38f));
		titleLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		titleLabel.setBounds(new Rectangle(0, 0, 793, 56));
		
					// Add components to Title Panel
					titlePanel.add(titleLabel);
					// Set Date and Time
					dateAndTime = new JLabel();
					dateAndTime.setBounds(792, 0, 402, 56);
					titlePanel.add(dateAndTime);
					dateAndTime.setHorizontalAlignment(JLabel.CENTER);
					dateAndTime.setFont(dateAndTime.getFont().deriveFont(28f));
					dateAndTime.setBorder(BorderFactory.createLineBorder(Color.black));
					updateClock();
					// Create a timer to update the clock
					timer = new Timer(500,this);
		            timer.setRepeats(true);
		            timer.setCoalesce(true);
		            timer.setInitialDelay(0);
		            timer.start();
		        	timer3 = new Timer(60000,this);
		            timer3.setRepeats(true);
		            timer3.setCoalesce(true);
		            timer3.setInitialDelay(0);
		            timer3.start();
		     
	}
	
	//*********************************************************
	//DO NOT change the location of the following panel
	//*********************************************************
	
	private void setButtonPanel()
	{
		// Create Button Panel
		buttonPanel = new JPanel();
		buttonPanel.setBounds(7, 61, 236, 604);
		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonPanel.setLayout(new GridLayout(4, 0,4 , 4));
		
		// SendEmergency Button
		emergencyButton = new GradientButton("Emergency");
		emergencyButton.addActionListener(this);
		emergencyButton.setFont(emergencyButton.getFont().deriveFont(16.0f));
		emergencyButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		emergencyButton.setFocusPainted(false);
		
		// Order Ready Button
		OrderReadyButton = new GradientButton("Order Ready");
		OrderReadyButton.addActionListener(this);
		OrderReadyButton.setFont(OrderReadyButton.getFont().deriveFont(16.0f));
		OrderReadyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		OrderReadyButton.setFocusPainted(false);
		
		// Set Request Refund Button // May edit later
		helpButton = new GradientButton("Help");
		helpButton.addActionListener(this);
		helpButton.setFont(helpButton.getFont().deriveFont(16.0f));
		helpButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		helpButton.setFocusPainted(false);
		
		// Set Logout Button
		logOutButton = new GradientButton("Logout");
		logOutButton.addActionListener(this);												
		logOutButton.setFont(logOutButton.getFont().deriveFont(16.0f));
		logOutButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		logOutButton.setFocusPainted(false);
		
		buttonPanel.add(emergencyButton);

		buttonPanel.add(OrderReadyButton);
		buttonPanel.add(helpButton);
		buttonPanel.add(logOutButton);
	}
	
	//********************************************************************************
	//DO NOT deviate from the card layout or change the size/location of the cardPanel.
	//Creating and adding cards is OK
	//********************************************************************************
	
	private void setCardPanel()
	{
		
		card1 = new GradientPanel(); // Create card with a button YES
	}
	
	
	// Action Listener
	public void actionPerformed(ActionEvent e) 
	{
		Object a = e.getSource();
		if(a == logOutButton)
			{	
				dispose();
				new LoginWindow();
				
			}
		if(a == emergencyButton)
			{
				// Use Send Message function to send notification to all employees.
			int choice=JOptionPane.showConfirmDialog(null,
					"Do you want to send an Emergency?", "Confirmation", JOptionPane.YES_NO_OPTION);
					switch(choice)
					{
					case 1:
						break;
					case 0:
						String msg = JOptionPane.showInputDialog("Please input the message: ");
					
						//Sending emergency to all employees
							notification.sendMessage("ALL", msg);

						break;
					}
				//SendMessage(msg,0); // 0 means all employees
			}

		if(a == OrderReadyButton)
		    {
			// Manager scroll views
			if(CurrentOrder.getRowCount()==0)
			{
				JOptionPane.showMessageDialog(this, "No orders available.");
			
			}
			else
			{
			ModelCurr=(DefaultTableModel)CurrentOrder.getModel();

		
				try 
				{
					int rowselected=CurrentOrder.getSelectedRow();
					String qs=(String) CurrentOrder.getValueAt(rowselected, 2);
					int q=Integer.parseInt(qs);
					
					// Have handler notify order is ready.
					KitchenStaffHandler.OrderReady(rowselected,q,commun);

					// Update the Inventory and Orders Display.	
					FillInventory();
					FillWaitingOrders();
					
					if(CurrentOrder.getRowCount()==0)
					{
						JOptionPane.showMessageDialog(this, "All orders Complete.");
					}
					
				} 
				catch (IndexOutOfBoundsException e1)
				{
					JOptionPane.showMessageDialog(this, "Please select a row.");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		    }
			
		if(a == helpButton)
			{
				int choice=0;
				String[] options={"Emergency","Send Message","Order Ready"};
				choice=JOptionPane.showOptionDialog(new JFrame(), " Which function would you like help with?","Help Button",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,0);
				switch(choice)
				{
					case 0: 
						JOptionPane.showMessageDialog(this, "After clicking the button. Type in emergency message. It will send it to all the employee.");
						break;
					case 1:
						JOptionPane.showMessageDialog(this, "Open the notification box by clicking on the clock and  then press Send Message.");
						break;
					case 2:
						JOptionPane.showMessageDialog(this, "Select the row for the item that was completed. Then click the button.");
						break;
				}
			
				
			}

		if(a == timer)
			{
				updateClock();
			}
		if(a==timer3)
		{
			try {
				
				FillInventory(); 
				FillWaitingOrders();
		
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}	
	private void updateClock() {
        dateAndTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }
/*
 * 
 * Fills all the NOT READY orders into the Orders ScrollView.
 * Uses data received from KitchenStaffCommunicator
 * @returns Nothing, does changes to GUI.
 * 	
 */
	private void FillWaitingOrders() throws SQLException 
	{

		
		ModelOrders=(DefaultTableModel)CurrentOrder.getModel();

		commun.CheckWaitingOrders();
		String[] Orders=commun.getTableOrders();
		int rows=Orders.length/7;
		int rowtemp2=0;

		for(int i=0;i<Orders.length;i++)
		{	
		/*
			int statusloc=7*(i)+4;
			String status=Orders[statusloc];
			if(status.equals("RETURNED"))
			{
				
			}
		*/	
			ModelOrders.setValueAt(Orders[i], rowtemp2, 0);
			i++;
			ModelOrders.setValueAt(Orders[i], rowtemp2, 1);
			i++;
			ModelOrders.setValueAt(Orders[i], rowtemp2, 2);
			i++;
			ModelOrders.setValueAt(Orders[i], rowtemp2, 3);
			i+=3;
			rowtemp2++;
			
	
			
			if(ModelOrders.getRowCount()<rows)
			{
				ModelOrders.addRow(new Object[][] {
						{null, null, null, null}});
			}
			
			
			
		}
	
		// delete the other rows that were already filled.
		int rowsnow= Orders.length/7;
		int oldrowcount=ModelOrders.getRowCount();
		for(int i=rowsnow;i<oldrowcount;i++)
		{
			ModelOrders.removeRow(i);
		}
		
	}
	/*
	 * This function fills the Inventory in the top left ScrollView.The ScrollViews contains all the Ingredients.
	 *  @returns nothing. 
	 */

	private void FillInventory() throws SQLException
	{
		
		DefaultTableModel ModelInven=(DefaultTableModel)StockTable.getModel();
	

		try{

		
		String[] InventoryName=commun.getInventoryName();
		Integer[] InventoryQuant=commun.getInventoryQ();
		int rows=InventoryName.length;
	
			int rowtemp=0;
			for(int i=0;i<InventoryName.length;i++)
			{
			
				ModelInven.setValueAt(InventoryName[i],rowtemp,0);
				ModelInven.setValueAt(InventoryQuant[i],rowtemp,1);
				rowtemp++;
				if(ModelInven.getRowCount()<rows)
				{
					ModelInven.addRow(new Object[][]{
							{null, null},});
				}
			}
		
		
		}
		catch (SQLException e)
		{
			
		};

	}
}
