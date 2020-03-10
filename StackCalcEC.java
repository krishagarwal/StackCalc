/**
 *  StackCalcEC.java
 *  
 *	A program that takes input mathematical expressions and evaluates the
 *  expressions. The program uses the ArrayStack implementation of a stack,
 *  and uses 2 such stacks, one to store the numerican values and the other
 *  to store the set of operators. The most basic operation is to use a
 *  pop-pop-pop, which extracts two values from the value stack and one
 *  operator from the operator stack, performing the operation on the two
 *  values (note: the preceding operand is the second operand to be "popped"
 *  from the value stack), and pushing the new value to the value stack.
 *  The program decides when to perform this operation during the parsing of
 *  the expression, and when the expression is fully parsed, the program
 *  performs this operation until the stacks are empty, thus yielding the
 *  result of the expression. Also supports variable storage by setting
 *  variablles (idetifiers) equal to values or expressions that are entered
 *  by the user.
 *
 *	@author	Krish Agarwal
 *	@since	3/2/2020
 */

import java.util.List;		// used by expression evaluator
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class StackCalcEC
{
	private ExprUtils utils;	// expression utilities
	
	private ArrayStack<Double> valueStack;		// value stack
	private ArrayStack<String> operatorStack;	// operator stack
	private List<Identifier> identifiers;       // list of identifiers
	private boolean failed; 					// status of expression evaluation success

	// constructor	
	public StackCalcEC() 
	{
		utils = new ExprUtils();
		valueStack = new ArrayStack<Double>();
		operatorStack = new ArrayStack<String>();
		identifiers = new ArrayList<Identifier>();
		identifiers.add(new Identifier("pi", Math.PI));
		identifiers.add(new Identifier("e", Math.E));
		failed = false;
	}
	
	/** Runs the program.
	 *  @param args not used
	 */
	public static void main(String[] args) 
	{
		StackCalcEC sc = new StackCalcEC();
		sc.run();
	}
	
	/** Introduces and exits the program. */
	public void run() 
	{
		System.out.println("\n\n\nWelcome to StackCalc!!!\n");
		runCalc();
		System.out.println("\nThanks for using StackCalc! Goodbye.\n\n\n");
	}
	
	/**
	 *	Prompt the user for expressions, run the expression evaluator,
	 *	and display the answer.
	 */
	public void runCalc() 
	{
		boolean run = true;
		while (run)
		{
			failed = false;
			String in = Prompt.getString("->");
			if (in.equals("h"))
				printHelp();
			else if (in.equals("q"))
				run = false;
			else if (in.equals("l"))
				printVars();
			else
			{
				List<String> tokens = utils.tokenizeExpression(in);
				int index = getEqualSignIndex(tokens);
				String varName = "";
				if (index == 1)
					varName = tokens.get(0);
				double value = evaluateExpression(tokens);
				if (!failed)
				{
					if (index == 1)
						System.out.print(varName + " = ");
					System.out.println(value);
				}
				else
					System.out.println("Not a valid expression. Try again.");
			}
		}
	}
	
	/**
	 *  Finds the idex of the equal sign token in the given list of tokens
	 *  @param tokens the tokens to check against
	 */
	private int getEqualSignIndex(List<String> tokens)
	{
		for (int i = 0; i < tokens.size(); i++)
		{
			String token = tokens.get(i);
			if (token.length() == 1 && token.charAt(0) == '=')
				return i;
		}
		return -1;
	}
	
	/**	Print help */
	public void printHelp() 
	{
		System.out.println("\nHelp:");
		System.out.println("  h - this message\n  q - quit\n  l - list variables");
		System.out.println("Expressions can contain:");
		System.out.println("  integers or decimal numbers");
		System.out.println("  arithmetic operators +, -, *, /, %, ^");
		System.out.println("  parentheses '(' and ')'\n");
	}
	
	/**	Print help */
	public void printVars() 
	{
		System.out.println("\nVariables:");
		for (Identifier var : identifiers)
			System.out.printf("  %s = %.2f\n", var.getName(), var.getValue());
		System.out.println();
	}
	
	/**
	 *	Evaluate expression and return the value
	 *	@param tokens	a List of String tokens making up an arithmetic expression
	 *	@return			a double value of the evaluated expression
	 */
	public double evaluateExpression(List<String> tokens) 
	{
		double value = 0.0;
		try
		{
			int index = getEqualSignIndex(tokens);
			if (index >= 0 && index != 1)
				return clearExpr();
			String varName = "";
			if (index >= 0)
			{
				varName = tokens.remove(0);
				if (!isValidVarName(varName))
					return clearExpr();
				tokens.remove(0);
			}
			for (String token : tokens)
			{
				boolean isOperator = token.length() == 1 && utils.isOperator(token.charAt(0));
				if (!isOperator && isNumber(token))
					valueStack.push(Double.parseDouble(token));
				else if (!isOperator)
				{
					Identifier var = findIdentifier(token);
					if (var == null)
						valueStack.push(0.0);
					else
						valueStack.push(var.getValue());
				}
				else if (token.equals(")"))
				{
					while (!operatorStack.peek().equals("("))
						performPopPopPop();
					operatorStack.pop();
				}
				else
				{
					while (!token.equals("(") && !operatorStack.isEmpty() && hasPrecedence(token, operatorStack.peek()))
						performPopPopPop();
					operatorStack.push(token);
				}
			}
			while (!operatorStack.isEmpty())
				performPopPopPop();
			value = valueStack.pop();
			if (!valueStack.isEmpty())
				return clearExpr();
			if (index >= 0 && findIdentifier(varName) != null)
				findIdentifier(varName).setValue(value);
			else if (index >= 0)
				identifiers.add(new Identifier(varName, value));
		}
		catch (Exception e)
		{
			return clearExpr();
		}
		return value;
	}

	/**
	 *  Checks if the given string is a valid variable name
	 *  @param name the string to check
	 *  @return whether or not the string is a valid variable name
	 */
	public boolean isValidVarName(String name)
	{
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);
			if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')))
				return false;
		}
		return true;
	}

	/**
	 *  Clears the expression by emptying both stacksbeing used
	 *  @return the default value of the expression (0)
	 */
	public int clearExpr()
	{
		while (!valueStack.isEmpty())
			valueStack.pop();
		while (!operatorStack.isEmpty())
			operatorStack.pop();
		failed = true;
		return 0;
	}
	
	/** Performs a pop-pop-pop operation on the stacks */
	private void performPopPopPop()
	{
		double operand2 = valueStack.pop();
		valueStack.push(performOperation(valueStack.pop(), operatorStack.pop(), operand2));
	}
	
	/**
	 *  Loosely checks if the given number can be parsed into a numeric
	 *  value (does not vork for strings with multiple decimals)
	 *  @param num the string to check
	 *  @return whether or not the string can be parsed into a numeric value
	 */
	private boolean isNumber(String num)
	{
		for (int i = 0; i < num.length(); i++)
		{
			char c = num.charAt(i);
			if (c > '9' || c < '0' && c != '.')
				return false;
		}
		return true;
	}
	
	/**
	 *  Finds the identifier in the list of identifiers given the name.
	 *  @param name the name of the identifier
	 *  @return the identifier from the list
	 */
	private Identifier findIdentifier(String name)
	{
		for (Identifier identifier : identifiers)
		{
			if (identifier.getName().equals(name))
				return identifier;
		}
		return null;
	}
	
	/** Performs a given operation on two given operands
	 * 	@param operand1 the preceding operand
	 *  @param operator the operator
	 *  @param operand2 the second operand
	 */
	private double performOperation(double operand1, String operator, double operand2)
	{
		if (operator.equals("+"))
			return operand1 + operand2;
		if (operator.equals("-"))
			return operand1 - operand2;
		if (operator.equals("*"))
			return operand1 * operand2;
		if (operator.equals("/"))
			return operand1 / operand2;
		if (operator.equals("%"))
			return operand1 % operand2;
		if (!operator.equals("^"))
			throw new NoSuchElementException();
		return Math.pow(operand1, operand2);
	}
	
	/**
	 *	Precedence of operators
	 *	@param op1		operator 1
	 *	@param op2		operator 2
	 *	@return			true if op2 has higher or same precedence as op1; false otherwise
	 *	Algorithm:
	 *		if op1 is exponent, then false
	 *		if op2 is either left or right parenthesis, then false
	 *		if op1 is multiplication or division or modulus and 
	 *				op2 is addition or subtraction, then false
	 *		otherwise true
	 */
	private boolean hasPrecedence(String op1, String op2) 
	{
		if (op1.equals("^")) 
			return false;
		if (op2.equals("(") || op2.equals(")")) 
			return false;
		if ((op1.equals("*") || op1.equals("/") || op1.equals("%")) && (op2.equals("+") || op2.equals("-")))
			return false;
		return true;
	}
}
