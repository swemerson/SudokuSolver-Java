package sudoku;


public class SudokuFile {

	/**
	 * p, q, N represent the dimensions of a game board.
	 * Assuming a game board has values p = 3, q = 4, N = 12,
	 * an empty board will look like this.
	 *
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * ---------------------------------------
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * ---------------------------------------
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * ---------------------------------------
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 * [] [] [] [] | [] [] [] [] | [] [] [] []
	 *
	 */
	private int p;//number of rows in a block && number of blocks per row
	private int q;//number of columns in a block && number of blocks per column
	private int N;//number of cells in a block && edge length of a NxN puzzle


	private int[][] board = null;

	protected SudokuFile(){}

	public SudokuFile(int N, int p, int q, int[][] board) // need to fix this
	{
		if(N != p * q || N < 1)
		{
			System.out.println("Board parameters invalid. Creating a 9x9 sudoku file instead.");
			setP(3);
			setQ(3);
			setN(9);
			setBoard(new int[9][9]);
		}
		else
		{
			setP(p);
			setQ(q);
			setN(N);
			setBoard(board==null ? new int[9][9] : board);
		}
	}

	public SudokuFile(int N, int p, int q)
	{
		this(N,p,q,null);
	}

	public int getP() {
		return p;
	}

	protected void setP(int p) {
		this.p = p;
	}

	public int getQ() {
		return q;
	}

	protected void setQ(int q) {
		this.q = q;
	}

	public int getN() {
		return N;
	}

	protected void setN(int n) {
		N = n;
	}

	public int[][] getBoard() {
		return board;
	}

	protected void setBoard(int[][] board) {
		this.board = board;
	}
	
	public String getSolutionTuple()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < N; ++i)
			for (int j = 0; j < N; ++j)
			{
				sb.append("\'");
				sb.append(Odometer.intToOdometer(board[j][i])); // Need to find why these are reversed!
				sb.append("\'");
				sb.append(", ");
			}
		sb.setLength(sb.length() - 2);
		sb.append(")");
		return sb.toString();
	}
	
	public String getEmptyTuple()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < N * N - 1; ++i)
			sb.append("0,");
		sb.append("0)");
		return sb.toString();
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(N);
		sb.append(" ");
		sb.append(p);
		sb.append(" ");
		sb.append(q);
		sb.append("\n");
		for(int i = 0; i < N; i ++)
		{
			for(int j = 0; j < N; j++)
				sb.append(Odometer.intToOdometer(board[i][j]) + " ");
			sb.append("\n");
		}
		return sb.toString();
	}
}
