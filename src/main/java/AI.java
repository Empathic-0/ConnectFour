/**
 * This class has the AI logic which is derived from the  <br>
 * ConnectFour class game logic that determines consecutive <br>
 * pieces and win conditions. The logic that the AI implements <br>
 * is an adaptation of the game logic and determines moves based <br>
 * on probability and streaks of opponent pieces of at least 2.
 * @author Alexander Mackey
 */
public class AI 
{	
	private static int moveCoords = 0; //Used to store AI move
	private static int counter = 0; //Used to create variations in AI Logic
/**
 * This method retrieves the value of AI's move position.
 * @return returns an integer value representing the column <br>
 * number that the AI selects to place its piece.
 */
	public static int getCoords()
	{
		return moveCoords;
	}
/**
 * This method is used to set the private variable moveCoords
 * @param coords is an integer value that represents the column <br>
 * number that the AI selects to place its piece.
 */
	public static void setCoords(int coords)
	{
		moveCoords = coords;
	}
/**
 * This is an adaptation of the ConnectFour game logic column checker. <br>
 * It looks for at least 2 consecutive opponent pieces and decides where <br>
 * it will make its move accordingly. 
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @return returns a boolean value, returns true if at least 2 values are detected <br>
 * and are vertically consecutive, returns false otherwise.
 */
	public static boolean ai_NS(int[][] gameGrid, int pieceTracker, int colNum)
	{
		int streakBonus = 0; //Initial position checked will have a piece
		for (int newtracker = ConnectFour.getRows()-1; newtracker > 0; newtracker--)
		{
			if (gameGrid[newtracker][colNum] == pieceTracker) //Increments the streak if consecutive pieces are found
			{	
				streakBonus++;
				if (streakBonus == 3 || streakBonus == 2)
				{	
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					setCoords(colNum);
					return true;
				}
			}
			else
				streakBonus = 0;		
		}
		return false; //if steak is not found
	}
/**
 * This is an adaptation of the ConnectFour game logic row checker. <br>
 * It looks for at least 1 opponent piece and decides where <br>
 * it will make its move accordingly. If 2 consecutive pieces are found <br>
 * the AI will place a pieces directly next to a piece, if only one is found <br>
 * the AI will place a piece 2 spaces away from last opponent piece checked.
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @return returns a boolean value which indicates whether the AI has found a valid <br>
 * move.
 */
	public static boolean ai_EW(int[][] gameGrid, int pieceTracker, int colNum)
	{
		counter++; //Used to make variations in the AI move logic
		int rowNum = 0; //Used to store the row when finding the row we need to check for win condition
		//The loop below is used to find the row of last piece dropped to find exact position
		for (int posTracker = 0; posTracker < ConnectFour.getRows(); posTracker++)
		{
			
			if (gameGrid[posTracker][colNum] != 0)
			{
				rowNum= posTracker;
				break;
			}
		}
		int streakBonus = 0; 
		for (int tracker = ConnectFour.getCols()-1; tracker > 0; tracker--) //CHECKS THE LEFT
		{
			if (counter %2 == 0) //Used to make the switch in the AI move logic
				break;
			if (gameGrid[rowNum][tracker] == pieceTracker) //Increments the streak if consecutive pieces are found
			{
				streakBonus++;
				if (streakBonus >= 2)
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					setCoords(tracker-1);
					return true;
				}
				else
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					if (tracker + 2 < ConnectFour.getCols()) //###
					{	
						setCoords(tracker+1); //###
						return true;
					}
				}
			}
			else 
				streakBonus = 0;
		}
		for (int tracker = 0; tracker < ConnectFour.getCols(); tracker++) //CHECKS THE RIGHT
		{
			
			if (gameGrid[rowNum][tracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
		
			{
				streakBonus++;
				if (streakBonus >= 2)
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					setCoords(tracker+1);
					return true;
				}
				else
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					if (tracker - 2 > 0) //###
					{	
						setCoords(tracker-1);//###
						return true;
					}
				}
			}
			else 
				streakBonus = 0;
		}
		return false; //if steak is not found
	}
/**
 * This is an adaptation of the ConnectFour game logic minor diagonal checker. <br>
 * It looks for at least 1 opponent piece and decides where <br>
 * it will make its move accordingly. If 2 consecutive pieces are found <br>
 * the AI will place a pieces2 spaces away, if only one is found <br>
 * the AI will place a piece directly next to a piece.
 * @param gameGrid is a two dimensional integer array storing 0's, 1's, or 2's <br>
 * which represent the empty space, player one piece, or AI piece (respectively).
 * @param pieceTracker is an integer used to keep track of whose turn it is.
 * @param colNum is an integer value which is used to track the column number <br>
 * for which a piece will be placed into.
 * @return returns a boolean value which indicates whether the AI has found a valid <br>
 * move.
 */	
	public static boolean ai_NESW(int[][] gameGrid, int pieceTracker, int colNum) //Minor
	{
		int streakBonus = 0;
		counter++; //Used to make variations in the AI move logic
		int rowNum = 0; //Used to store the row when finding the row we need to check for win condition
		//The loop below is used to find the row of last piece dropped to find exact position
		for (int posTracker = 0; posTracker < ConnectFour.getRows(); posTracker++)
		{
			
			if (gameGrid[posTracker][colNum] != 0)
			{
				rowNum = posTracker;
				break;
			}
		}
		for (int rowTracker = rowNum + 1, colTracker = colNum - 1; colTracker >= 0 && rowTracker < ConnectFour.getRows(); rowTracker++ ,colTracker--)
		{	
			if (counter %2 == 0) //Used to make the switch in the AI move logic
				break;
			if (gameGrid[rowTracker][colTracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
			{
				streakBonus++; 
				if (streakBonus >= 2)
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					if (colTracker - 1 > 0) //###
					{	
						setCoords(colTracker-1); //###
						return true;
					}
				}
				else
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					if (colTracker - 2 > 0) //###
					{	
						setCoords(colTracker-2); //###
						return true;
					}
				}
			}
			else 
				streakBonus = 0;
		}
		for (int colTracker = colNum + 1, rowTracker = rowNum - 1; colTracker < ConnectFour.getCols() && rowTracker >= 0; rowTracker--, colTracker++)
		{
			if (gameGrid[rowTracker][colTracker] == pieceTracker)  //Increments the streak if consecutive pieces are found
			{
				streakBonus++;
				if (streakBonus >= 2)
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					if (colTracker + 1 < ConnectFour.getCols()) //###
					{	
						setCoords(colTracker+1); //###
						return true;
					}
				}
				else
				{
					if (ConnectFour.getFlag()) //Used to prevent same AI move, causing overflow
						break;
					if (colTracker + 2 < ConnectFour.getCols()) //###
					{	
						setCoords(colTracker+2); //###
						return true;
					}
				}
			}
			else 
				streakBonus = 0;
		}
		return false; //if steak is not found
	}

/**
 * This is the main method that must be included for each new class. <br>
 * Within the main method is a script that will execute the statements <br>
 * necessary gather user input and to complete the mathematical operations.
 * @param args you can pass n-number of arguments to the program, and convert as you <br>
 * need due to the fact a string can be manipulated into any primitive type in the <br>
 * Java language.*/	
	public static void main(String[] args) 
	{

	}

}