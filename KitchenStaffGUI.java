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

import javax.swing.ButtonGroup;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;
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
	private Timer timer;
	private JTable CurrentOrder,OrdersTable,StockTable,MessageTable;
	private JPanel panel;
	private JPanel panel_1;
	Font myFont = new Font("SansSerif", Font.PLAIN, 18);
	private JPanel panel_2;
	private JPanel panel_3;
	private DefaultTableModel ModelCurr,ModelOrders;
	public Queue<Order> Current;
	public Queue<TableOrder> temp;
	public KitchenStaffGUI()
	{
		super();
		init();
	}


	public void init()
	{
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
                dispose();
            }
        });
		
		this.setVisible(true);
	}

	public void frameManipulation()
	{
		rootPanel = new JPanel();
		rootPanel.setLayout(null);
		setBackgroundPanel();
		setTitlePanel();
		setCardPanel();
		setButtonPanel();
		temp=new LinkedList <TableOrder>();
		//temp=KitchenStaffHandler.WaitQueueOrder;
	
	// Creates the Table for Current Orders	
			CurrentOrderScroll();

	// Creates the JTable for Messages
			MessageScroll();
			
   // Creates the JTable for Inventory
			InventoryScroll();
			
			
		setRootPanel();
		FillWaitingOrders();
	
		FillInventory(IngredientHandler.IngredientList,true);
		
		
		
	}
	
	private void setRootPanel()
	{
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
		buttonPanel.setLayout(new GridLayout(5, 0, 5, 5));
		
		// SendEmergency Button
		emergencyButton = new GradientButton("Emergency");
		emergencyButton.addActionListener(this);
		emergencyButton.setFont(emergencyButton.getFont().deriveFont(16.0f));
		emergencyButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		emergencyButton.setFocusPainted(false);
		
		// Send Message Button
		SendMessageButton = new GradientButton("Send Message");
		SendMessageButton.addActionListener(this);
		SendMessageButton.setFont(SendMessageButton.getFont().deriveFont(16.0f));
		SendMessageButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		SendMessageButton.setFocusPainted(false);
		
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
		buttonPanel.add(SendMessageButton);
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
						break;
					}
				//SendMessage(msg,0); // 0 means all employees
			}
		if(a == SendMessageButton)
		{
		//Asks for the message and the id,
			String msg = JOptionPane.showInputDialog("Please input the message: ");
			String id = JOptionPane.showInputDialog("Please input the id of the employee: ");
			int emid=Integer.valueOf(id);
			
			//Use sendmessage funtion KSHandler.
			//SendMessage(msg,emid);
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
			String test="";
		
				try 
				{
					int rowselected=CurrentOrder.getSelectedRow();
					test=(String) ModelCurr.getValueAt(rowselected, 1);
					int id=MenuItem.getId(test);
					InventoryFix(id,(Integer) ModelCurr.getValueAt(rowselected,2));
					ModelCurr.removeRow(rowselected);
					if(CurrentOrder.getRowCount()==0)
					{
						JOptionPane.showMessageDialog(this, "All orders Complete.");
					}
				} 
				catch (IndexOutOfBoundsException e1)
				{
					JOptionPane.showMessageDialog(this, "Please select a row.");
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
						JOptionPane.showMessageDialog(this, "After clicking the button. Type in message and then employee ID. It will send the message to the employees.");
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
	}
	

	private void CurrentOrderScroll()
	{
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder( null, "Orders", TitledBorder.CENTER, TitledBorder.BELOW_TOP, myFont, new Color(0, 0, 0)));
		panel_1.setBounds(770, 75, 400, 580);
		rootPanel.add(panel_1);
		panel_1.setLayout(null);

		JScrollPane CurrentOrderPanel = new JScrollPane();
		CurrentOrderPanel.setBounds(0, 27, 400, 555);
		panel_1.add(CurrentOrderPanel);
		
					
					CurrentOrder = new JTable();
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
		
	}
