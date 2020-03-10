/**
 *  ArrayStack.java
 *  
 *	A class that represents a stack that holds objects of type E.
 *  The stack uses an ArrayList to holt elements, with the beginning
 *  of the ArrayList (index 0) being the bottom of the stack and the
 *  end of the ArrayList being the top of the stack.
 *
 *	@author	Krish Agarwal
 *	@since	2/25/2020
 */

import java.util.List;
import java.util.ArrayList;

public class ArrayStack<E> implements Stack<E>
{
	/** holds the elements of the stack */
	private List<E> elements;
	
	/** Constructs an ArrayStack element */
	public ArrayStack()
	{
		elements = new ArrayList<E>();
	}
	
	/** Checks if the stack is empty
	 *  @return whether or not the stack is empty
	 */
	public boolean isEmpty()
	{
		return elements.size() == 0;
	}
	
	/** Returns the topmost element of the stack
	 *  @return the topmost element of the stack
	 */
	public E peek()
	{
		return elements.get(elements.size() - 1);
	}
	
	/** Places a given element at the top of the stack
	 *  @param obj the element to be pushed to the stack
	 */
	public void push(E obj)
	{
		elements.add(obj);
	}
	
	/** Returns and removes the topmost element of the stack
	 *  @return the topmost element of the stack
	 */
	public E pop()
	{
		return elements.remove(elements.size() - 1);
	}
	
	/** Tests the ArrayStack class
	 *  @param args not used
	 */
	public static void main(String[] args)
	{
		ArrayStack<String> myStack = new ArrayStack<String>();
		
		myStack.push("First");
		myStack.push("Second");
		myStack.push("Third");
		
		System.out.println("\n\n\n");
		System.out.println(myStack.isEmpty());
		System.out.println(myStack.peek());
		System.out.println(myStack.pop());
		System.out.println(myStack.peek());
		System.out.println(myStack.pop());
		System.out.println(myStack.peek());
		System.out.println(myStack.pop());
		System.out.println(myStack.isEmpty());
		System.out.println("\n\n\n");
	}
}
