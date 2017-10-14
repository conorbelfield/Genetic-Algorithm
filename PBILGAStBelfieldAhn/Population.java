/*
 * Ryan St. Pierre
 * Conor Belfield
 * John Ahn
 * 
 * This class implements the Genetic Algorithm. It contains three different
 * methods of ranking individuals, which are Boltzman selction, tournament
 * selection and ranked selection. It also crosses over solutions when 
 * creating the next generation in either a one point crossover or
 * a uniform crossover. The individuals can then be mutated with a given
 * probabilty
 * 
 */

import java.util.ArrayList;
import java.util.Random;

public class Population {

	private static int TOURNAMENT_SIZE = 4;

	private ArrayList<Individual> individuals = new ArrayList<Individual>();
	private ArrayList<Individual> breedingPool = new ArrayList<Individual>();
	private Individual best = new Individual();
	private int iterFoundBest = 0;
	private int iterations = 0;
	
	MaxSat maxSat;
	
	// create a random population of size numLiterals
	public Population(int numIndividuals, int numLiterals, MaxSat max) {
		maxSat = max;
		for (int i = 0; i < numIndividuals; i++) {
			individuals.add(new Individual(numLiterals));
		}
	}
	
	//function holds the loop that creates each generation of individuals for the
	//GA and calls the smaller functions to do each of the subtasks in the process
	//to create an optimal solution
	//nothing is returned but a solution is created in the class object
	public void geneticAlgorithm(String sel, String cross, double crossProb, int generations, double mutateProb, int candSolutions, MaxSat maxSat) {	
		this.updateFitness();
		for (int x = 0; x < generations; x++) {
			
			//Select a breeding pool
			this.selection(sel);
			//Crossover to create the new candidate solutions
			this.crossover(cross, crossProb);
			//mutate the soultions
			this.mutate(mutateProb);
			//Update the fitness of the new soultions
			this.updateFitness();
			//check if we have a new best solution
			this.findBest(x);
			
			iterations++;
		}
	}
	
	// score fitness of all individuals
	// and stores the info in the class object
	public void scoreFitness(MaxSat maxSat) {
		for (int i = 0; i < this.individuals.size(); i++) {
			individuals.get(i).setFitness(maxSat.scoreFitness(individuals.get(i)));
		}
	}

	// produce an int[][] where individual's fitness in [0] and index in [1]
	//to be used in sorting by fitness but to still allow finding
	//the individual in an array quickly
	//the 2D array of fitness and indices is returned
	public int[][] rankFitness() {
		int[][] fitnessRankings = new int[individuals.size()][2];
		for (int i = 0; i < this.individuals.size(); i++) {
			fitnessRankings[i][0] = this.individuals.get(i).getFitness();
			fitnessRankings[i][1] = i;
		}
		QuickSort.quickSort(fitnessRankings);
		return fitnessRankings;
	}

	//function performs one of three types of selection on the individuals of
	// the population, in this case rank selection where individuals have
	//a probability of being chosen proportional to their fitness rank in the
	//population
	public void rankSelection() {
		//Fitness rankings is a 2D array of size[individuals.size()][2] where the first
		//entry is the fitness of a solution and the second is the index of that solution
		//in individuals
		int[][] fitnessRankings = this.rankFitness();
		
		//Where we're adding to for our new breedingPool
		ArrayList<Individual> newBreedingPool = new ArrayList<Individual>();
		
		//The sum of all the ranks
		int sumOfRanks = (individuals.size()) * (individuals.size() + 1) / 2;

		//Create n*2 new solutions
		for (int i = 0; i < individuals.size() * 2; i++) {
			
			//Order the fitnesses while keeping track of what index they correspond to
			QuickSort.quickSort(fitnessRankings);

			//Start the mark at the top of the range
			double marker = sumOfRanks;

			Random rand = new Random();
			//random is a random int that falls into the ranges that we've created to give 
			//accurate probabilities to the appropriate rank.
			int random = (int) (rand.nextDouble() * (sumOfRanks + 1) + 1);
			boolean cont = true;
			
			for (int y = individuals.size() - 1; y > -1 && cont; y--) {
				//If the random number plus the rank is greater than the marker, we've found
				//the correct range and add the corresponding individual
				if (random + y + 1 > marker) {
					
					newBreedingPool.add(individuals.get(fitnessRankings[y][1]));
					cont = false;
				} 
				//else, we move the marker back by the current rank
				else {
					marker -= (y + 1);
				}
			}

			//End of method
		}
		//Set the new breeding pool
		breedingPool = newBreedingPool;
	}