/*
	private void WaitingOrderScroll()
	{
		 Font myFont = new Font("SansSerif", Font.PLAIN, 18);
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Waiting Orders", TitledBorder.CENTER, TitledBorder.BELOW_TOP, myFont, null));
		panel.setBounds(770, 375, 400, 280);
		rootPanel.add(panel);
		
		panel.setLayout(null);
		JScrollPane OrderPanel = new JScrollPane();
		OrderPanel.setBounds(0, 27, 400, 252);
		panel.add(OrderPanel);
		
					OrdersTable = new JTable();
					OrdersTable.setModel(new DefaultTableModel(
						new Object[][] {
								{null, null, null, null},
						},
						new String[] {
							"Table ID", "MenuItem", "Quantity", "Special Instrcutions"
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
					OrdersTable.getColumnModel().getColumn(0).setResizable(false);
					OrdersTable.getColumnModel().getColumn(0).setPreferredWidth(49);
					OrdersTable.getColumnModel().getColumn(1).setResizable(false);
					OrdersTable.getColumnModel().getColumn(1).setPreferredWidth(110);
					OrdersTable.getColumnModel().getColumn(2).setResizable(false);
					OrdersTable.getColumnModel().getColumn(2).setPreferredWidth(55);
					OrdersTable.getColumnModel().getColumn(2).setMinWidth(11);
					OrdersTable.getColumnModel().getColumn(3).setResizable(false);
					OrdersTable.getColumnModel().getColumn(3).setPreferredWidth(125);
					OrderPanel.setViewportView(OrdersTable);
					OrdersTable.setVisible(true);
					OrderPanel.setVisible(true);
	}
*/	
	

		private void MessageScroll()
		{
			
			panel_2 = new JPanel();
			panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Messages", TitledBorder.CENTER, TitledBorder.BELOW_TOP, myFont, new Color(0, 0, 0)));
			panel_2.setBounds(280, 375, 400, 280);
			
			rootPanel.add(panel_2);
			panel_2.setLayout(null);
			JScrollPane MessagesPanel = new JScrollPane();
			MessagesPanel.setBounds(0, 30, 400, 250);
			panel_2.add(MessagesPanel);
			
							
							MessageTable = new JTable();
							MessageTable.setModel(new DefaultTableModel(
								new Object[][] {
									{null, null},
								},
								new String[] {
									"Message", "    From Employee"
								}
							) {
								Class[] columnTypes = new Class[] {
									String.class, Object.class
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
							MessageTable.getColumnModel().getColumn(0).setResizable(false);
							MessageTable.getColumnModel().getColumn(0).setPreferredWidth(175);
							MessageTable.getColumnModel().getColumn(0).setMaxWidth(200);
							MessageTable.getColumnModel().getColumn(1).setResizable(false);
							MessageTable.getColumnModel().getColumn(1).setPreferredWidth(70);
			MessagesPanel.setViewportView(MessageTable);
		}
		/*
		 * Creates the top left ScrollView  with the current Inventory.
		 * @returns nothing.
		 */
			private void InventoryScroll()
			{
				Table table1=new Table(1,null,true);
				
				panel_3 = new JPanel();
				panel_3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Inventory Available", TitledBorder.CENTER, TitledBorder.BELOW_TOP, myFont, new Color(0, 0, 0)));
				panel_3.setBounds(280, 75, 400, 280);
				rootPanel.add(panel_3);
				panel_3.setLayout(null);
				
				JScrollPane StockPanel = new JScrollPane();
				StockPanel.setBounds(0, 27, 400, 253);
				panel_3.add(StockPanel);
				StockPanel.setToolTipText("");
				StockPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				
					
					StockTable = new JTable();
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
			}
	
	
	
	
	private void updateClock() {
        dateAndTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }
	

/*	
	private void FillCurrentOrder()
	{
		ModelCurr=(DefaultTableModel)CurrentOrder.getModel();
		int rowtemp=0;
	
		while(!Current.isEmpty())
		{
			Order ordertemp=Current.peek();
			ModelCurr.setValueAt(1, rowtemp, 0);
			
			ModelCurr.setValueAt(ordertemp.item.STRING_ID, rowtemp, 1);
			ModelCurr.setValueAt(ordertemp.Quantity, rowtemp, 2);
			ModelCurr.setValueAt(ordertemp.Spc_Req, rowtemp, 3);
			rowtemp++;
			Current.remove();
			if(!Current.isEmpty())
			{
				ModelCurr.addRow(new Object[][] {
						{null, null, null, null}});
			}
			
		}
		ModelCurr.addRow(new Object[][] {
				{null, null, null, null}});

		
	}
	*/
	private void FillWaitingOrders()
	{
		ModelOrders=(DefaultTableModel)CurrentOrder.getModel();
		ExampleOrders test2=new ExampleOrders();
//		TableOrder temp2=test2.table5;
		int rowtemp2=0;

	

		
		while(!KitchenStaffHandler.WaitQueueOrder.isEmpty())
		{
		
		while(!KitchenStaffHandler.WaitQueueOrder.peek().FullTableOrder.isEmpty())
		{
			Order ordertemp2=KitchenStaffHandler.WaitQueueOrder.peek().FullTableOrder.peek();
			
			ModelOrders.setValueAt(KitchenStaffHandler.WaitQueueOrder.peek().TABLE_ID, rowtemp2, 0);
			ModelOrders.setValueAt(ordertemp2.item.STRING_ID, rowtemp2, 1);
			ModelOrders.setValueAt(ordertemp2.Quantity, rowtemp2, 2);
			ModelOrders.setValueAt(ordertemp2.Spc_Req, rowtemp2, 3);
			rowtemp2++;
			KitchenStaffHandler.WaitQueueOrder.peek().FullTableOrder.remove();
			if(!KitchenStaffHandler.WaitQueueOrder.peek().FullTableOrder.isEmpty())
			{
				ModelOrders.addRow(new Object[][] {
						{null, null, null, null}});
			}
		}

		KitchenStaffHandler.WaitQueueOrder.remove();
		if(!KitchenStaffHandler.WaitQueueOrder.isEmpty())
		{
			ModelOrders.addRow(new Object[][] {
				{null, null, null, null}});
		}
		}
		
	}
	/*
	 * This function fills the Inventory in the top left ScrollView.The ScrollViews contains all the Ingredients.
	 *  @returns nothing. 
	 */

	private void FillInventory(Ingredient ingredientList[],boolean moreinven)
	{
		
		DefaultTableModel ModelInven=(DefaultTableModel)StockTable.getModel();
		int rowtemp=0;
		for(int i=0;i<ingredientList.length;i++)
		{
			if(i!=ingredientList.length-1 &(moreinven))
			{
				ModelInven.addRow(new Object[][]{
					{null, null},});
			}
			ModelInven.setValueAt(ingredientList[i].name,rowtemp,0);
			ModelInven.setValueAt(ingredientList[i].count,rowtemp,1);
			rowtemp++;
		}


	}

/*	
private void MoveWaitingtoCurrent()
{
	int rowtemp=0;
	int tableid=-1;
	ModelOrders=(DefaultTableModel)OrdersTable.getModel();
	if(ModelOrders.getRowCount()>0)
	{
		tableid=(Integer) ModelOrders.getValueAt(0,0);
	
	while(tableid==(Integer) ModelOrders.getValueAt(0,0))
	{
	
		ModelCurr.setValueAt(tableid,rowtemp,0);
		ModelCurr.setValueAt(ModelOrders.getValueAt(0,1),rowtemp,1);
		ModelCurr.setValueAt(ModelOrders.getValueAt(0,2),rowtemp,2);
		ModelCurr.setValueAt(ModelOrders.getValueAt(0,3),rowtemp,3);
		ModelOrders.removeRow(0);
		rowtemp++;
		if(tableid==(Integer) ModelOrders.getValueAt(0,0))
		{
			ModelCurr.addRow(new Object[][] {
				{null, null, null, null}});
		}
	}
	}
	
	
}
*/
	
/*
 * As items are completed this function updates our inventory based off the ingredients for that i
 */

	private void InventoryFix(int Menu_ID,int quantity)
	{
		MenuItem temp = new MenuItem(Menu_ID);
		
		Ingredient temp1[]=temp.ings;

		KitchenStaffHandler.UpdateInventory(temp1,quantity);
		FillInventory(IngredientHandler.IngredientList,false);
	}


	

}
