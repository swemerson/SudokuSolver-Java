package cspSolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.text.DecimalFormat;

import cspSolver.BTSolver;
import cspSolver.BTSolver.ConsistencyCheck;
import cspSolver.BTSolver.ValueSelectionHeuristic;
import cspSolver.BTSolver.VariableSelectionHeuristic;
import sudoku.SudokuBoardGenerator;
import sudoku.SudokuBoardReader;
import sudoku.SudokuFile;

public class handler {

	public static void main(String[] args) throws IOException, NumberFormatException, InterruptedException 
	{
		// Start program timer in seconds
		long programStartTime = (System.currentTimeMillis() / 1000);		
		
		// Tokens
		boolean genToken = false;
		boolean fcToken = false;
		
		// Detect tokens
		int count = args.length;
		for (int i = 3; i < count; ++i)
		{
			switch ((args[i].toUpperCase()))
			{
			case "GEN":
				genToken = true;
				generateBoard(args[0], args[1]);
				break;
			case "FC":
				fcToken = true;				
			}
		}		
		
		// Run solver if GEN token not received
		if (!genToken)
		{
			SudokuFile sf = SudokuBoardReader.readFile(args[0]);
			BTSolver solver = new BTSolver(sf);
			
			// Set checks and heuristics based off tokens
			if (fcToken)
				solver.setConsistencyChecks(ConsistencyCheck.ForwardChecking);
			else
				solver.setConsistencyChecks(ConsistencyCheck.None);
			solver.setValueSelectionHeuristic(ValueSelectionHeuristic.None);
			solver.setVariableSelectionHeuristic(VariableSelectionHeuristic.None);
			
			// Create thread for solver and time it
			Thread t1 = new Thread(solver);
			t1.start();
			t1.join(Long.valueOf(args[2]) * 1000);
			if(t1.isAlive())
				t1.interrupt();

			sf = solver.getSolution();
			
			// Open output file for recording of results
			File outFile = new File(args[1]);
			FileWriter fileWriter = new FileWriter(outFile);	
			
			// Record results
			DecimalFormat df = new DecimalFormat("#0.00"); 
			String results = "==============================\n" + 
					 		 "TOTAL_START=" + df.format(0) + "\n" +
					 		 "PREPROCESSING_START=" + df.format(0) + "\n" + // Add preprocessor times
					 		 "PREPROCESSING_DONE=" + df.format(0) + "\n" + // Add preprocessor times
					 		 "SEARCH_START=" + df.format(solver.getStartTime() / 1000.0 - programStartTime) + "\n" +
					 		 "SEARCH_DONE=" + df.format(solver.getEndTime() / 1000.0 - programStartTime) + "\n" +
					 		 "SOLUTION_TIME=" + df.format(((0 - 0) + (solver.getEndTime() - solver.getStartTime())) / 1000.0) + "\n" + // Add preprocessor times
					 		 "STATUS=" + (solver.hasSolution() ? "success" : "timeout") + "\n" +
					 		 "SOLUTION=" + (solver.hasSolution() ? sf.getSolutionTuple() : sf.getEmptyTuple()) + "\n" +
					 		 "COUNT_NODES=" + solver.getNumAssignments() + "\n" +
					 		 "COUNT_DEADENDS=" + solver.getNumBacktracks() + "\n" +
					 		 "==============================\n";
			
			// Close out
			fileWriter.write(results);				
			fileWriter.flush();
			fileWriter.close();
		}	
	}
	
	public static void generateBoard(String inFile, String outFile) throws IOException
	{
		Reader reader = new FileReader(inFile);
		BufferedReader br = new BufferedReader(reader);
		String line;
		line = br.readLine();
		String[] lineParts = line.split("\\s+");
		int N = Integer.parseInt(lineParts[1]);
		int p = Integer.parseInt(lineParts[2]);
		int q = Integer.parseInt(lineParts[3]);
		int numAssignments = Integer.parseInt(lineParts[0]);
		
		File outFileStream = new File(outFile);
		FileWriter fileWriter = new FileWriter(outFileStream);			
		
		SudokuFile generatedSF = SudokuBoardGenerator.generateBoard(N, p, q, numAssignments);
		fileWriter.write((generatedSF.toString()));
			
		fileWriter.flush();
		fileWriter.close();
		br.close();
	}
}