	//the function is an implementation of tournament selection where two individuals
	//at random are chosen from the population and the one with the higher fitness
	//is selected for the next generation
	public void tournamentSelection() {
		
		ArrayList<Individual> newBreedingPool = new ArrayList<Individual>();
		
		//Create 2*individuals.size() new soultions
		for (int j = 0; j < individuals.size() * 2; j++) {
			//Choose two random individuals
			int ind1 = (int) (Math.random() * (individuals.size() - 1));
			int ind2 = (int) (Math.random() * (individuals.size() - 1));
			int fit1 = individuals.get(ind1).getFitness();
			int fit2 = individuals.get(ind1).getFitness();
			
			//Add the better one to the new breeding pool
			if (fit1 >= fit2) {
				newBreedingPool.add(individuals.get(ind1));

			} else {
				newBreedingPool.add(individuals.get(ind2));
			}

		}
		breedingPool = newBreedingPool;
	}

	//implements boltzman selection where the individuals are chosen with a
	// probability based upon e to the power their fitness. Chosen individuals
	//then advance to the next generation
	public void boltzmannSelection() {
		ArrayList<Individual> newBreedingPool = new ArrayList<Individual>();
		double sumFit = 0;
		double e = Math.E;
		
		//Sum all the boltzman equation fitnesses
		for (int x = 0; x < individuals.size(); x++) {
			sumFit += Math.pow(e, individuals.get(x).getFitness());
		}
		
		//Setting the probabilites of individuals
		double[] prob = new double[individuals.size()];
		for (int x = 0; x < individuals.size(); x++) {
			
			prob[x] = Math.pow(e, individuals.get(x).getFitness()) / sumFit;
		}
		
		//This is creating the ranges that correspond to the probability of an individual
		//being chosen. All the probabilites sum to one, which is why we can split them into sections
		//from zero to one.
		double mark = 0;
		double[] ranges = new double[individuals.size()];
		for (int x = 0; x < individuals.size(); x++) {
			ranges[x] = mark;
			mark += prob[x];
		}
		
		//We now find individuals.size()*2 random numbers between 0 and 1, and whichever
		//Individual owns the range that the random number falls in is selected. This is
		//very similar to rank selection.
		for (int x = 0; x < individuals.size()*2; x++) {
			double location = Math.random();
			int y;
			for (y = individuals.size() - 1;  location <  ranges[y]; y--) {
			}

			newBreedingPool.add(individuals.get(y));
		}
		breedingPool = newBreedingPool;
	}

	//A method that determines which selection we will use
	public void selection(String selectionMethod) {
		if (selectionMethod.equals("rs")) {
			this.rankSelection();
		}
		if (selectionMethod.equals("ts")) {
			this.tournamentSelection();
		}
		if (selectionMethod.equals("bs")) {
			this.boltzmannSelection();
		}
	}

	//This sets the fitnesses of all individuals
	public void updateFitness() {
		for (int i = 0; i < individuals.size(); i++) {
			individuals.get(i).setFitness(maxSat.scoreFitness(individuals.get(i)));
		}
	}
	
	//This finds the best individual from the current solutions. If it is better than the
	//overall best, we set the overall best to this new solution
	public void findBest(int x) {
		int bestFit = 0;
		int index = 0;
		
		for (int i = 0; i < individuals.size(); i++) {
			if (individuals.get(i).getFitness() > bestFit) {
				bestFit = individuals.get(i).getFitness();
				index = i;
			}
		}
		if (bestFit > this.best.getFitness()) {
			this.best = individuals.get(index);
			this.iterFoundBest = x;
		}
	}
	
