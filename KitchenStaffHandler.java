package KitchenStaff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import Shared.ADT.*;
import Shared.Communicator.DatabaseCommunicator;

/**
 * 
 * @author Rahul Tandon
 *
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
	 * The function  Does the  Order Ready Commands once the order ready button is clicked.
	 *  @return Nothing
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

	
	
}
