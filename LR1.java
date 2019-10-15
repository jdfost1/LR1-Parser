
/*
 *@author Josh Foster
 * Asg 2 - LR1 Parser
 * 10/09/2019
 * Dr. Li 
 * IT 327
 * 
 * Left to Right Scanning of input stream
 * Right- Most Derivation
 * 1 token look ahead to make decisions
 * 
 * non-recursive,shift-reduce, bottom-up parser
 * 
 * Given an input string from the user, this LR(1) program will parse an expression to tell whether or not it is a 
 * valid expression. This program will also print out the contents of the stack during each step of the parsing.
 */
import java.util.Stack;

public class LR1 {

	// input string provided by the user
	private static String input;

	// variable used to break input string into individual tokens
	private static String token;

	// pointer to keep track of the current index of the input
	private static int pointer;

	// variable to keep track of the current state of the parser
	private static int state;

	// variable to exit loop when parse is accepted and complete
	private static boolean complete;

	// stack used to hold states and tokens for parsing
	private static Stack<String> stack;

	public enum Symbol {
		E, T, F;
	}

	// create object for nonterminals so that they are provided with a value as
	// well
	class Nonterminal {
		public char symbol;
		public int value;

		Nonterminal(char symbol, int value) {
			this.symbol = symbol;
			this.value = value;
		}
	};

	//pop token off of the stack
	private static String pop() {
		String result = stack.pop();
		
		// print all content of the stack and the remaining input string to be parsed
		System.out.println(stack.toString() + "    " + input.substring(pointer) + "\n");
		return result;
	}

	//push token onto the stack
	private static void push(String s) {
		stack.push(s);

		// print all content of the stack and the remaining input string to be parsed
		System.out.println(stack.toString() + "    " + input.substring(pointer) + "\n");
	}

	// method to check if input char is a terminal symbol
	private static boolean isTerminal(char c) {
		switch (c) {
		case '+':
		case '-':
		case '*':
		case '/':
		case '(':
		case ')':
		case '$':
			return true;
		default:
			return false;
		}
	}// end of isTerminal method

	private static void setToken() {
		if (isTerminal(input.charAt(pointer))) {
			token = "" + input.charAt(pointer);
			pointer++;
		} else if (Character.isDigit(input.charAt(pointer))) {
			token = "";

			// ensure that if there is more digits after the current digit.. add
			// them to complete the token (multiple digit #)
			while (Character.isDigit(input.charAt(pointer))) {
				token += input.charAt(pointer);
				pointer++;
			} // end of while
		} // end of else if

		// if character from current index of input is not a non terminal symbol
		// or a digit then.. invalid input. not accepted
		else {
			Invalid();
		}
	}// end of setToken method

	private static boolean tokenIs(char c) {
		if (token.charAt(0) == c)
			return true;
		return false;
	}// end of tokenIs method

