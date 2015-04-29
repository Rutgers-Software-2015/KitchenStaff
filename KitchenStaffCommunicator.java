package KitchenStaff;

/* This file assists in helping the  KitchenStaff communicate with the database and execute functions.
 * @author Rahul Tandon
 * @tester Rahul Tandon
 * @debugger Rahul Tandon
 */

import java.sql.ResultSet;
import java.sql.SQLException;







import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Shared.Communicator.DatabaseCommunicator;
import Shared.Notifications.NotificationGUI;


public class KitchenStaffCommunicator extends DatabaseCommunicator

{
	private NotificationGUI Note;
	
	/*
	 *  Constructor for Communicator. Establishes connection to the DB.
	 *  @return nothing.
	 */
	public KitchenStaffCommunicator(NotificationGUI n)
	{
			Note = n;
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
 		@return String Array
		@param  none
		@Exceptions = SQLException
	 * 
	 */
	public String[] getInventoryName()
	{	

		ResultSet I = this.tell("select * FROM INVENTORY;");
		try{
	//Getting rows in result set
		int rowcount=0;
		do
		{
			rowcount++;

		}while(I.next());
		
		I.beforeFirst();// Reset pointer to beginning of resultset.
		
		String[] InventoryName=new String[rowcount];// Initialize
	
			int arrayindex=0;
			while(I.next())
			{	
		    	InventoryName[arrayindex]=I.getString(1);
				arrayindex++;
			}

			return InventoryName;
		}
		
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
		
			
	}
	
/*
 * Gets the inventory ingredients amounts from the database
 * 
 * 		@return Integer Array
		@param  none
 */
	public Integer[] getInventoryQ()
	{	
		Integer[] InventoryQ = null;
		ResultSet I = this.tell("select * FROM INVENTORY;");
		try{
	//Getting rows in result set
		int rowcount=0;
		do
		{
			rowcount++;

		}while(I.next());
		I.beforeFirst();// Reset pointer to beginning of resultset.
		
		InventoryQ=new Integer[rowcount];// Initialize
			
			int arrayindex=0;
			while(I.next())
			{	
		    	InventoryQ[arrayindex]=I.getInt(2);
				arrayindex++;
			}
			
			return InventoryQ;
		}
		
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}

	
		
			
	}
	
	/*
	 * This function parses the inputed string and returns a String Array
	 	
	 	@return String Array
		@param  
			Ingredients: A single string containing all the Ingredients:String

	 */
	public String[] ParseIngredients(String Ingredients)
	{
		String[] temp = null;
		try{
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
			temp=new String[count];
				
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
			
			return temp;// Return the Ingredients for the MENUItem.
			
		}catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
			
	}
		
