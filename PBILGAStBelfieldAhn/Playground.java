/* Ryan St. Pierre
 * Conor Belfield
 * John Ahn
 * 
 * The main file of the function, it handles the command line arguments
 * and calls the appropriate algorithm to solve the problem with the
 * given settings, and then returns an output describing the results
 * 
 */


import java.util.ArrayList;
import javax.swing.plaf.synth.SynthSeparatorUI;


public class Playground {

	public static void main(String[] args) {
			
			//error handling to ensure that each value inputed
			//as a parameter is as it should be, and returns a
			//warning if it isnt
		
			
			if (args.length != 8){
				System.out.println("Please enter 8 arguments");
		        System.exit(1);
			}
			
			String arg1 = args[0];
			String arg8 = args[7];
			if(!arg8.equals("g") && !arg8.equals("p")){
				System.out.println("Please enter p or g in the 8th spot");
		        System.exit(1);
			}
			
			int arg2 = -1;
			
		    try {
		        arg2 = Integer.parseInt(args[1]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }
			
		    if( arg2 <= 1){
		    	System.out.println("The number of individuals must be greater or equal "
		    			+ "to one");
		        System.exit(1);
		    }
		    
			int arg7 = -1;
			
		    try {
		        arg7 = Integer.parseInt(args[6]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[6] + " must be an integer.");
		        System.exit(1);
		    }
		    
			
		    if( arg7 <= 1){
		    	System.out.println("The number of iterations must be greater or equal "
		    			+ "to one");
		        System.exit(1);
		    }
		    
		    //reads and creates MaxSat problem from file
			MaxSat maxSatInfo = new MaxSat(arg1);
		    
			//if statement determing if the GA or PBIL runs
			if(args[7].equals("g")){
				
				String arg3 = args[2];
				String arg4 = args[3];
				double arg5 = -0.1;
				double arg6 = -0.1;
				
				//individual error handling for the GA
			    try {
			        arg5 = Double.parseDouble(args[4]);
			    } catch (NumberFormatException e) {
			        System.err.println("Argument" + args[4] + " must be a double between 0 and 1.");
			        System.exit(1);
			    }
				
			    try {
			        arg6 = Double.parseDouble(args[5]);
			    } catch (NumberFormatException e) {
			        System.err.println("Argument" + args[5] + " must be a double between 0 and 1.");
			        System.exit(1);
			    }
			    
			    if( !arg3.equals("rs") && !arg3.equals("ts") && !arg3.equals("bs")){
			    	System.out.println("The selection method must be rs, ts or bs");
			        System.exit(1);
			    }
			    
			    if( !arg4.equals("1c") && !arg4.equals("uc")){
			    	System.out.println("The crossover method must be 1c or uc");
			        System.exit(1);
			    }
			   
			    
			    if( arg5 > 1.0 || arg5 < 0.0){
			    	System.out.println("The crossover proability must be between zero and"
			    			+ " one");
			        System.exit(1);
			    }
			    
			    if( arg6 > 1.0 || arg6 < 0.0){
			    	System.out.println("The mutation proability must be between zero and"
			    			+ " one");
			        System.exit(1);
			    }
			    
			    
				if(!maxSatInfo.isValidProblem()) {
					System.out.println("MaxSat problem invalid");
				}
				
				//timing the code
				double startTime = System.currentTimeMillis();
				
				//creating the GA object and solving the MaxSat 
				Population pop = new Population(arg2, maxSatInfo.getNumLiterals(), maxSatInfo);
				pop.geneticAlgorithm(arg3, arg4, arg5, arg7, arg6, arg2, maxSatInfo);
				
				double endTime = System.currentTimeMillis();

				double duration = (endTime - startTime)/ 1000.0; 
				
				//gets the answer from the GA on=bject
				Individual answer = pop.getBest();
				
				//calculates the number of correct clauses created by the solution
				int score = pop.getBest().getFitness();
				
				//calculates the number of incorrect clauses created by the solution
				int incorrect = maxSatInfo.getNumClauses() - score;
				
				//calculates the percent of clauses correct
				double percent = (double)score / (double)maxSatInfo.getNumClauses(); 
				
				//prints required information
				
				System.out.print("Solution: ");

				System.out.println();
				System.out.println();
				
				System.out.println("Problem file: " + arg1);
				System.out.println();
				System.out.println("Number of variables in the problem: " + maxSatInfo.getNumLiterals());
				System.out.println();
				System.out.println("Number of clauses in the problem: " + maxSatInfo.getNumClauses());
				System.out.println();
				System.out.println("Number of correct clauses: " + score);
				System.out.println();
				System.out.println("Number of incorrect clauses: " + incorrect);
				System.out.println();
				System.out.println("Percentage of correct clauses: " + Math.floor((percent * 100) * 100) / 100 + "%");	
				System.out.println();		
				System.out.println("Number of iterations: " + pop.getIterations());
				System.out.println();
				System.out.println("Iteration when best answer found: " + pop.getIterFoundBest());
				System.out.println();
				System.out.println("Runtime: " + Math.floor(duration * 1000) / 1000 + " seconds " );
				System.out.println();
				System.out.println("Variable assignments: ");
				
				//loops though solution to print variable assignments
				for(int i = 1; i <= maxSatInfo.getNumLiterals(); i ++){
					if(answer.getSolution().get(i-1) == 0){
						System.out.print("-" + i + " ");
					}
					else{
						System.out.print(i + " ");
					}
					
					if(i % 10 == 0){
						System.out.println();
					}
				}
			}
			
			//running the PBIL code
			else{
				double arg3 = -0.1;
				double arg4 = -0.1;
				double arg5 = -0.1;
				double arg6 = -0.1;
				
				//error handling for PBIL
			    try {
			        arg3 = Double.parseDouble(args[2]);
			    } catch (NumberFormatException e) {
			        System.err.println("Argument" + args[2] + " must be a double between 0 and 1.");
			        System.exit(1);
			    }
				
			    try {
			        arg4 = Double.parseDouble(args[3]);
			    } catch (NumberFormatException e) {
			        System.err.println("Argument" + args[3] + " must be a double between 0 and 1.");
			        System.exit(1);
			    }
				
			    try {
			        arg5 = Double.parseDouble(args[4]);
			    } catch (NumberFormatException e) {
			        System.err.println("Argument" + args[4] + " must be a double between 0 and 1.");
			        System.exit(1);
			    }
				
			    try {
			        arg6 = Double.parseDouble(args[5]);
			    } catch (NumberFormatException e) {
			        System.err.println("Argument" + args[5] + " must be a double between 0 and 1.");
			        System.exit(1);
			    }
			    
			    if( arg3 > 1.0 || arg3 < 0.0){
			    	System.out.println("The positive learning rate must be between zero and"
			    			+ " one");
			        System.exit(1);
			    }
			    
			    if( arg4 > 1.0 || arg4 < 0.0){
			    	System.out.println("The negative learning rate must be between zero and"
			    			+ " one");
			        System.exit(1);
			    }
			    
			    if( arg5 > 1.0 || arg5 < 0.0){
			    	System.out.println("The mutation proability must be between zero and"
			    			+ " one");
			        System.exit(1);
			    }

			    if( arg6 > 1.0 || arg6 < 0.0){
			    	System.out.println("The mutation shift must be between zero and"
			    			+ " one");
			        System.exit(1);
			    }
				
				if(!maxSatInfo.isValidProblem()) {
					System.out.println("MaxSat problem invalid");
				}
				
				//timing the code
				double startTime = System.currentTimeMillis();
				
				//creating a PBIL object and solving the MaxSat while returning the solution
				PBIL prob = new PBIL(maxSatInfo);
				Individual answer = prob.solvePBIL(arg2, arg3, arg4, arg5, arg6, arg7); 
				
				double endTime = System.currentTimeMillis();

				double duration = (endTime - startTime)/ 1000.0; 
				
				//calculates number of clauses satisfied by the solution
				int score = maxSatInfo.scoreFitness(answer);
				
				//calculates the number of clauses not satisfied by the solution
				int incorrect = maxSatInfo.getNumClauses() - score;
				
				//calculates the percent of correct clauses
				double percent = (double)score / (double)maxSatInfo.getNumClauses(); 
				System.out.print("Solution: ");

				System.out.println();
				System.out.println();
				
				System.out.println("Problem file: " + arg1);
				System.out.println();
				System.out.println("Number of variables in the problem: " + maxSatInfo.getNumLiterals());
				System.out.println();
				System.out.println("Number of clauses in the problem: " + maxSatInfo.getNumClauses());
				System.out.println();
				System.out.println("Number of correct clauses: " + score);
				System.out.println();
				System.out.println("Number of incorrect clauses: " + incorrect);
				System.out.println();
				System.out.println("Percentage of correct clauses: " + Math.floor((percent * 100) * 100) / 100 + "%");	
				System.out.println();		
				System.out.println("Number of iterations: " + prob.getNumIterations());
				System.out.println();
				System.out.println("Runtime: " + Math.floor(duration * 1000) / 1000 + " seconds " );
				System.out.println();
				System.out.println("Variable assignments: ");
				
				//loops through the solution to print out variable assignments
				for(int i = 1; i <= maxSatInfo.getNumLiterals(); i ++){
					if(answer.getSolution().get(i-1) == 0){
						System.out.print("-" + i + " ");
					}
					else{
						System.out.print(i + " ");
					}
					
					if(i % 10 == 0){
						System.out.println();
					}
				}
				
			}
		}	
}
