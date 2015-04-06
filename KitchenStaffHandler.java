package KitchenStaff;

import java.util.LinkedList;
import java.util.Queue;

import Shared.ADT.*;

/**
 * 
 * @author Rahul Tandon
 *
 **/

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
	/*
 	* Updates the current Inventory stock.
 	* @return TableStock is now updated.	
 	*/
	public static void UpdateInventory(Ingredient I[],int quantity)
	{

		for(int i=0;i<I.length;i++)
		{
			IngredientHandler.UpdateInventory(I[i],quantity*-1);
		}
		
	}
	
	/*
	 * Remove completed order and notify waiter to pick it up. Will be used once communicator created.
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
	 * Moves next order to Current Order.
	 */
	
	public boolean StartOrder()
	{
		CurrentOrder.addAll(WaitQueueOrder.peek().FullTableOrder);// Adds all the orders from the Queue in Table order.
		current=WaitQueueOrder.peek().Employee;
		WaitQueueOrder.remove();                  //Remove items from the waiting orders
		return true;
	}
	
	/*
	 * Constructor	
	 */
	public KitchenStaffHandler()
	{

	}
	/*
	 * Adds the WaitingOrders to the WaitQueue.
	 * @return WaitQueueOrder now has a new element added to the end.
	 */
	public static void addTableOrder(TableOrder T)
	{
		WaitQueueOrder.add(T);
	}

}
