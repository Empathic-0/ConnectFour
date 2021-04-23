import java.util.Random;
import java.util.Scanner;
/**
 * This class contains member methods and instance variables <br> 
 * which are used to support the program that runs in the main <br>
 * method. The program that runs is a connect four game which <br>
 * uses a rudimentary AI system. (The AI DOES NOT always win).
 * @author Alexander Mackey
 */
public class ConnectFour 
{
	private final static int NUM_ROWS = 6;		//Number of rows on the game board
	private final static int NUM_COLS = 7;		//Number of columns on the game board
	private static boolean currentMoveIsPlayer; //Used to track player turns, true if player's turn, false if AI's turn
	private static boolean endOfGame = false;   //Default end state, set to false so game can continue, track game state
	private static int playerMoveChoice;        //Default initialization to avoid problems, variable to store column position
	private static int pieceTracker = 0;	    //Used to track player pieces: 1 for human player, 2 for AI, 0 for empty 
	private static int[][] gameBoard = new int[NUM_ROWS][NUM_COLS]; //Game board initialization
	private static int isFullCounter = 0;		//Used to track remaining moves left in the game, used to declared draw 
	private static int lastPlayerMove = 0;		//Used to provide the AI with the last move made by the human player
	private static boolean flag = false;		//Used as a flag to prevent invalid move overflow
	public static void drawBoard(int[][] gameBoard)
	{
		 for(int rows=0; rows < gameBoard.length; rows++)
		 {
			 	System.out.print(NUM_ROWS - rows - 1);
	            for(int cols=0; cols < gameBoard[0].length; cols++)
	            {
	            	
	                if(gameBoard[rows][cols] == 0)
	                    {System.out.print("[ ]");}
	                else if(gameBoard[rows][cols] == 1)
	                    {System.out.print("[X]");}
	                else if(gameBoard[rows][cols] == 2)
	                 	{System.out.print("[O]");}
	                else
	                    System.out.print(gameBoard[rows][cols]+"  ");
	            }
	            System.out.println();
	      }
	        for(int i=0;i<NUM_COLS;i++)
	        {
	            System.out.print("  "+ (i));
	        }
	        System.out.println();
	}
/**
 * This method is used to access the value of the flag variable <br>
 * so the AI can stop infinite move choice loop.
 * @return returns a boolean value, true if flag is thrown, false otherwise.
 */
	public static boolean getFlag()
	{
		return flag;
	}
/**
 * This method is used to access the number of rows on the game board.
 * @return returns the number of rows on the game board as an integer value. 
 */
	public static int getRows()
	{
		return NUM_ROWS;
	}
/**
 * This method is used to access the number of columns on the game board.
 * @return returns the number of columns on the game board as an integer value.
 */
	public static int getCols()
	{
		return NUM_COLS;
	}
/**
 * This method is used as a script to ask the player whether or not they would <br>
 * like to play first. If invalid input is provided the user is prompted again.
 */
	public static void turnChoice()
	{
		@SuppressWarnings("resource") //closing the scanner below causes problems with the player input
		Scanner inputTurn = new Scanner(System.in); //Scanner used to take player input
		System.out.println("Welcome to ConnectFour!");
		System.out.print("Would you like to go first? Enter true if yes, false if no: ");
		while (true) //Loop endlessly if value is not valid
		{
			if (inputTurn.hasNextBoolean())
				{
					currentMoveIsPlayer = inputTurn.nextBoolean();
					break;
				}
			else
				{
					System.out.print("Invalid input please provide a choice (true/false): ");
					inputTurn.next(); //Important! Use to receive next attempt at valid input.
				} 
		}	
	}
/**
 * This method is used by the human player and the AI to drop pieces onto the game board.
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @return returns a boolean value, true if the move is valid, false if the move is not.
 */
	public static boolean dropPiece(int[][] gameGrid, int pieceTracker, int colNum)
	{
		flag = false; //Resets the flag to false
		for (int rows = getRows() - 1; rows >= 0; rows--)
		{
				if (gameGrid[rows][colNum] == 0)
				{
					gameGrid[rows][colNum] = pieceTracker;
					isFullCounter++; //After every successful move this counter is incremented
					return true;
				}
		}
		System.out.println("Sorry column " + colNum + " is full. Please select another!");
		flag = true; //If a column is full a flag is thrown, indicating another column must be chosen
		return false;
	}
/**s
 * This method is used to track whether the win conditions have been met after every move <br>
 * it returns a boolean value which is used to change the game state condition (endOfGame).
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @return returns a boolean value, true if at least 4 consecutive pieces are detected, <br>
 * indicating the game must end, false otherwise.
 */
	public static boolean gameLogic(int[][] gameGrid, int pieceTracker, int colNum)
	{
		int rowToCheck = 0; //Used to store the row when finding the row we need to check for win condition
		//The loop below is used to find the row of last piece dropped to find exact position
		for (int posTracker = 0; posTracker < getRows(); posTracker++)
		{
			if (gameGrid[posTracker][colNum] != 0)
			{
				rowToCheck = posTracker;
				break;
			}
		}
		
		if (checkNorth_South(gameGrid, pieceTracker, colNum, rowToCheck)) {return true;} //Checks columns (VERTICAL)
		if (checkEast_West(gameGrid, pieceTracker, colNum, rowToCheck)) {return true;}  //Checks rows (HORIZONTAL)
		if (checkNorthEast_SouthWest(gameGrid, pieceTracker, colNum, rowToCheck)) {return true;} //Checks minor diagonals
		if (checkNorthWest_SouthEast(gameGrid, pieceTracker, colNum, rowToCheck)) {return true;} //Checks major diagonals
		return false;
	}
/**
 * This method is used to check whether 4 consecutive pieces are detected in a given column.
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @param rowNum is an integer value used to find which row the check must start from <br>
 * in order to determine consecutive pieces.
 * @return returns a boolean value, true if at least 4 consecutive pieces are detected, <br>
 * indicating the game must end, false otherwise.
 */
	public static boolean checkNorth_South(int[][] gameGrid, int pieceTracker, int colNum, int rowNum)
	{
		int streakBonus = 1; //Initial position checked will have a piece
		if ((rowNum + 4) <= getRows())
		{
			for (int tracker = rowNum + 1; tracker <= (rowNum + 3); tracker++)
			{
				if (gameGrid[tracker][colNum] == pieceTracker)  //Increments the streak if consecutive pieces are found
					streakBonus++;
				else
					break;
			}
		}
		if (streakBonus == 4)
			return true; //return true if streak not found
		return false; //if steak is not found
	}
/**
 * This method is used to check whether 4 consecutive pieces are detected in a given row.
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @param rowNum is an integer value used to find which row the check must start from <br>
 * in order to determine consecutive pieces.
 * @return returns a boolean value, true if at least 4 consecutive pieces are detected, <br>
 * indicating the game must end, false otherwise.
 */
	public static boolean checkEast_West(int[][] gameGrid, int pieceTracker, int colNum, int rowNum)
	{
		int streakBonus = 1; //Initial position checked will have a piece
		
		for (int tracker = colNum - 1; tracker >= 0; tracker--)
			if (gameGrid[rowNum][tracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
				streakBonus++; 
			else 
				break;
		if (streakBonus >= 4) {return true;} //return true if streak not found
		
		for (int tracker = colNum + 1; tracker < getCols(); tracker++)
			if (gameGrid[rowNum][tracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
				streakBonus++;
			else 
				break;
		if (streakBonus >= 4) {return true;} //return true if streak not found
		
		return false; //if steak is not found
	}
/**
 * This method is used to check whether 4 consecutive pieces are detected in a the minor diagonal.
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @param rowNum is an integer value used to find which row the check must start from <br>
 * in order to determine consecutive pieces.
 * @return returns a boolean value, true if at least 4 consecutive pieces are detected, <br>
 * indicating the game must end, false otherwise.
*/
	public static boolean checkNorthEast_SouthWest(int[][] gameGrid, int pieceTracker, int colNum, int rowNum) //Minor
	{
		int streakBonus = 1;
		for (int rowTracker = rowNum + 1, colTracker = colNum - 1; colTracker >= 0 && rowTracker < getRows(); rowTracker++ ,colTracker--)
			if (gameGrid[rowTracker][colTracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
				streakBonus++; 
			else 
				break;
		if (streakBonus == 4) {return true;} //return true if streak not found
		
		for (int colTracker = colNum + 1, rowTracker = rowNum - 1; colTracker < getCols() && rowTracker >= 0; rowTracker--, colTracker++)
			if (gameGrid[rowTracker][colTracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
				streakBonus++;
			else 
				break;
		if (streakBonus == 4) {return true;} //return true if streak not found
		
		return false; //if steak is not found
	}
/**
 * This method is used to check whether 4 consecutive pieces are detected in a the major diagonal.
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @param rowNum is an integer value used to find which row the check must start from <br>
 * in order to determine consecutive pieces.
 * @return returns a boolean value, true if at least 4 consecutive pieces are detected, <br>
 * indicating the game must end, false otherwise.
*/
	public static boolean checkNorthWest_SouthEast(int[][] gameGrid, int pieceTracker, int colNum, int rowNum) //Major
	{
		int streakBonus = 1;
		for (int rowTracker = rowNum - 1, colTracker = colNum - 1; colTracker >= 0 && rowTracker >= 0; rowTracker--, colTracker--)
			if (gameGrid[rowTracker][colTracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
				streakBonus++; 
			else 
				break;
		if (streakBonus == 4) {return true;} //return true if streak not found
		
		for (int colTracker = colNum + 1, rowTracker = rowNum + 1; colTracker < getCols() && rowTracker < getRows(); rowTracker++, colTracker++)
			if (gameGrid[rowTracker][colTracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
				streakBonus++;
			else 
				break;
		if (streakBonus == 4) {return true;} //return true if streak not found
		
		return false; //if steak is not found
		
	}

/**
 * This is where the game engine is deployed, it contains the primary <br>
 * game state loop which continues running while the game has no winner <br>
 * or while there are still valid moves.
 * 
 * This is the main method that must be included for each new class. <br>
 * Within the main method is a script that will execute the statements <br>
 * necessary gather user input and to complete the mathematical operations.
 * @param args you can pass n-number of arguments to the program, and convert as you <br>
 * need due to the fact a string can be manipulated into any primitive type in the <br>
 * Java language.*/	
	public static void main(String[] args) 
	{
		Scanner inputMove = new Scanner(System.in); //Scanner used to take player input
		turnChoice();//Ask if player would like to go first
		drawBoard(gameBoard); //Display game board before game starts
		while(!endOfGame) //While the condition is such that the game has not ended, continue to play
		{
			if(currentMoveIsPlayer) 
			{
				System.out.println("(X) Player's Turn ");
				pieceTracker = 1;
				do //This loop is used for user friendly input which keeps asking for valid input
				{
					System.out.print("Please select a valid column to drop a piece (0-6): ");
				    while (!inputMove.hasNextInt()) 
				    {
				    	System.out.print("Invalid input please provide an integer (0-6): ");
				    	inputMove.next(); // this is important!
				    }
				    playerMoveChoice = inputMove.nextInt();
				} 
				while (0 > playerMoveChoice || playerMoveChoice > 6 );
				lastPlayerMove = playerMoveChoice; //Used to track the last move made by the player so the AI can make its move
				System.out.println("Your choice: " + playerMoveChoice); //Confirms the input made by the user
			}
			else
			{
				System.out.println("(O) AI Player's Turn");
				System.out.println("AI has made its move");
						
				Random genRandom = new Random();
				if (AI.ai_NS(gameBoard, pieceTracker, lastPlayerMove))
				{
					playerMoveChoice = AI.getCoords();	
				}
				else if (AI.ai_EW(gameBoard, pieceTracker, lastPlayerMove))
				{
					playerMoveChoice = AI.getCoords();
				}
				else if (AI.ai_NESW(gameBoard, pieceTracker, lastPlayerMove))
				{	
					playerMoveChoice = AI.getCoords();
				}
				else
					playerMoveChoice = genRandom.nextInt(6);
				if (isFullCounter == 0) {
					playerMoveChoice = 3;
				}
				
				pieceTracker = 2;
				
			}
			
			if (dropPiece(gameBoard, pieceTracker, playerMoveChoice))
			{
				currentMoveIsPlayer = !currentMoveIsPlayer; //Swaps value from true to false to change player state	  
			}

			drawBoard(gameBoard);
			if (isFullCounter == 42)
			{
				endOfGame = true;
				System.out.println("Draw");
			}
			
			if (gameLogic(gameBoard, pieceTracker, playerMoveChoice))
			{	
				endOfGame = true;
				if (pieceTracker == 1) {System.out.println("Player wins!");}
				if (pieceTracker == 2) {System.out.println("AI player wins!");}
				inputMove.close();
			}
			
		}
		

	}

}