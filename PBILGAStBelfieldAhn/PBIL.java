/*
 * Ryan St. Pierre
 * Conor Belfield
 * John Ahn
 * 
 * This file implements the PBIL as a class. It creates individuals from a 
 * probability vector and modifies the probability vector based upon the best and worst
 * individuals created on each iteration. The probability vector is mutated each
 * iteration to add randomness. PBIL eventually creates a perfect solution
 * or returns a value based upon the rounded values of the probability vector
 * as a solution to the MaxSat
 * 
 */


import java.util.ArrayList;

public class PBIL {
	
	private MaxSat maxSatInfo;
	private double[] probValues;
	private int iter;
	
	public PBIL(MaxSat maxSat){
		this.maxSatInfo = maxSat;
		this.probValues = new double[maxSat.getNumLiterals()];
		this.iter = 0;
	}
	
	//function solves the PBIL by running the loop that creates individuals
	//that are used to adjust the probability vector and calls the mutate
	//function on the probability vector
	//returns an individual that is the solution to MaxSat, an Individual
	//that contains a list of zeros and ones that represent true and 
	//false values for each variable in the MaxSat problem
    public Individual solvePBIL(int NUM_INDIVIDUALS, double P_L_RATE, 
    		double N_L_RATE, double MUT_PROB, double MUT_SHIFT, int ITERATIONS){

        
        //initial population of probability vector
        for(int i = 0; i < maxSatInfo.getNumLiterals(); i++){
            probValues[i] =  0.5;
        }
        
        int[] fitness = new int[NUM_INDIVIDUALS];
        int maxFitness = 0;
        
        //creates a list to be populated with individuals
        ArrayList<Individual> individuals = new ArrayList<Individual>();
        for (int i = 0; i < NUM_INDIVIDUALS; i++){
        	Individual ind = new Individual();
        	individuals.add(ind);
        }
        
        //loop to go through generations until hit limit or get perfect answer
        while(iter < ITERATIONS && maxFitness != maxSatInfo.getNumClauses()){
           
            //make individuals from the probability vector
            for(int i = 0; i < NUM_INDIVIDUALS; i++){
                ArrayList<Integer> curr = new ArrayList<Integer>();
            	
                //checks the probability vector for each literal to set
                //the value of the individual as True or False
                for(int j = 0; j < maxSatInfo.getNumLiterals(); j++){
                	if(Math.random() < probValues[j]){
                		
                        curr.add(1);
                    }
                    else{
                    	curr.add(0);
                    }
                }
                
                //turns the values into an individual and adds to the list
                Individual add = new Individual(curr);
                individuals.set(i, add);
                
                //calculate fitness of each individual and stores
                //their fitness
                fitness[i] = maxSatInfo.scoreFitness(add);
            }
            
            //find the best individual
            int currMaxValue = -1;
            int bestIndex = -1;          
            for(int i = 0; i < NUM_INDIVIDUALS; i++){
                if(fitness[i] > currMaxValue){
                    currMaxValue = fitness[i];
                    bestIndex = i; 
                    //if all clauses satisfied make it so will leave loop
                    if(fitness[bestIndex] == maxSatInfo.getNumClauses()){
                    	iter++;
                        return individuals.get(bestIndex);                       
                    }
                }
            }
            
            //find the worst individual
            int currMinValue = 10000000;
            int worstIndex = -1;
            for(int i = 0; i < NUM_INDIVIDUALS; i++){
                if(fitness[i] < currMinValue){
                    currMinValue = fitness[i];
                    worstIndex = i; 
                }
            }

            //Update probability towards the best solution
            for(int i = 0; i < maxSatInfo.getNumLiterals(); i++){
                double value = probValues[i] * (1.0 - P_L_RATE) + 
                        individuals.get(bestIndex).getValue(i) * (P_L_RATE);
                probValues[i] = value;
            }

            //Update probability away from worst solution
            for(int i = 0; i < maxSatInfo.getNumLiterals(); i++){
                if(individuals.get(bestIndex).getValue(i) != individuals.get(worstIndex).getValue(i)){
                    double value = probValues[i] * (1.0 - N_L_RATE) + 
                    		individuals.get(bestIndex).getValue(i) * (N_L_RATE);
                    probValues[i] = value;
                }
            }
            
            //Mutates the probability vector
            pbilMutate(maxSatInfo, MUT_PROB, MUT_SHIFT);    
            iter++;
        }
        
        ArrayList<Integer> bestIndividual = new ArrayList<Integer>();

        //Creates the solution to return by looking at the 
        //probability value and rounding the values to zeros
        //or ones
        for(int i = 0; i < maxSatInfo.getNumLiterals(); i++){
            if(probValues[i] >= 0.5){
                bestIndividual.add(1);
            }
            else{
                bestIndividual.add(0);
            }
        }
        
        Individual theIndividual = new Individual(bestIndividual);
        
        return theIndividual;
    }
    
    //mutates the probability vector and changes it in the object
    // but returns no values
    public void pbilMutate( MaxSat maxSatInfo, double MUT_PROB, double MUT_SHIFT){
       	int mutDir;
        for (int i = 0; i < maxSatInfo.getNumLiterals(); i++){
            
        	//if the probability hit, changes the value in the vector
        	//in a random direction by a small amount
        	if(Math.random() < MUT_PROB){
                if(Math.random() > 0.5){
                    mutDir = 1;
                }
                else{
                    mutDir = 0;
                }
                this.probValues[i] = this.probValues[i] * (1.0 - MUT_SHIFT)
                        + mutDir * MUT_SHIFT;
            }
        }
    }
    
    //returns the number of iterations
	public int getNumIterations() {
		return iter;
	}


}
