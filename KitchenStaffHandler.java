package KitchenStaff;

import java.util.Queue;

import ADT.*;

/**
 * 
 * @author Rahul Tandon
 *
 */

public class KitchenStaffHandler 
{
	
	public Queue<Order> CurrentOrder;
	public Queue<TableOrder> WaitQueueOrder;
	private Employee current;
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
	public void ViewOrderQueue()
	{
		

	}
	public void DisplayCurrentOrder()
	{
		
		
	}
}