	// parsing algorithm
	private static void parse() {
		
		//do not pop value
		String temp = stack.peek();

		if (Character.isDigit(temp.charAt(0)))
			state = Integer.parseInt(temp);
		else
			state = -1;

		switch (state) {

		// *************@ STATE 0 ***************************
		case 0: {
			char tempChar = token.charAt(0);

			if (Character.isDigit(tempChar)) {
				// shift 5
				push(token);
				push("5");
				setToken();
			} else if (tempChar == '(') {
				// shift 4
				push(token);
				push("4");
				setToken();
			} else {
				Invalid();
			}

			break;
		} // end of state 0

		// *************@ STATE 1 ***************************
		case 1: {
			char tempChar = token.charAt(0);

			switch (tempChar) {
			case '+':
				// shift 6
				push(token);
				push("6");
				setToken();
				break;
			case '-':
				// shift 6
				push(token);
				push("6");
				setToken();
				break;
			case '$':
				complete = true;

				break;
			default:
				Invalid();
			}
			break;
		} // end of state 1

		// *************@ STATE 2 ***************************
		case 2: {

			char tempChar = token.charAt(0);

			switch (tempChar) {
			case '+':
				// E->T
				pop();
				if (pop().charAt(0) != 'T') {
					Invalid();
				}
				pushE();
				break;
			case '-':
				// E->T
				pop();
				if (pop().charAt(0) != 'T') {
					Invalid();
				}
				pushE();
				break;
			case '*':
				// shift 7
				push(token);
				push("7");
				setToken();
				break;
			case '/':
				// shift 7
				push(token);
				push("7");
				setToken();
				break;
			case ')':
			case '$':
				// E->T
				pop();
				if (pop().charAt(0) != 'T') {
					Invalid();
				}
				pushE();
				break;
			default:
				Invalid();
			}
			break;
		} // end of state 2

		// *************@ STATE 3 ***************************
		case 3: {
			char tempChar = token.charAt(0);

			switch (tempChar) {
			case '+':
				// T->F
				pop();
				if (pop().charAt(0) != 'F')
					Invalid();
				pushT();
				break;
			case '-':
				// T->F
				pop();
				if (pop().charAt(0) != 'F')
					Invalid();
				pushT();
				break;
			case '*':
				// T->F
				pop();
				if (pop().charAt(0) != 'F')
					Invalid();
				pushT();
				break;
			case '/':
				// T->F
				pop();
				if (pop().charAt(0) != 'F')
					Invalid();
				pushT();
				break;
			case ')':
			case '$':
				// T->F
				pop();
				if (pop().charAt(0) != 'F')
					Invalid();
				pushT();
				break;
			default:
				Invalid();
			}
			break;
		} // end of state 3

		// *************@ STATE 4 ***************************
		case 4: {
			char tempChar = token.charAt(0);

			if (Character.isDigit(tempChar)) {
				// shift 5
				push(token);
				push("5");
				setToken();
			} else if (tempChar == '(') {
				// shift 4
				push(token);
				push("4");
				setToken();
			} else
				Invalid();

			break;
		} // end of state 4

		// *************@ STATE 5 ***************************
		case 5: {
			char tempChar = token.charAt(0);

			switch (tempChar) {
			case '+':
				// F->n
				pop();
				if (!Character.isDigit(pop().charAt(0))) {
					Invalid();
				}
				pushF();
				break;
			case '-':
				// F->n
				pop();
				if (!Character.isDigit(pop().charAt(0))) {
					Invalid();
				}
				pushF();
				break;
			case '*':
				// F->n
				pop();
				if (!Character.isDigit(pop().charAt(0))) {
					Invalid();
				}
				pushF();
				break;
			case '/':
				// F->n
				pop();
				if (!Character.isDigit(pop().charAt(0))) {
					Invalid();
				}
				pushF();
				break;
			case ')':
			case '$':
				// F->n
				pop();
				if (!Character.isDigit(pop().charAt(0))) {
					Invalid();
				}
				pushF();
				break;
			default:
				Invalid();
			}
			break;
		} // end of state 5

		// *************@ STATE 6 ***************************
		case 6: {
			char tempChar = token.charAt(0);

			if (Character.isDigit(tempChar)) {
				// shift 5
				push(token);
				push("5");
				setToken();
			} else if (tempChar == '(') {
				// shift 4
				push(token);
				push("4");
				setToken();
			} else {
				Invalid();
			}
			break;
		} // end of state 6

		// *************@ STATE 7 ***************************
		case 7: {
			char tempChar = token.charAt(0);

			if (Character.isDigit(tempChar)) {
				// shift 5
				push(token);
				push("5");
				setToken();
			} else if (tempChar == '(') {
				// shift 4
				push(token);
				push("4");
				setToken();
			} else {
				Invalid();
			}
			break;
		} // end of state 7

		// *************@ STATE 8 ***************************
		case 8: {
			char tempChar = token.charAt(0);

			switch (tempChar) {
			case '+':
				// shift 6
				push(token);
				push("6");
				setToken();
				break;
			case '-':
				// shift 6
				push(token);
				push("6");
				setToken();
				break;
			case ')':
				// shift 11
				push(token);
				push("11");
				setToken();
				break;
			default:
				Invalid();
			}
			break;
		} // end of state 8

		// *************@ STATE 9 ***************************
		case 9: {
			if (tokenIs('+') || tokenIs(')') || tokenIs('$')) {

				pop();
				if (pop().charAt(0) != 'T') {
					Invalid();
				}
				pop();
				if (pop().charAt(0) != '+') {
					Invalid();
				}
				pop();
				if (pop().charAt(0) != 'E') {
					Invalid();
				}
				pushE();
			} else if (tokenIs('*')) {
				// shift 7
				push(token);
				push("7");
				setToken();
			} else
				Invalid();
			break;
		} // end of state 9

		// *************@ STATE 10 ***************************
		case 10: {
			if (tokenIs('+') || tokenIs('*') || tokenIs(')') || tokenIs('$')) {
				// T->T*F
				pop();
				if (pop().charAt(0) != 'F') {
					Invalid();
				}
				pop();
				if (pop().charAt(0) != '*') {
					Invalid();
				}
				pop();
				if (pop().charAt(0) != 'T') {
					Invalid();
				}
				pushT();
			} else
				Invalid();

			break;
		} // end of state 10

		// *************@ STATE 11 ***************************
		case 11: {
			char tempChar = token.charAt(0);

			switch (tempChar) {
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				// F->(E)
				pop();
				if (pop().charAt(0) != ')') {
					Invalid();
				}
				pop();
				if (pop().charAt(0) != 'E') {
					Invalid();
				}
				pop();
				if (pop().charAt(0) != '(') {
					Invalid();
				}
				pushF();
				break;
			default:
				Invalid();
			}
			break;
		} // end of state 11

		// if not state 0-11 then input is invalid
		default:
			Invalid();

		}// end of switch case
	}// end of parse method