	//Returns the iteration that found the best solution
	public int getIterFoundBest() {
		return iterFoundBest;
	}
	
	//Returns number of iterations ran
	public int getIterations(){
		return iterations;
	}
	
	//Returns the best solution
	public Individual getBest() {
		return best;
	}
	
	//Sets the best solution
	public void setBest(Individual ind) {
		best = ind;
	}
	
	//implements 1 point crossover between two individuals
	//where a random point is chosen between the two and the halves of
	//the two swap
	public Individual onePointCrossover(Individual parent1, Individual parent2) {

		ArrayList<Integer> childSolution;

		int crossoverIndex = (int) Math.floor(Math.random() * (parent1.size() - 1) + 0.5);

		// randomly select which parent is parent1
		if (Math.random() < 0.5) {
			Individual copyOfParent1 = new Individual(parent1.getSolution());
			parent1.setSolution(parent2.getSolution());
			parent2.setSolution(copyOfParent1.getSolution());
		}
		
		// create the child solution from the parent splices
		childSolution = new ArrayList<Integer>(parent1.getSolution().subList(0, crossoverIndex));
		childSolution.addAll(parent2.getSolution().subList(crossoverIndex, parent2.size()));

		Individual child = new Individual(childSolution);
		return child;

	}

	//implements uniform crossover between two individuals where the individual
	//corresponding values in the two are swapped with some probability 
	public Individual uniformCrossover(Individual parent1, Individual parent2) {
		ArrayList<Integer> childSolution = new ArrayList<Integer>();

		for (int i = 0; i < parent1.size(); i++) {
			if (Math.random() < 0.5) {
				childSolution.add(parent1.getSolution().get(i));
			}

			else {
				childSolution.add(parent2.getSolution().get(i));
			}
		}

		Individual child = new Individual(childSolution);
		return child;
	}

	// method that applies crossover to entire breeding pool
	// note: because breeding pool is twice the size of individual pool, it must
	// be even
	//It does not return anything but modifies the object
	public void crossover(String crossoverMethod, double crossoverProbability) {

		ArrayList<Individual> newIndividuals = new ArrayList<Individual>();

		//loops through the pairs of individuals
		for (int i = 0; i < (this.breedingPool.size() / 2); i++) {

			// for every consecutive pair of individuals
			Individual parent1 = this.breedingPool.get(2 * i);
			Individual parent2 = this.breedingPool.get(2 * i + 1);
			Individual child = new Individual();
			// if crossover occurs
			if (Math.random() < crossoverProbability) {
				if (crossoverMethod.equals("1c")) {
					child = onePointCrossover(parent1, parent2);
				}
				if (crossoverMethod.equals("uc")) {
					child = uniformCrossover(parent1, parent2);
				}
			}

			// else crossover doesn't occur
			else {

				// if parent2 is more fit (or if parent fitness are equal),
				// then (randomly) transform parent1 into parent2
				if (parent1.getFitness() < parent2.getFitness()
						|| (parent1.getFitness() == parent2.getFitness() && Math.random() < 0.5)) {
					parent1.setSolution(parent2.getSolution());
				}

				child = new Individual(parent1.getSolution());
			}
			// populate the individuals pool with the new child
			newIndividuals.add(child);
		}

		this.individuals = newIndividuals;

	}

	// method that mutates each individual
	public void mutate(double mutationProbability) {
		for (int i = 0; i < this.individuals.size(); i++) {
			individuals.get(i).mutate(mutationProbability);
		}
	}

	//returns the size of the individuals arraylist
	public int size() {
		return this.individuals.size();
	}

	//gets the individuals arraylist
	public ArrayList<Individual> getIndividuals() {
		return individuals;
	}

	//sets the individuals arraylist
	public void setIndividuals(ArrayList<Individual> individuals) {
		this.individuals = individuals;
	}

}
