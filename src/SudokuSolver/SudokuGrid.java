package SudokuSolver;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;
import info.gridworld.gui.GUIController;
import info.gridworld.gui.GridPanel;
import info.gridworld.world.World;
import javax.swing.JOptionPane;
public class SudokuGrid extends World<Integer>
{
	private int solveCount = 0;
	private static int[][] puzzle = new int[9][9];
	/**
	 * Standard Constructor Creates a 9 by 9 grid and fits the frame to the
	 * grid.
	 */
	public SudokuGrid()
	{
		super(new BoundedGrid<Integer>(9, 9));
		show();
	}
	/**
	 * @param args
	 *            No arguments required to run.
	 */
	public static void main(String[] args)
	{
		new SudokuGrid();
	}
	/**
	 * Clears the puzzle array, setting all values to 0.
	 */
	public void clearArray()
	{
		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++)
				puzzle[r][c] = 0;
	}
	/**
	 * Loops through the grid and places the numbers into the puzzle array Blank
	 * squares are left as 0s.
	 */
	public void fillPuzzleArray()
	{
		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++)
				if (getGrid().get(new Location(r, c)) != null)
					puzzle[r][c] = getGrid().get(new Location(r, c));
	}
	/*
	 * (non-Javadoc)
	 * @see info.gridworld.world.World#keyPressed(java.lang.String,
	 * info.gridworld.grid.Location)
	 */
	public boolean keyPressed(String description,Location loc)
	{
		// System.out.println(description);
		try
		{
			if (Integer.parseInt(description) >= 1 && Integer.parseInt(description) <= 9)
				getGrid().put(loc, Integer.parseInt(description));
		}
		catch (NumberFormatException e)
		{
		}
		if (description.equals("RIGHT"))
			if (loc.getCol() < 8)
				GridPanel.setCurrentLocation(loc.getAdjacentLocation(90));
			else
				GridPanel.setCurrentLocation(new Location(loc.getRow(), 0));
		if (description.equals("LEFT"))
			if (loc.getCol() > 0)
				GridPanel.setCurrentLocation(loc.getAdjacentLocation(-90));
			else
				GridPanel.setCurrentLocation(new Location(loc.getRow(), 8));
		if (description.equals("UP"))
			if (loc.getRow() > 0)
				GridPanel.setCurrentLocation(loc.getAdjacentLocation(0));
			else
				GridPanel.setCurrentLocation(new Location(8, loc.getCol()));
		if (description.equals("DOWN"))
			if (loc.getRow() < 8)
				GridPanel.setCurrentLocation(loc.getAdjacentLocation(180));
			else
				GridPanel.setCurrentLocation(new Location(0, loc.getCol()));
		if (description.equals("NUMPAD1"))
			getGrid().put(loc, 1);
		if (description.equals("NUMPAD2"))
			getGrid().put(loc, 2);
		if (description.equals("NUMPAD3"))
			getGrid().put(loc, 3);
		if (description.equals("NUMPAD4"))
			getGrid().put(loc, 4);
		if (description.equals("NUMPAD5"))
			getGrid().put(loc, 5);
		if (description.equals("NUMPAD6"))
			getGrid().put(loc, 6);
		if (description.equals("NUMPAD7"))
			getGrid().put(loc, 7);
		if (description.equals("NUMPAD8"))
			getGrid().put(loc, 8);
		if (description.equals("NUMPAD9"))
			getGrid().put(loc, 9);
		if (description.equals("0") || description.equals("NUMPAD0") || description.equals("BACK_SPACE"))
			getGrid().remove(loc);
		if (description.equals("alt F4"))
			System.exit(0);
		if (description.equals("BACK_QUOTE"))
			printValidity();
		if (description.equals("ENTER"))
			step();
		if (description.equals("F1"))
			GUIController.showHelp();
		if (description.equals("F2"))
			GUIController.showAbout();
		if (description.equals("ESCAPE"))
		{
			for (Location g : getGrid().getOccupiedLocations())
				getGrid().remove(g);
			clearArray();
		}
		if (description.equals("shift ctrl C"))
			new Command(this);
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * info.gridworld.world.World#locationClicked(info.gridworld.grid.Location)
	 */
	public boolean locationClicked(Location loc)
	{
		return true;
	}
	/**
	 * Tells if a number is able to be placed in the square.
	 * 
	 * @param row
	 *            The row to attempt placement of the number
	 * @param col
	 *            The column to attempt placement of the number
	 * @param num
	 *            The number to attempt placing in the grid
	 * @return true if the number is allowed in the grid, false otherwise
	 */
	public boolean numIsValid(int row,int col,int num)
	{
		for (int r = 0; r < 9; r++)
			if (r != row && num == puzzle[r][col])
				return false;
		for (int c = 0; c < 9; c++)
			if (c != col && num == puzzle[row][c])
				return false;
		int topLeftRow = (row / 3) * 3;
		int topLeftCol = (col / 3) * 3;
		for (int r = 0; r < 3; r++)
			for (int c = 0; c < 3; c++)
				if (topLeftRow + r != row && topLeftCol + c != col && num == puzzle[topLeftRow + r][topLeftCol + c])
					return false;
		return true;
	}
	/**
	 * Loops through the array and places all non-zero numbers into the grid
	 */
	public void printPuzzleArray()
	{
		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++)
				if (puzzle[r][c] != 0)
					getGrid().put(new Location(r, c), puzzle[r][c]);
	}
	/**
	 * Shows a dialog box telling if the grid layout is legal or not.
	 */
	public void printValidity()
	{
		fillPuzzleArray();
		if (puzzleIsValid())
			JOptionPane.showMessageDialog(null, "The current configuration is valid.", "Check", 1);
		else
			JOptionPane.showMessageDialog(null, "The current configuration is invalid.", "Check", 0);
		clearArray();
	}
	/**
	 * @return true if the puzzle is a valid configuration, false if it is not.
	 */
	public boolean puzzleIsValid()
	{
		for (Location loc : getGrid().getOccupiedLocations())
			if (getGrid().get(loc) != 0 && !numIsValid(loc.getRow(), loc.getCol(), getGrid().get(loc)))
				return false;
		return true;
	}
	public int getValueOriginal(int x,int y)
	{
		while (x < 0)
		{
			x += 9;
			y--;
		}
		while (x > 8)
		{
			x -= 9;
			y++;
		}
		return puzzle[x][y];
	}
	public int getValue(int x,int y)
	{
		while (x < 0)
		{
			x += 9;
			y--;
		}
		while (x > 8)
		{
			x -= 9;
			y++;
		}
		return puzzle[x][y];
	}
	/**
	 * Fills the square with the correct value
	 * 
	 * @param x
	 * @param y
	 * @param value
	 */
	private void fill(int x,int y,int value)
	{
		while (x < 0)
		{
			x += 9;
			y--;
		}
		while (x > 8)
		{
			x -= 9;
			y++;
		}
		puzzle[x][y] = value;
	}
	/**
	 * Fills a valid number to the square following the sudoku rules
	 * 
	 * @param x
	 * @return
	 */
	public boolean solve(int x)
	{
		if (x == 81)
			return true;
		if (getValue(x, 0) != 0 && getValue(x, 0) == getValueOriginal(x, 0))
			return solve(x + 1);
		else
			for (int i : getValidNumbers(x, 0))
				if (i != 0)
				{
					fill(x, 0, i);
					if (!solve(x + 1))
						fill(x, 0, 0);
					else
						return true;
				}
		return false;
	}
	/**
	 * Returns an array of valid numbers possible in the current square.
	 * Eventually there will only be one possible value signifying that the
	 * puzzle is at completion
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int[] getValidNumbers(int x,int y)
	{
		while (x < 0)
		{
			x += 9;
			y--;
		}
		while (x > 8)
		{
			x -= 9;
			y++;
		}
		int[] validNumbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		// Check vertical and horizontal
		for (int i = 0; i != 9; i++)
		{
			if (puzzle[i][y] != 0)
				validNumbers[puzzle[i][y] - 1] = 0;
			if (puzzle[x][i] != 0)
				validNumbers[puzzle[x][i] - 1] = 0;
		}
		// Check the "squares"
		int squareX, squareY;
		if (x < 3)
			squareX = 0;
		else if (x < 6)
			squareX = 3;
		else
			squareX = 6;
		if (y < 3)
			squareY = 0;
		else if (y < 6)
			squareY = 3;
		else
			squareY = 6;
		for (int i = 0; i != 3; i++)
			for (int j = 0; j != 3; j++)
				if (puzzle[i + squareX][j + squareY] != 0)
					validNumbers[puzzle[i + squareX][j + squareY] - 1] = 0;
		return validNumbers;
	}
	public boolean isSolved()
	{
		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++)
				if (puzzle[r][c] == 0)
					return false;
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see info.gridworld.world.World#step()
	 */
	public void step()
	{
		fillPuzzleArray();
		if (!puzzleIsValid())
		{
			JOptionPane.showMessageDialog(null, "This is an invalid Sudoku grid", "Invalid Grid", 0);
			clearArray();
			return;
		}
		if (isSolved())
		{
			solveCount++;
			if (solveCount == 7)
			{
				JOptionPane.showMessageDialog(null, "I'm tired of trying to solve your puzzle!", "I Give Up!", 0);
				System.exit(0);
			}
			JOptionPane.showMessageDialog(null, "This puzzle is already solved.", "Already Solved", 1);
			return;
		}
		solveCount = 0;
		solve(0);
		printPuzzleArray();
		clearArray();
	}
}