	private static void pushE() {
		// E -> E + T | E - T | T
		String temp = stack.peek();

		push("E");

		if (temp.equals("0"))
			push("1");
		else if (temp.equals("4"))
			push("8");
		else
			Invalid();
	}// end of pushE method

	private static void pushT() {
		// T -> T * F | T / F | F
		String temp = stack.peek();

		push("T");

		if (temp.equals("0") || temp.equals("4"))
			push("2");
		else if (temp.equals("6"))
			push("9");
		else
			Invalid();
	}// end of pushT method

	private static void pushF() {
		// F -> (E) | n
		String temp = stack.peek();

		push("F");

		if (temp.equals("0") || temp.equals("4") || temp.equals("6"))
			push("3");
		else if (temp.equals("7"))
			push("10");
		else
			Invalid();
	}// end of pushF method

	// if input is not accepted by the parser.. expression is invalid..
	// terminate program
	private static void Invalid() {
		System.out.println("Invalid Expression: This input is not accepted by the parser");

		// terminate program
		System.exit(-1);

	}// end of invalid method

	// Driver Code
	public static void main(String[] args) {
		// initialize new stack for parsing
		stack = new Stack<String>();
		pointer = 0;
		state = -1;
		complete = false; // set to false to continue loop until input is
							// accepted and complete

		// grab input passed from user in commmand line
		input = args[0];
		
		//input = "(1+7)"; // input for testing

		// add $ to end of input to let parser know end of input string
		input = input.concat("$");

		// push starting state of value 0 onto the parsing stack to begin parsing
		push("0");
		setToken();

		// keep parsing until complete is set to true. Complete when parser gets
		// $ when at state 1
		while (!complete)
			parse();

		System.out.println("Valid Expression: This input was accepted by the parser");

	}// end of main method

}// end of class
