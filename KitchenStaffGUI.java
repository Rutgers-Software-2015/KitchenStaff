package KitchenStaff;
/**
 * This .java file creates the gui for the KitchenStaff.
 * author Rahul Tandon
 **/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.border.Border;

import ADT.*;
import Login.LoginWindow;
public class KitchenStaffGUI  extends JFrame implements ActionListener {

	private JFrame frame;
	private JTable StockTable;
	private JTable MessageTable;
	private JTable OrdersTable;
	private JTable CurrentOrder;
	private JButton EmergButton;
	private JButton OrderReadyButton;
	private JButton btnLogout;
	private Order temp;
	DefaultTableModel ModelCurr;
	DefaultTableModel ModelOrders;
	public Queue<Order> Current;
	public Queue<TableOrder> Waiting;
	/*
	 * Initializes the KitchenStaffGUI.
	 * @returns nothing.
	 */
	public KitchenStaffGUI()
	{	
		initialize();
		
	}
	/*
	 * Used to initialize the Kitchen GUI:
	 *  @returns nothing
	 */
	public void initialize()
	{
		
		frame = new JFrame("KitchenStaff");
		frame.setResizable(false);
		frame.setSize(750,650);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		Current=new LinkedList <Order>();
		Waiting=new LinkedList <TableOrder>();
		
		ExampleOrders test=new ExampleOrders();
		while(!test.table1.FullTableOrder.isEmpty())
		{
			Current.add(test.table1.FullTableOrder.peek());
			test.table1.FullTableOrder.remove();
		}

		Waiting.add(test.table2);
		Waiting.add(test.table3);
		Waiting.add(test.table5);
		Waiting.add(test.table4);
		while(!KitchenStaffHandler.WaitQueueOrder.isEmpty())
		{
			Waiting.add(KitchenStaffHandler.WaitQueueOrder.peek());
			KitchenStaffHandler.WaitQueueOrder.remove();
		}
		EmergButtonInterface();
		Logout();	
		ReadyButtonInterface();
		ScrollOrders();
		MessageScroll();
		InventoryScroll();
		ScrollCurrentOrder();
		FillInventory(IngredientHandler.IngredientList,true);
		FillCurrentOrder();
		FillWaitingOrders();
	
	}
	/*
	 * This function creates the current Order in the top right ScrollView. 
	 *  @returns nothing. 
	 */
	private void ScrollCurrentOrder()
	{
		JScrollPane CurrentOrderPanel = new JScrollPane();
		CurrentOrderPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		CurrentOrderPanel.setBounds(357, 115, 362, 211);
		frame.getContentPane().add(CurrentOrderPanel);
		
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
	 * Fills the bottom right ScrollView  with the waiting Orders from each table.
	 * @return nothing.
	 */
	private void ScrollOrders()
	{
		JScrollPane OrderPanel = new JScrollPane();
		OrderPanel.setBounds(357, 333, 362, 277);
		OrderPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(OrderPanel);
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
	/*
	 * Creates the bottom right ScrollView  with the waiting Orders from each table.
	 * @return nothing.
	 */
private void MessageScroll()
{
	JScrollPane MessagesPanel = new JScrollPane();
	MessagesPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	MessagesPanel.setBounds(15, 430, 301, 180);
	MessagesPanel.setToolTipText("");
	frame.getContentPane().add(MessagesPanel);
	
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
		
		JScrollPane StockPanel = new JScrollPane();
		StockPanel.setBounds(15, 115, 301, 303);
		StockPanel.setToolTipText("");
		StockPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(StockPanel);
		
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
	

	/*
	 * Creates the ready button in the KitchenStaff GUI.
	 * @return nothing.
	 */	
	private void ReadyButtonInterface()
	{

		
		OrderReadyButton = new JButton("Order Ready");
		OrderReadyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				
				ModelCurr=(DefaultTableModel)CurrentOrder.getModel();
				String test="";
				try {
					test=(String) ModelCurr.getValueAt(0, 1);
					int id=MenuItem.getId(test);
					InventoryFix(id,(Integer) ModelCurr.getValueAt(0,2));
				} 
				catch (IndexOutOfBoundsException e1)
				{
					JOptionPane.showMessageDialog(frame, "No more orders available.");
				}
				
			
				if(ModelCurr.getRowCount()==1)
				{
					
					
					if(ModelOrders.getRowCount()==0)
					{
					
						ModelCurr.removeRow(0);
					}
					else
					{
						try {
							MoveWaitingtoCurrent();
						} 
						catch (IndexOutOfBoundsException e1)
						{
							JOptionPane.showMessageDialog(frame, "No more orders available.");
						}
					}}	

				else
				{
					try {

						ModelCurr.removeRow(0);
					} 
					catch (IndexOutOfBoundsException e1)
					{
						JOptionPane.showMessageDialog(frame, "All orders completed.");
					}
					
					
				}
				
			}
		});
		
		OrderReadyButton.setBounds(444, 15, 275, 75);
		OrderReadyButton.setForeground(Color.BLACK);
		frame.getContentPane().add(OrderReadyButton);
		OrderReadyButton.setVisible(false);
	}

