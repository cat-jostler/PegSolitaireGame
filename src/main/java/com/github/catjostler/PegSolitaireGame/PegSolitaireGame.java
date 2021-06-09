package com.github.catjostler.PegSolitaireGame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PegSolitaireGame 
{	
	//TODO: break gameboard and user interface out into their own classes. Maybe.
	//this is basically a "fill in the blanks and connect the dots" exercise - I used the skeleton provided on the project page
	
	/**
	 * This method is responsible for everything from displaying the opening 
	 * welcome message to printing out the final thank you.  It will clearly be
	 * helpful to call several of the following methods from here, and from the
	 * methods called from here.  See the Sample Runs below for a more complete
	 * idea of everything this method is responsible for.
	 * 
	 * @param args - any command line arguments may be ignored by this method.
	 */
	public static void main(String[] args)
	{
		Map<String, String> prompts = Prompts.getInstance().prompts;
		
		Scanner scanner = new Scanner(System.in);
		//"Always break [strings to be output] with the concatenation operator to start the next line for readability."
		//holy cow Eclipse does it automatically if you press enter within the quotes!!
		System.out.println("Welcome to the game!\n"
				+ "Please pick a board style:\n"
				+ "1 - Cross\n"
				+ "2 - Circle\n"
				+ "3 - Triangle\n"
				+ "4 - Simple T");
				
		char[][] currentBoard;
		
		//if the user is selecting a board, min = 1 and max = 4
		int boardType = readValidInt(scanner, prompts.get("boardTypePrompt"), 1, 4);
		currentBoard = createBoard(boardType);
		displayBoard(currentBoard);
		
		while (countMovesAvailable(currentBoard) != 0) {
			int[] moveArray = readValidMove(scanner, currentBoard);
			performMove(currentBoard, moveArray[1], moveArray[0], moveArray[2]);
			displayBoard(currentBoard);
		}
		
		if (countPegsRemaining(currentBoard) == 1) {
			System.out.println("You won!");
		}
		else {
			System.out.println("No more legal moves. Please try again.");
		}
		
		System.out.println("Thanks for playing!");
	}
		
	/**
	 * This method is used to read in all inputs from the user.  After printing
	 * the specified prompt, it will check whether the user�s input is in fact
	 * an integer within the specified range.  If the user�s input does not 
	 * represent an integer or does not fall within the required range, print
	 * an error message asking for a value within that range before giving the
	 * user another chance to enter valid input.  The user should be given as
	 * many chances as they need to enter a valid integer within the specified
	 * range.  See the Sample Runs to see how these error messages should be 
	 * phrased, and to see how the prompts are repeated when multiple invalid 
	 * inputs are entered by the user.
	 * 
	 * @param in - user input from standard in is ready through this.
	 * @param prompt - message describing what the user is expected to enter.
	 * @param min - the smallest valid integer that the user may enter.
	 * @param max - the largest valid integer that the user may enter.
	 * @return - the valid integer between min and max entered by the user.
	 */
	public static int readValidInt(Scanner in, String prompt, int min, int max)
	{
		System.out.println(prompt);
		
		String userInput;
		int input;
		
		while(true) {
			userInput = in.nextLine();
			
			String regexPattern = "^([" + + min + "-" + max + "]){1}$";
			Pattern validInput = Pattern.compile(regexPattern);
			Matcher matcher = validInput.matcher(userInput);
			boolean inputIsValid = matcher.find();
			
			if (inputIsValid && !(Integer.parseInt(userInput) < 0)) {
				input = Integer.parseInt(userInput);
				break;
			}
			else {
				System.out.println("Please enter an integer between " + min + " and " + max + ":");
			}
		}
		
		return input;
	}

	/**
	 * This method creates, initializes, and then returns a rectangular two 
	 * dimensional array of characters according to the specified boardType.  
	 * Initial configurations for each of the possible board types are depicted
	 * below.  Note that pegs are displayed as @s, empty holes are displayed as
	 * -s, and extra blank positions that are neither pegs nor holes within 
	 * each rectangular array are displayed as #s.
	 * 
	 * @param boardType - 1-4 indicating one of the following initial patterns:
	 *   1) Cross
	 *     ###@@@###
	 *     ###@@@###
	 *     @@@@@@@@@
	 *     @@@@-@@@@
	 *     @@@@@@@@@
	 *     ###@@@###
	 *     ###@@@###
	 *     
	 *   2) Circle
	 *     #-@@-#
	 *     -@@@@-
	 *     @@@@@@
	 *     @@@@@@
	 *     -@@@@-
	 *     #-@@-#
	 *     
	 *   3) Triangle
	 *     ###-@-###
	 *     ##-@@@-##
	 *     #-@@-@@-#
	 *     -@@@@@@@-
	 *     
	 *   4) Simple T
	 *     -----
	 *     -@@@-
	 *     --@--
	 *     --@--
	 *     -----
	 *     
	 * @return - the fully initialized two dimensional array.
	 */
	public static char[][] createBoard(int boardType)
	{
		//you could algorithmically generate some if not all
		//of these, but why would you want to?
		
		//don't access enums by index because this can break things
		//could use an indexed hashmap
		//received indices should be valid values, as validation is
		//done in readValidInt above
		
		Map<Integer, char[][]> boardMap = new HashMap<Integer, char[][]>(4);
		boardMap.put(1, new char[][] {{'#', '#', '#', '@', '@', '@', '#', '#', '#'},
									  {'#', '#', '#', '@', '@', '@', '#', '#', '#'},
									  {'@', '@', '@', '@', '@', '@', '@', '@', '@'},
									  {'@', '@', '@', '@', '-', '@', '@', '@', '@'},
									  {'@', '@', '@', '@', '@', '@', '@', '@', '@'},
									  {'#', '#', '#', '@', '@', '@', '#', '#', '#'},
									  {'#', '#', '#', '@', '@', '@', '#', '#', '#'}});
		
		boardMap.put(2, new char[][] {{'#','-','@','@','-','#'},
						 			  {'-','@','@','@','@','-'},
						 			  {'@','@','@','@','@','@'},
						 			  {'@','@','@','@','@','@'},
						 			  {'-','@','@','@','@','-'},
						 			  {'#','-','@','@','-','#'}});
		
		boardMap.put(3, new char[][] {{'#','#','#','-','@','-','#','#','#'},
						 			  {'#','#','-','@','@','@','-','#','#'},
						 			  {'#','-','@','@','-','@','@','-','#'},
						 			  {'-','@','@','@','@','@','@','@','-'}});
		
		boardMap.put(4, new char[][] {{'-','-','-','-','-'},
						 			  {'-','@','@','@','-'},
						 			  {'-','-','@','-','-'},
						 			  {'-','-','@','-','-'},
						 			  {'-','-','-','-','-'}});
		
		/**
		 * not sure if this is good practice or necessary
		public char[][] getBoardByIndex(int boardType){
			return boardMap.get(boardType);
		}
		*/
		
		return boardMap.get(boardType);
	}
	
	/**
	 * This method prints out the contents of the specified board using @s to 
	 * represent pegs, -s to represent empty hole, and #s to represent empty 
	 * positions that are neither pegs nor holes.  In addition to this, the 
	 * columns and rows of this board should be labelled with numbers starting 
	 * at one and increasing from left to right (for column labels) and from 
	 * top to bottom (for row labels).  See the Sample Runs for examples of how
	 * these labels appear next to boards with different dimensions.
	 * 
	 * @param board - the current state of the board being drawn.
	 */
	public static void displayBoard(char[][] board)
	{
		//initialize an array with one extra row and two extra columns for numbers of same
		//the second column formats the board more like the board in the examples
		char[][] displayedBoard = new char[board.length+1][board[0].length+2];
		
		//and add the numbers
		//the top row of numbers will all be displayedBoard[0][COLUMN_NUMBER]
		//the first column of numbers will all be displayedBoard[ROW_NUMBER][0]
		
		//you can't think about this great right now so let's do the top row separately
		for (int columnNumber = 0; columnNumber < displayedBoard[0].length; columnNumber++) {
			if (columnNumber == 0 || columnNumber == 1) {
				displayedBoard[0][columnNumber] = ' ';
			}
			else {
				displayedBoard[0][columnNumber] = (char)(48+(columnNumber-1));
			}
		}
		
		//the top row is taken care of, so you can start at row 1
		for (int row = 1; row < displayedBoard.length; row++) {
			for (int column = 0; column < displayedBoard[row].length; column++) {
				if (column == 0) {
					//direct cast to char does not work here because (char)i attempts to display "the char with the ASCII value of i"
					displayedBoard[row][column] = (char)(48+row); //this works right, don't touch
				}
				else if (column == 1) {
					displayedBoard[row][column] = ' ';
				}
				else {
					displayedBoard[row][column] = board[row-1][column-2];
				}
			}
		}
		//using System.out.println(Arrays.deepToString(displayedBoard)) won't display properly
		//so loop through displayedBoard and print each row
		//should I have a separate method for this??
		//doesn't currently work as intended with String.join(", Arrays.toString(displayedBoard[i]))
		//probably because I was expecting this to work as it does in Javascript
		for (int row = 0; row < displayedBoard.length; row++) {
			String displayRow = "";
			for (int column = 0; column < displayedBoard[row].length; column++) {
				displayRow += displayedBoard[row][column];
			}
			System.out.println(displayRow);
		}
	}
	
	/**
	 * Quick debugging method to display the actual board
	 * */
	
	public static void displayActualBoard(char[][] board) {
		for (int row = 0; row < board.length; row++) {
			String displayedRow = "";
			for (int column = 0; column < board[row].length; column++) {
				displayedRow += board[row][column];
			}
			System.out.println(displayedRow);
		}
	}
	
	/**
	 * This method is used to read in and validate each part of a user�s move 
	 * choice: the row and column that they wish to move a peg from, and the 
	 * direction that they would like to move/jump that peg in.  When the 
	 * player�s row, column, and direction selection does not represent a valid
	 * move, your program should report that their choice does not constitute a
	 * legal move before giving them another chance to enter a different move.  
	 * They should be given as many chances as necessary to enter a legal move.
	 * The array of three integers that this method returns will contain: the 
	 * user�s choice of column as the first integer, their choice of row as the
	 * second integer, and their choice of direction as the third.
	 * 
	 * @param in - user input from standard in is ready through this.
	 * @param board - the state of the board that moves must be legal on.
	 * @return - the user's choice of column, row, and direction representing
	 *   a valid move and store in that order with an array.
	 */
	public static int[] readValidMove(Scanner in, char[][] board)
	{
		Map<String, String> prompts = Prompts.getInstance().prompts;
		
		int column = 0;
		int row = 0;
		int direction = 0;
		
		column = readValidInt(in, prompts.get("pegColumnPrompt"), 1, board[0].length);
		
		row = readValidInt(in, prompts.get("pegRowPrompt"), 1, board.length);
		
		direction = readValidInt(in, prompts.get("directionChoicePrompt"), 1, 4);
		
		int[] moveArray = new int[] {column, row, direction};
		System.out.println(Arrays.toString(moveArray));
		return moveArray;
	}
	
	/**
	 * This method checks whether a specific move (column + row + direction) is
	 * valid within a specific board configuration.  In order for a move to be
	 * a valid: 1)there must be a peg at position row, column within the board,
	 * 2)there must be another peg neighboring that first one in the specified
	 * direction, and 3)there must be an empty hole on the other side of that
	 * neighboring peg (further in the specified direction).  This method
	 * only returns true when all three of these conditions are met.  If any of
	 * the three positions being checked happen to fall beyond the bounds of 
	 * your board array, then this method should return false.  Note that the 
	 * row and column parameters here begin with one, and may need to be 
	 * adjusted if your programming language utilizes arrays with zero-based 
	 * indexing.
	 * 
	 * @param board - the state of the board that moves must be legal on.
	 * @param row - the vertical position of the peg proposed to be moved.
	 * @param column - the horizontal position of the peg proposed to be moved.
	 * @param direction - the direction proposed to move/jump that peg in.
	 * @return - true when the proposed move is legal, otherwise false.
	 */
	public static boolean isValidMove(char[][] board, int row, int column, int direction)
	{
		
		//remember the user input will be too large by 1 (row) and 2 (column)
		//because the user sees and enters input based on the board returned by displayBoard()
		//int adjRow = row;
		//int adjCol = column - 1;
		
		//if no peg at board[row][column], return false
		if (board[row][column] != '@') {
			return false;
		}
		
		//the other three conditions depend on direction
		//handle bounds checks first
		switch (direction) {
		case 1: //up - row-n
			if (row - 2 < 0) {
				return false;
			}
			else if (board[row - 1][column] == '@' && board[row - 2][column] == '-') {
				return true;
			}
			else {
				return false;
			}
		case 2: //down - row+n
			if (row + 2 > board.length) {
				return false;
			}
			else if (board[row + 1][column] == '@' && board[row + 2][column] == '-') {
				return true;
			}
			else {
				return false;
			}
		case 3: //left - column-n
			if (column - 2 < 0) {
				return false;
			}
			else if (board[row][column - 1] == '@' && board[row][column - 2] == '-') {
				return true;
			}
			else {
				return false;
			}
		case 4: //right - column+n
			if (column + 2 > board[0].length) {
				return false;
			}
			else if (board[row][column + 1] == '@' && board[row][column + 2] == '-') {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * The parameters of this method are the same as those of the isValidMove()
	 * method.  However this method changes the board state according to this
	 * move parameter (column + row + direction), instead of validating whether
	 * the move is valid.  If the move specification that is passed into this
	 * method does not represent a legal move, then do not modify the board.
	 * 
	 * @param board - the state of the board will be changed by this move.
	 * @param row - the vertical position that a peg will be moved from.
	 * @param column - the horizontal position that a peg will be moved from.
	 * @param direction - the direction of the neighbor to jump this peg over.
	 * @return - the updated board state after the specified move is taken.
	 */
	public static char[][] performMove(char[][] board, int row, int column, int direction)
	{
		
		if (!isValidMove(board, row, column, direction)) {
			System.out.println("Sorry, that's not a valid move.");
			return board; //early exit - return board unchanged if invalid move
		}
		
		//remember the user input will be too large by 1 (row) and 2 (column)
		//because the user sees and enters input based on the board returned by displayBoard()
		int adjRow = row - 1;
		int adjCol = column - 2;		
		
		//a successful move will:
		// - leave a - at board[i][j]
		// - turn the jumped @ in DIRECTION into a -
		// - turn the destination - into a @
		
		switch (direction) {
			case 1: //up - row-n
				board[adjRow][adjCol] = '-';
				board[adjRow - 1][adjCol] = '-';
				board[adjRow - 2][adjCol] = '@';
				break;
			case 2: //down - row+n
				board[adjRow][adjCol] = '-';
				board[adjRow + 1][adjCol] = '-';
				board[adjRow + 2][adjCol] = '@';				
				break;
			case 3: //left - column-n
				board[adjRow][adjCol] = '-';
				board[adjRow][adjCol - 1] = '-';
				board[adjRow][adjCol - 2] = '@';				
				break;
			case 4: //right - column+n
				board[adjRow][adjCol] = '-';
				board[adjRow][adjCol + 1] = '-';
				board[adjRow][adjCol + 2] = '@';				
				break;
		}
		
		return board;
	}
	
	/**
	 * This method counts up the number of pegs left within a particular board 
	 * configuration, and returns that number.
	 * 
	 * @param board - the board that pegs are counted from.
	 * @return - the number of pegs found in that board.
	 */
	public static int countPegsRemaining(char[][] board)
	{
		int numPegs = 0;
		
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; row++) {
				if (board[row][column] == '@') {
					numPegs++;
				}
			}
		}
		
		return numPegs;
	}
	
	/**
	 * This method counts up the number of legal moves that are available to be
	 * performed in a given board configuration.
	 * 
	 * HINT: Would it be possible to call the isValidMove() method for every
	 * direction and from every position within your board?  Counting up the
	 * number of these calls that return true should yield the total number of
	 * moves available within a specific board.
	 * 
	 * @param board - the board that possible moves are counted from.
	 * @return - the number of legal moves found in that board.
	 */
	public static int countMovesAvailable(char[][] board)
	{
		
		int possibleMoves = 0;
		
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				for (int direction = 1; direction <= 4; direction++) {
					//man this is ugly :)
					if (isValidMove(board, row, column, direction)) {
						possibleMoves++;
					}
				}
			}
		}

		return possibleMoves;
	}	

}