	/*
	 * This function executes functions  to update the Inventory and the order status
	 * 
	 * 	@return boolean
		@param  
			MENUID: The id of the menuitem that is being 'readied'. :Integer
			rowid: The id of the row in TABLE_ORDER that is being 'readied'.:Integer
			q: Quantity of MenuItem being made:Integer
		@Exceptions = SQLException
	 */
	public boolean getMenuItemIngredientsandUpdate(int MenuID,int rowid,int q)
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
				//Note.sendMessage("Waiter", Item +" ready for Table "+tableid); //public message sent to waiter
			}
			else
			{
				// Not enough inventory to complete order.
				
				String notvalid="UPDATE MENU set VALID=0 WHERE MENU_ID="+MenuID+";";
				this.update(notvalid);
				ResultSet itemname=this.tell("SELECT ITEM_NAME from MENU where MENU_ID="+MenuID+ ";");
				String menuitem=itemname.getString("ITEM_NAME");
				
				// Alert Manager and Customer
				Note.sendMessage("Customer", "We could not complete the "+menuitem+ " that you order due to low inventory.");
				Note.sendMessage("Manager","We need more stock so we can make "+ menuitem);
				
				// Update the status to WAITING.
				String sqlcommand="UPDATE TABLE_ORDER set CURRENT_STATUS ='WAITING' where rowid="+rowid+";"; 
				this.update(sqlcommand);
			}
			
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
			};

			return true;
			
	}
	
	/*
	 * This function updates the Inventory based off the Ingredients in Ing and the quantity q.
		@return boolean
		@param  
			Ing: A string array that contains the Ingredients that make up a particular MenuItem:String Array
			q: Quantity of MenuItem being made:Integer
		@Exceptions = SQLException
	 */
	public boolean UpdateInventory(String Ing[],int q)
	{
		boolean updated=true;

		try{
			ResultSet I = this.tell("select * FROM INVENTORY;");
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
		}catch(Exception e){
			e.printStackTrace(System.out);
		}
		
		return updated;  // Return true if it passes through all of Ing are available.
	}
	
	/*
			This function checks if the Ingredients to make the MenuItem, that is about to be readied, exist in our Inventory. If 
			all the Ingredients to make the MenuItem exist, return true.
			
			@return boolean
			@param  
				Ing: A string array that contains the Ingredients that make up a particular MenuItem:String Array
			@Exceptions = SQLException
	*/
	public boolean ingredientsExist(String[] Ing)
	{
		String[] AllI={" "};
		try 
		{
			AllI = getInventoryName();
		}
		catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int j=0;j<Ing.length;j++)
		{
			for(int k=0;k<AllI.length;k++)
			{
				
				if(AllI[k].equals(Ing[j]))
				{
					break;
				}
				if((k==AllI.length-1))
				{
					return false;
				}
					
			}
		
		}
		return true;
		
	}
	
	/*
	 *  This is just an error checking function.
	 *  So if you have three ingredients for a MENUITEM and one ingredient makes it so that  the item cannot be made. 
	 *  The other ingredients should not be altered since the item was never made.
	 *  returns true if the ingredients can be updated.
	 *  
	 *  @return boolean
		@param  
			Ing: A string array that contains the Ingredients that make up a particular MenuItem:String Array
			q: Quantity of MenuItem being made:Integer
	 */
	public boolean Updateable(String[] Ing, int q)
	{	
		boolean abletoupdate=true;
	try
	{
		// Checking if all the Ingredients in Ing exist in the inventory.
		if(!ingredientsExist(Ing))
		{
			return false;
		}
		
		ResultSet I = this.tell("select * FROM INVENTORY;");
		
		I.beforeFirst();
	for(int i=0; i<Ing.length;i++)
	{
		while(I.next())
		{	
			if(I.getString("Item_Name").equals(Ing[i]))
			{
		
				int old=I.getInt("Amount");

				old=old-q*1;
				// Now must check if quantity is valid.
				if(old<0)
				{				
					Note.sendMessage("Manager","We do not have enough of "+Ing[i]+ " to complete the orders.");
					abletoupdate= false;
					break;
				}

			}
			
		}
		
		I.beforeFirst();
	}
	
	}
	catch(Exception e)
	{
		e.printStackTrace(System.out);
	}
		return abletoupdate; 

 // Return true if it passes through all of Ing are available.
	}
	
	/*
	 * 		This function gets the information needed by the KitchenStaff for the orders that appear in the TABLE_ORDER database
			table that arent ready. It gets the information for orders that still have to be completed and inserts them into an array. The array 
			is then returned.
			
			@return String Array
			@param  none

	*/
	public String[] getTableOrders()
	{
		try
		{
			
		ResultSet TableOrder = this.tell("Select * FROM TABLE_ORDER;");
		
	//Getting rows in result set
		int rowcount=0;

		
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
		catch(Exception e2)
		{
			e2.printStackTrace();
		}
				return null;
		
	}
	
	/*
	 * 	This function checks the Orders that have a 'WAITING' status and checks if they can be completed
	 *  due to any inventory updates.
	 *  
	 * 	@return none
		@param  none

	 */
	public void CheckWaitingOrders()
	{

		try
		{
		ResultSet Waiting = this.tell("SELECT * FROM TABLE_ORDER where CURRENT_STATUS='WAITING';");
		
		if(Waiting!=null){
			Waiting.first();
		while(Waiting.next())
		{
			int menuid=Waiting.getInt("MENU_ITEM_ID");
			int q=Waiting.getInt("QUANTITY");
			int rowid=Waiting.getInt("rowid");
			ResultSet temp=this.tell("select INGREDIENTS from MENU where MENU_ID="+menuid+";" );
			if(temp != null){
				temp.first();
				String ing = null;
				try{
			ing=temp.getString("INGREDIENTS");
				}catch(SQLException e){
					e.printStackTrace(System.out);
				}
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

		
}

