package KitchenStaff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import Shared.ADT.*;
import Shared.Communicator.DatabaseCommunicator;
import Shared.Notifications.NotificationGUI;
/*
 * This file helps run some functions of the KitchenStaff.
* @author Rahul Tandon
* @tester Rahul Tandon
* @debugger Rahul Tandon
**/

public class KitchenStaffHandler 
{

	/*
	 * Constructor	
	 */
	public KitchenStaffHandler()
	{

	}	
	/* 
	 * This function  does the  Order Ready Commands once the order ready button is clicked.
	 *		@return nothing
	 *		@param  
	 *			 rowselected: The rowselected in JTable :Integer 
	 *			 q: The quantity in that same row: Integer
	 *			 comm: A communicator to access the Database: KitchenStaffCommunicator
	 *		@Exceptions = SQLException
	*/

	public static void OrderReady(int rowselected,int q,KitchenStaffCommunicator comm) throws SQLException
	{
		
		String[] temp=comm.getTableOrders();

		int idloc= 7*(rowselected)+5;           //Gets location of MENUID
		int MenuID=Integer.parseInt(temp[idloc]); // Gets the MENUID value
		int rowid=Integer.parseInt(temp[7*(rowselected)+6]); //gets row id of order
		
		//Get the ingredients and update inventory accordingly.
		comm.getMenuItemIngredientsandUpdate(MenuID,rowid,q);

	}
	/*
	 * This function assists in sending emergencies to all employees.
	@return boolean
	@param  
		 message: The message that is to be sent.: String 
		 confirm: A confirmation bit checking to make sure the emergency should be sent.: Integer (0 or 1)
		 n: A notificationGUI that allows you to communicate with other employees: NotificationGUI
	@Exceptions = SQLException
	
	*/
	public static boolean SendEmergency(String message,int confirm,NotificationGUI n)
	{
		if(confirm==1)
		{
			return false;
		}
		n.sendMessage("ALL", message);
		return true;
		
	}

	
	
}
