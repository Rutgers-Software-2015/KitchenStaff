package KitchenStaff;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Shared.Communicator.DatabaseCommunicator;
/*
 * author Rahul Tandon
 */
public class KitchenStaffCommunicator extends DatabaseCommunicator

{
	public KitchenStaffCommunicator()
	{
	
	}
	
	/*
	public ResultSet getAllInventory() throws SQLException
	{	/*
		boolean INTERNET;
		INTERNET = isThereInternet();
		
		this.connect("admin", "gradMay17");
		this.tell("use MAINDB;");
		ResultSet rsI = this.tell("select * FROM INVENTORY;");
		this.disconnect();
		return rsI;
		
	}*/

	/*
	 * Turns Inventory Result into a Array
	 * 
	 */
	public String[] getInventoryName() throws SQLException
	{	
		this.connect("admin", "gradMay17");
		this.tell("use MAINDB;");
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
			
			
			ResultSetMetaData rsd = I.getMetaData();
			int colsize=rsd.getColumnCount();
			int arrayindex=0;
			while(I.next())
			{	
		    	InventoryName[arrayindex]=I.getString(1);
				arrayindex++;
			}
			this.disconnect();
			return InventoryName;
		}
		
		catch(SQLException e)
		{
			
		}
		
		
		return null;
			
	}
	public Integer[] getInventoryQ() throws SQLException
	{	
		this.connect("admin", "gradMay17");
		this.tell("use MAINDB;");
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
			int colsize=rsd.getColumnCount();
			int arrayindex=0;
			while(I.next())
			{	
		    	InventoryQ[arrayindex]=I.getInt(2);
				arrayindex++;
			}
			this.disconnect();
			return InventoryQ;
		}
		
		catch(SQLException e)
		{
			
		}
	
		return null;
	
		
			
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
		
	public String[] getMenuItemIngredients(int MenuID,int rowid) throws SQLException
	{
		try{
		
			this.connect("admin", "gradMay17");
			this.tell("use MAINDB;");
			String sqlcomm = "SELECT * FROM MENU WHERE MENU_ID = " + MenuID; 
			ResultSet rs =  this.tell(sqlcomm);
			String I=rs.getString("INGREDIENTS");
			

			String[] IngList=ParseIngredients(I);
			String sqlcommand="UPDATE TABLE_ORDER set CURRENT_STATUS ='READY' where rowid="+rowid+";"; 
			this.update(sqlcommand);
			this.disconnect();
			return IngList;
			}
			catch(SQLException e)
			{
				
			};
			return null;
			
	}
	/*
	 * Pass the Ingredients string from the Database. Parses string and puts it into 
	 * a String array.
	 * @return  String array with all the ingredients.
	 */
	
	
	
	public void UpdateInventory(String Ing[],int q) throws SQLException
	{

		
	
			this.connect("admin", "gradMay17");
			this.tell("use MAINDB;");
			ResultSet I = this.tell("select * FROM INVENTORY;");
		for(int i=0; i<Ing.length;i++)
		{
			while(I.next())
			{

				if(I.getString("Item_Name").equals(Ing[i]))
				{
			
					int old=I.getInt("Amount");
					old=old-q*1;
					String sqlcommand="UPDATE INVENTORY set Amount= "+old+"  where Item_Name="+"'"+Ing[i]+"'"+";"; 
					this.update(sqlcommand);
				
				}
			}
			I.beforeFirst();
		}
		this.disconnect();

	}
	
	public String[] getTableOrders() throws SQLException
	{
		this.connect("admin", "gradMay17");
		this.tell("use MAINDB;");
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
				if(!TableOrder.getString("CURRENT_STATUS").equals("READY"))
				{
					rowcount++;
				}
				
			}
		}while(TableOrder.next());
		String[] FullOrders=new String[rowcount];
		TableOrder.beforeFirst();
		
				try{
					
					while(TableOrder.next())
					{	
						if(!TableOrder.getString("CURRENT_STATUS").equals("READY"))
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
					this.disconnect();

					return FullOrders;
				}
				
				catch(SQLException e)
				{
					
				}
				return null;
		
	}
	public void print(String[] test)
	{
		for(int i=0; i<test.length;i++)
		{
			System.out.println(test[i]);
		}
	}
	
}
