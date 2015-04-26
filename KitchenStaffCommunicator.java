package KitchenStaff;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.management.Notification;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Shared.Communicator.DatabaseCommunicator;
import Shared.Notifications.NotificationGUI;
import Shared.Notifications.NotificationHandler;
;
/* This file assists in helping the  KitchenStaff communicate with the database and execute functions.
 * @author Rahul Tandon
 * @tester Rahul Tandon
 * @debugger Rahul Tandon
 **/
public class KitchenStaffCommunicator extends DatabaseCommunicator

{
	private NotificationGUI temp=new NotificationGUI(5,"KitchenStaff");
	
	/*
	 *  Constructor for Communicator. Establishes connection to the DB.
	 *  @return nothing.
	 */
	public KitchenStaffCommunicator()
	{

			this.connect("admin", "gradMay17");
			this.tell("use MAINDB;");
	}
	/*
	 * Used to disconnect from the Database.
	 * @return nothing.
	 */
	public void dis()
	{
		this.disconnect();
	}
	/*
	 * Gets the inventory ingredients names from the database
	 * @returns the name of each of the inventory items in a String array
	 * 
	 */
	public String[] getInventoryName() throws SQLException
	{	

		ResultSet I = this.tell("select * FROM INVENTORY;");
		
	//Getting rows in result set
		int rowcount=0;
		do
		{
			rowcount++;

		}while(I.next());
		
		I.beforeFirst();// Reset pointer to beginning of resultset.
		
		String[] InventoryName=new String[rowcount];// Initialize
		try{
	
			int arrayindex=0;
			while(I.next())
			{	
		    	InventoryName[arrayindex]=I.getString(1);
				arrayindex++;
			}

			return InventoryName;
		}
		
		catch(SQLException e)
		{
			
		}
		
		return null;
			
	}
/*
 * Gets the inventory ingredients amounts from the database
*  @returns the amount of each of the inventory items in a Integer array
 */
	public Integer[] getInventoryQ() throws SQLException
	{	
	
		ResultSet I = this.tell("select * FROM INVENTORY;");
		
	//Getting rows in result set
		int rowcount=0;
		do
		{
			rowcount++;

		}while(I.next());
		I.beforeFirst();// Reset pointer to beginning of resultset.
		
		Integer[] InventoryQ=new Integer[rowcount];// Initialize
		try{
			
			
			ResultSetMetaData rsd = I.getMetaData();
			
			int arrayindex=0;
			while(I.next())
			{	
		    	InventoryQ[arrayindex]=I.getInt(2);
				arrayindex++;
			}
			
	
		}
		
		catch(SQLException e)
		{
			
		}

		return InventoryQ;
	
		
			
	}
	/*
	 * Input: The ingredient String for the MenuItem
	 * @returns all the ingredients in an array. Parsed.
	 */
	public String[] ParseIngredients(String Ingredients) throws SQLException
	{
		String comma=",";
		int index=0;// Index in the array we are returning.
		int prevword=0; // Index at which the last ingredient ended.
		
		// Finding number of Ingredients in MenuItem
			int count=1;
			for(int i=0;i<Ingredients.length();i++)
			{
				if(comma.charAt(0)==Ingredients.charAt(i))
				{
					count++;
				}

			}	
		
		//Array we are returning
			String[] temp=new String[count];
				
		//Parsing the Ingredients
			for(int i=0;i<Ingredients.length();i++)
			{
				if(comma.charAt(0)==Ingredients.charAt(i))
				{
					temp[index]=Ingredients.substring(prevword, i);
					prevword=i+1;
					index++;
				}
				else if(i==(Ingredients.length()-1))
				{
					temp[index]=Ingredients.substring(prevword, i+1);
				}

			}
			
		return temp;     // Return the Ingredients for the MENUItem.
	}
		
	/*
	 * Given the MENUID, rowid and quantity the system updates the order status
	 */
	public boolean getMenuItemIngredientsandUpdate(int MenuID,int rowid,int q) throws SQLException
	{
		try{
		

			String sqlcomm = "SELECT * FROM MENU WHERE MENU_ID = " + MenuID+";"; 
			ResultSet rs =  this.tell(sqlcomm);
			String I=rs.getString("INGREDIENTS");
			String Item=rs.getString("ITEM_NAME");
			
	// Getting the tableid of that order.
			String getTableid="SELECT TABLE_ID from TABLE_ORDER where rowid="+rowid+";";
			ResultSet TID=this.tell(getTableid);
			int tableid=TID.getInt("TABLE_ID");

    // Getting the list of ingredients for an item.
			String[] IngList=ParseIngredients(I);
			
	// Checking if the Inventory is available to complete the order.
			if(Updateable(IngList,q))
			{
				
				UpdateInventory(IngList,q);
				String sqlcommand="UPDATE TABLE_ORDER set CURRENT_STATUS ='READY' where rowid="+rowid+";"; 
				this.update(sqlcommand);
				

				
				// Need to get Name of employee based of ID.
				temp.sendMessage("Waiter", Item +" ready for Table "+tableid); //public message sent to waiter
			}
			else
			{
				String notvalid="UPDATE MENU set VALID=0 WHERE MENU_ID="+MenuID+";";
				this.update(notvalid);
				// Not enough inventory to complete order.
				temp.sendMessage("Customer", "We could not process you order due to lack of stock.");
				
				String sqlcommand="UPDATE TABLE_ORDER set CURRENT_STATUS ='WAITING' where rowid="+rowid+";"; 
				this.update(sqlcommand);
			}
			
			}
			catch(SQLException e)
			{
				
			};

			return true;
			
	}
	/*
	 * Pass the Ingredients string from the Database. Parses string and puts it into 
	 * a String array.
	 * @return  String array with all the ingredients.
	 */
	
	
	
