/* Ryan St. Pierre
 * John Ahn
 * Conor Belfield
 * 
 * This file reads the problem in the .cnf file and extrccts the needed information
 * from it. It gets the number or variables, the number of clauses, and uses the information
 * from the clauses in the file to create a score fitness function to rate the 
 * individuals created by the two algorithms
 * 
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MaxSat {

	//2D arraylist of the clauses in the problem 
	private ArrayList<ArrayList<Integer>> problem = new ArrayList<ArrayList<Integer>>();
	private boolean validProblem;
	private int numLiterals;
	private int numClauses;
	private String fileName;

	//Constructs a MaxSat Object from a file
	public MaxSat(String fileName) {
		this.fileName = fileName;
		validProblem = readProblem();
	}

	//The function takes the file in the maxsat and parses it 
	//for information, determining the number of variables, 
	//the number of clauses and what the clauses are
	//returns a boolean telling if the file was properly parsed
	public boolean readProblem() {

		String problemFileName;
		problemFileName = fileName;
		
		//reads file
		try {
			Scanner fileScan = new Scanner(new File(problemFileName));

			if (!fileScan.hasNext()) {
				System.out.println("Error: file is empty.");
				fileScan.close();
				return false;
			}
			
			//obtains information from the file by looping through the lines
			//using the structure of the .cnf file to locate information
			while (fileScan.hasNextLine()) {
				String nextLine = fileScan.nextLine();
				if (nextLine.startsWith("c")) {
					continue;
				} else if (nextLine.startsWith("p")) {
					String[] parameters = nextLine.split("\\s+");
					numLiterals = Integer.parseInt(parameters[2]);
					numClauses = Integer.parseInt(parameters[3]);
				}

				//records each clause in the conjuction of disjunctions
				else {
					String[] clause = nextLine.split(" ");
					ArrayList<Integer> clauseArgs = new ArrayList<Integer>();
					for (int i = 0; i < clause.length - 1; i++) {
						if (!clause[i].equals("")) {
							int possibleLiteral = Integer.parseInt(clause[i]);
							if (Math.abs(possibleLiteral) <= this.numLiterals) {
								clauseArgs.add(possibleLiteral);
							}
						}
					}
					problem.add(clauseArgs);
				}
			}
			fileScan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: file not found.");
			return false;
		}

		// successfully imported the problem
		return true;

	}

	//function uses the clauses found earlier
	//when reading the file to rate how effective an individual
	//is at satisfying clauses
	//returns the number of clauses satisified by the individual
	public int scoreFitness(Individual individual) {
		
		if (individual.getSolution().size() != this.numLiterals) {
			System.out.println("Error: wrong number of Literals");
			return -1;
		}

		int fitness = 0;
		
		//gets the true/false values from the indivivual
		ArrayList<Integer> solution = individual.getSolution();
		
		// for each clause
		for (int i = 0; i < this.numClauses; i++) {
			ArrayList<Integer> clause = problem.get(i);
			
			// for each literal
			for (int j = 0; j < clause.size(); j++) {

				// Literal i has index of i - 1 in solution
				int literalIndex = Math.abs(clause.get(j)) - 1;

				// Rademacher function for signs
				boolean isTrue = clause.get(j) * ((solution.get(literalIndex)) * 2 - 1) > 0;
				
				// if the literal is true, the whole clause is true
				if (isTrue) {
					fitness++;
					break;
				}
			}
		}
		return fitness;
	}
	
	// returns the clause values given an individual solution to the maxSat
	public ArrayList<Integer> getClauseValues(Individual individual) {
		ArrayList<Integer> clauseValues = 
				new ArrayList<Integer>(
						Collections.nCopies(this.numClauses, 0));
		for(int i = 0; i < this.numClauses; i++) {
			ArrayList<Integer> clause = problem.get(i);
			for (int j = 0; j < clause.size(); j++) {
				int literalIndex = java.lang.Math.abs(clause.get(j)) - 1;
				boolean isTrue = clause.get(j) * ((individual.getSolution().get(literalIndex)) * 2 - 1) > 0;
				if (isTrue) {
					clauseValues.set(i, 1);
					break;
				}
			}
		}
		
		return clauseValues;
	}

	
	// Getters & Setters
	public boolean isValidProblem() {
		return validProblem;
	}

	public void setValidProblem(boolean goodProblem) {
		this.validProblem = goodProblem;
	}

	public ArrayList<ArrayList<Integer>> getProblem() {
		return problem;
	}

	public void setProblem(ArrayList<ArrayList<Integer>> problem) {
		this.problem = problem;
	}

	public int getNumLiterals() {
		return numLiterals;
	}

	public void setNumLiterals(int numLiterals) {
		this.numLiterals = numLiterals;
	}

	public int getNumClauses() {
		return numClauses;
	}

	public void setNumClauses(int numClauses) {
		this.numClauses = numClauses;
	}

}
