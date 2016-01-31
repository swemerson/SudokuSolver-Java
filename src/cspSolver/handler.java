package cspSolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.IOException;
import java.io.Reader;

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
		int count = args.length;
		
		boolean genToken = false;
		boolean btToken = false;
		
		for (int i = 3; i < count; ++i)
		{
			switch ((args[i].toUpperCase()))
			{
			case "GEN":
				genToken = true;
				break;
			case "BT":
				btToken = true;
			}
		}
		
		if (genToken)
		{
			Reader reader = new FileReader(args[0]);
			BufferedReader br = new BufferedReader(reader);
			String line;
			line = br.readLine();
			String[] lineParts = line.split("\\s+");
			int N = Integer.parseInt(lineParts[1]);
			int p = Integer.parseInt(lineParts[2]);
			int q = Integer.parseInt(lineParts[3]);
			int numAssignments = Integer.parseInt(lineParts[0]);
			
			File outFile = new File(args[1]);
			FileWriter fileWriter = new FileWriter(outFile);			
			
			SudokuFile generatedSF = SudokuBoardGenerator.generateBoard(N, p, q, numAssignments);
			fileWriter.write((generatedSF.toString()));
				
		
			fileWriter.flush();
			fileWriter.close();
			br.close();
		}			
		
		if (btToken || !genToken)
		{
			SudokuFile sf = SudokuBoardReader.readFile(args[0]);
			BTSolver solver = new BTSolver(sf);
			
			solver.setConsistencyChecks(ConsistencyCheck.None);
			solver.setValueSelectionHeuristic(ValueSelectionHeuristic.None);
			solver.setVariableSelectionHeuristic(VariableSelectionHeuristic.None);
			
			Thread t1 = new Thread(solver);
			t1.start();
			t1.join(Long.valueOf(args[2]) * 1000);
			if(t1.isAlive())
				t1.interrupt();

			sf = solver.getSolution();
			
			File outFile = new File(args[1]);
			FileWriter fileWriter = new FileWriter(outFile);			
			
			String results = "TOTAL_START=" + solver.getStartTime() + "\n" +
					 "PREPROCESSING_START=" + 0 + "\n" +
					 "PREPROCESSING_DONE=" + 0 + "\n" +
					 "SEARCH_START=" + solver.getStartTime() + "\n" +
					 "SEARCH_DONE=" + solver.getEndTime() + "\n" +
					 "SOLUTION_TIME=" + ((0 - 0) + (solver.getEndTime() - solver.getStartTime())) + "\n" + // Need to add in preprocessor times
					 "STATUS=" + (solver.hasSolution() ? "success" : "timeout") + "\n" +
					 "SOLUTION=" + (solver.hasSolution() ? sf.getSolutionTuple() : sf.getEmptyTuple()) + "\n" +
					 "COUNT_NODES=" + solver.getNumAssignments() + "\n" +
					 "COUNT_DEADENDS=" + solver.getNumBacktracks() + "\n";		
			
			fileWriter.write(results);	
			
			fileWriter.flush();
			fileWriter.close();
		}	
	}
}
