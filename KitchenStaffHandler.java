package KitchenStaff;

import java.util.LinkedList;
import java.util.Queue;

import ADT.*;

/**
 * 
 * @author Rahul Tandon
 *
 */

public class KitchenStaffHandler 
{
	
	public static Queue<Order> CurrentOrder=new LinkedList<Order>();
	public static Queue<TableOrder> WaitQueueOrder=new LinkedList<TableOrder>();
	private Employee current;
	public static int TABLE_ID;
	
	// make the communicator
	/*
	 * The function below sends the message to a particular employee.
	 */
	private static boolean SendMessage(String message,Employee employee)
	{
		// Use connection with communicator to send message
		return true;
	}
	/*
	 * Listen for any messages being sent to the KitchenStaff
	 */
	public String RecieveMessage()
	{
		// Listen on socket;
		String message ="THE UNDERTAKER IS HERE";; // Set message received on socket.
		return message; 	
	}

	/*
	 * The function below sends emergency message to all the employees
	 */
	public boolean NotifyEmergency(String message)
	{
		/*
		 * Use the SendMessage function to send emergency message to 
		 * all employees.
		 */
		
		return true;
	}
	public String CheckInventory()
	{
		/*
		 * Access Database to read out inventory
		 */
		String InventoryStatus=" "; //Use the database to get this info
		return InventoryStatus;	
	}
	/*
	 * Remove completed order and notify waiter to pick it up.
	 */
	public boolean OrderComplete(Order currentOrder)
	{  
		if(SendMessage("Order Complete",current))// Sends message to waiter say order is done.
		{  
			if(!CurrentOrder.isEmpty())
			{
				CurrentOrder.remove();
			}
			return true;                         // If message was sent return true
		}
		return false;
	}
	/*
	 * Moves next order to Current Order
	 */
	public boolean StartOrder()
	{
		CurrentOrder.addAll(WaitQueueOrder.peek().FullTableOrder);// Adds all the orders from the Queue in Table order.
		current=WaitQueueOrder.peek().Employee;
		WaitQueueOrder.remove();                  //Remove items from the waiting orders
		return true;
	}
	public KitchenStaffHandler()
	{
//		this.CurrentOrder=new LinkedList<Order>();
//		this.WaitQueueOrder=new LinkedList<TableOrder>();
//		
//		WaitQueueOrder.add(ExampleOrders.table2);
//		WaitQueueOrder.add(ExampleOrders.table3);
//		WaitQueueOrder.add(ExampleOrders.table5);
//		WaitQueueOrder.add(ExampleOrders.table4);
	}
	public static void DisplayCurrentOrder()
	{

		WaitQueueOrder.add(ExampleOrders.table2);
		WaitQueueOrder.add(ExampleOrders.table3);
		WaitQueueOrder.add(ExampleOrders.table5);
		WaitQueueOrder.add(ExampleOrders.table4);
//		WaitQueueOrder.add(ExampleOrders.table2);
//		WaitQueueOrder.add(ExampleOrders.table3);
//		WaitQueueOrder.add(ExampleOrders.table5);
//		WaitQueueOrder.add(ExampleOrders.table4);
//		while(!ExampleOrders.table1.FullTableOrder.isEmpty())
//		{
//			KitchenStaffHandler.CurrentOrder.add(ExampleOrders.table1.FullTableOrder.peek());
//			ExampleOrders.table1.FullTableOrder.remove();
//			KitchenStaffHandler.TABLE_ID=ExampleOrders.table1.TABLE_ID;
//		}

	}
	public static void addTableOrder(TableOrder T)
	{
		WaitQueueOrder.add(T);
	}
}
