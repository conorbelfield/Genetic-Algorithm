/* Ryan St. Pierre
 * Conor Belfield
 * John Ahn
 * Individual.java
 * 
 * This file creates an Individual class which is used to store information
 * about an individual created by either the PBIL or the Genetic Algorithm
 * The primary concern is the fitness of the individual
 * 
 * 
 */



import java.util.ArrayList;
import java.util.Collections;

public class Individual {

	// solution is an ArrayList of 0s & 1s
	private ArrayList<Integer> solution = new ArrayList<Integer>();
	
	private int fitness;

	//initializes an empty individual
	public Individual() {
		this.solution = new ArrayList<Integer>();
		this.fitness = 0;
	}

	//Constructor creates empty/random Individual of size numLiterals
	public Individual(int numLiterals) {
		this();
		for (int i = 0; i < numLiterals; i++) {
			// the "+0.5" transforms Math.floor() into a rounding function
			this.solution.add((int) Math.floor(Math.random() + 0.5));
		}
	}

	//Constructor creates individual from a given solution
	public Individual(ArrayList<Integer> solution) {
		this();
		this.solution = solution;
	}

	// mutation method for a single individual
	//that randomly changes the values of the
	//individual it is called on
	public void mutate(double mutationProbability) {
		for (int i = 0; i < this.size(); i++) {
			if (Math.random() < mutationProbability) {
				// "^" is the XOR function
				this.solution.set(i, this.solution.get(i) ^ 1);
			}
		}
	}

	//returns the size of the individual
	public int size() {
		return this.solution.size();
	}

	//Creates and returns a string of the Individual's values
	public String toString() {
		String solution = "";
		for (int i = 0; i < this.size(); i++) {
			solution = solution + this.solution.get(i);
		}
		return solution;
	}

	// Getters & Setters

	//returns an individual's solution
	public ArrayList<Integer> getSolution() {
		return solution;
	}

	//returns an individual's fitness
	public int getFitness() {
		return fitness;
	}
	
	//returns a single value from the solution
	//the individual holds
	public int getValue(int i) {
		return this.solution.get(i);
	}

	//sets the fitness of an individual
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	//sets the solution inside the individual
	public void setSolution(ArrayList<Integer> solution) {
		this.solution = solution;
	}

}
