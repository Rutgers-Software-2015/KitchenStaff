package KitchenStaff;

import java.sql.ResultSet;

import Shared.Communicator.DatabaseCommunicator;

public class KitchenStaffCommunicator extends DatabaseCommunicator

{
	public KitchenStaffCommunicator()
	{
	
	}
	public void getIngredients()
	{	/*
		boolean INTERNET;
		INTERNET = isThereInternet();
		*/
		System.out.println("Hello1");
		this.connect("admin", "gradMay17");
		this.tell("use MAINDB;");
		ResultSet rs = this.tell("select * FROM INVENTORY;");
		this.consolePrintTable(rs);
		System.out.println("Hello2");
		
	}
	
}
