/**
 *  Identifier.java
 *  
 *	A class that can be used to represent a (double) variable by storing
 *  the name of the variable and the (double) value it holds.
 *
 *	@author	Krish Agarwal
 *	@since	3/2/2020
 */

public class Identifier
{
	private String name; // the name of the identifier
	private double value; // the value associated with the identifier
	
	// constructor
	public Identifier (String name, double value)
	{
		this.name = name;
		this.value = value;
	}
	
	/**
	 *  Sets the value of the identifier to the parameter
	 *  @param value the value to set
	 */
	public void setValue(double value)
	{
		this.value = value;
	}
	
	/**
	 *  Returns the value associated with the identifier
	 *  @return the value of the identifier
	 */
	public double getValue()
	{
		return value;
	}
	
	/**
	 *  Returns the name of the identifier
	 *  @return the name of the identifier
	 */
	public String getName()
	{
		return name;
	}
}