	/*
	 * Creates the emergency button in the KitchenStaff GUI.
	 * @return nothing.
	 */	
	private void EmergButtonInterface()
	{
		EmergButton = new JButton("Emergency");
		EmergButton.setSelected(false);

		EmergButton.setBounds(15, 15, 275, 75);
		EmergButton.setForeground(Color.RED);

		frame.getContentPane().add(EmergButton);
		EmergButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				//Send Message to all employees.
				//if message send =true;
				JOptionPane.showMessageDialog(frame, "The emergency was sent.");
			}
		}
		);
		EmergButton.setVisible(false);
	}

	/*
	 * Creates the logout button in the KitchenStaff GUI.
	 * @return nothing.
	 */	
	private void Logout()
	{
		 btnLogout = new JButton("Logout");
		 btnLogout.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					new LoginWindow();
					//parent.setVisible(false);
					frame.dispose();
				}
			});
		btnLogout.setBounds(302, 34, 124, 37);
		frame.getContentPane().add(btnLogout);
	}
	/*
	 * This function fills the current Order in the top right ScrollView.The ScrollViews contains all the items from the current TableOrder. 
	 *  @returns nothing. 
	 */
	
	
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
		
	}
	private void FillWaitingOrders()
	{
		ModelOrders=(DefaultTableModel)OrdersTable.getModel();
		ExampleOrders test2=new ExampleOrders();
//		TableOrder temp2=test2.table5;
		int rowtemp2=0;

		Queue<TableOrder> temp = Waiting;
//		
//		
//		temp.add(test2.table2);
//		temp.add(test2.table3);
//		temp.add(test2.table4);
//		temp.add(test2.table5);
		
		while(!temp.isEmpty())
		{
		
		while(!temp.peek().FullTableOrder.isEmpty())
		{
			Order ordertemp2=temp.peek().FullTableOrder.peek();
			
			ModelOrders.setValueAt(temp.peek().TABLE_ID, rowtemp2, 0);
			ModelOrders.setValueAt(ordertemp2.item.STRING_ID, rowtemp2, 1);
			ModelOrders.setValueAt(ordertemp2.Quantity, rowtemp2, 2);
			ModelOrders.setValueAt(ordertemp2.Spc_Req, rowtemp2, 3);
			rowtemp2++;
			temp.peek().FullTableOrder.remove();
			if(!temp.peek().FullTableOrder.isEmpty())
			{
				ModelOrders.addRow(new Object[][] {
						{null, null, null, null}});
			}
		}

		temp.remove();
		if(!temp.isEmpty())
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
		btnLogout.setVisible(true);
		EmergButton.setVisible(true);
		OrderReadyButton.setVisible(true);

	}
	
	/*
	 * This function fills moves the first TableOrder in the Waiting Queue to the Current Order Queue once the Current Order is done.
	 *  @returns nothing. 
	 */

	
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
/*
 * As items are completed this function updates our inventory
 */
	private void InventoryFix(int Menu_ID,int quantity)
	{
		MenuItem temp = new MenuItem(Menu_ID);
		
		Ingredient temp1[]=temp.ings;

		KitchenStaffHandler.UpdateInventory(temp1,quantity);
		FillInventory(IngredientHandler.IngredientList,false);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