	public boolean UpdateInventory(String Ing[],int q) throws SQLException
	{

			ResultSet I = this.tell("select * FROM INVENTORY;");
			boolean updated=true;
			I.beforeFirst();
		for(int i=0; i<Ing.length;i++)
		{
			while(I.next())
			{

				if(I.getString("Item_Name").equals(Ing[i]))
				{
			
					int old=I.getInt("Amount");
					old=old-q*1;
					// Already checked it could be updated. So update inventory.
		
						String sqlcommand="UPDATE INVENTORY SET Amount= "+old+" where Item_Name="+"'"+Ing[i]+"'"+";";
						update(sqlcommand);
						if(old==0)
						{
							// make function to change valid but in MENU.
						}
				}
				
			}
			
			I.beforeFirst();
		}
	
		return updated;  // Return true if it passes through all of Ing are available.
	}
	
	
	
	/*
	 *  This is just an error checking function.
	 *  So if you have three ingredients for a MENUITEM and one ingredient makes it so that  the item cannot be made. 
	 *  The other ingredients should not be altered since the item was never made.
	 *  returns true if the ingredients can be updated.
	 */
	public boolean Updateable(String[] Ing, int q) throws SQLException
	{
		String[] AllI=getInventoryName();
		for(int j=0;j<Ing.length;j++)
		{
			for(int k=0;k<AllI.length;k++)
			{
				if(AllI[k].equals(Ing[j]))
				{
					break;
				}
			}
			return false;
		}
		
		
		
		ResultSet I = this.tell("select * FROM INVENTORY;");
		boolean abletoupdate=true;
		I.beforeFirst();
	for(int i=0; i<Ing.length;i++)
	{
		while(I.next())
		{	
			if(I.getString("Item_Name").equals(Ing[i]))
			{
		
				int old=I.getInt("Amount");
				int tempold=old;
				old=old-q*1;
				// Now must check if quantity is valid.
				if(old<0)
				{				
					temp.sendMessage("Manager","We do not have enough of"+Ing[i]+ "to complete the orders.");
					abletoupdate= false;
					break;
				}

			}
			
		}
		
		I.beforeFirst();
	}

	return abletoupdate;  // Return true if it passes through all of Ing are available.
}

	
	public String[] getTableOrders() throws SQLException
	{

		ResultSet TableOrder = this.tell("select * FROM TABLE_ORDER;");
		
	//Getting rows in result set
		int rowcount=0;
		ResultSetMetaData rsd = TableOrder.getMetaData();
		
		int colsize=rsd.getColumnCount();
		int arrayindex=0;
		do
		{
			for(int i=1;i<=7;i++)
			{ 
				if(TableOrder.getString("CURRENT_STATUS") != null)
				{
					if((TableOrder.getString("CURRENT_STATUS").equals("NOT READY") ) || (TableOrder.getString("CURRENT_STATUS").equals("RETURNED") ))
					{
						rowcount++;
					}
				}	
			}
		}while(TableOrder.next());
		
		String[] FullOrders=new String[rowcount];
		TableOrder.beforeFirst();
		
				try{
					
					while(TableOrder.next())
					{	
						if(TableOrder.getString("CURRENT_STATUS") != null)
						{
							if((TableOrder.getString("CURRENT_STATUS").equals("NOT READY") ) || (TableOrder.getString("CURRENT_STATUS").equals("RETURNED") ))
						{
							FullOrders[arrayindex]=TableOrder.getString("TABLE_ID");
							arrayindex++;
							FullOrders[arrayindex]=TableOrder.getString("ITEM_NAME");
							arrayindex++;
							FullOrders[arrayindex]=TableOrder.getString("QUANTITY");
							arrayindex++;
							FullOrders[arrayindex]=TableOrder.getString("SPEC_INSTR");
							arrayindex++;
							FullOrders[arrayindex]=TableOrder.getString("CURRENT_STATUS");
							arrayindex++;
							FullOrders[arrayindex]=TableOrder.getString("MENU_ITEM_ID");
							arrayindex++;
							FullOrders[arrayindex]=TableOrder.getString("rowid");
							arrayindex++;
						}
						}
					}
				

					return FullOrders;
				}
				
				catch(SQLException e)
				{
					
				}
				return null;
		
	}
	/*
	 * If Inventory was updated check if the waiting orders are satisfied. If the item can be made now
	 * its status is now 'NOT_READY'. Also the items valid bit is now set to 1. 
	 */
	public void CheckWaitingOrders() throws SQLException
	{

		ResultSet Waiting = this.tell("select * FROM TABLE_ORDER where CURRENT_STATUS='WAITING'; ");
		
		while(Waiting.next())
		{
			int menuid=Waiting.getInt("MENU_ITEM_ID");
			int q=Waiting.getInt("QUANTITY");
			int rowid=Waiting.getInt("rowid");
			ResultSet temp=this.tell("select INGREDIENTS from MENU where MENU_ID="+menuid+";" );
			String ing=temp.getString("INGREDIENTS");
			String tempI[]=ParseIngredients(ing);
			
			// Checks if the menuItem can be made since the Inventory has been changed.
			if(Updateable(tempI,q))
			{
				// Updating order so it can be made.
				String sqlcommand="UPDATE TABLE_ORDER set CURRENT_STATUS ='NOT READY' where rowid="+rowid+";"; 
				this.update(sqlcommand);
				
				//Updating Menu making it valid
				String valid="UPDATE MENU set VALID=1 WHERE MENU_ID="+menuid+";";
				this.update(valid);
			}
			
		}

		
	}
}
