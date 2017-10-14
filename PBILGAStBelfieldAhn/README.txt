Ryan St. Pierre
John Ahn
Conor Belfield
2/23/17

ReadMe file

This program implements two methods for solving a Maximum satisfiablilty problem.

The first is a Genetic Algorithm, which simulates evolution through creating
a population of individuals, ranking their fitness, simulating a selection 
process that chooses what genes survive based on this fitness. Reproduction is then
simulated through crossover and mutation, and this process repeats until the 
iteration limit is reached or a perfect answer is found.

The second is Population Based Incremental Learning. This algorithm also simulates
evolution, but in a different manner. This algorithm is good for solving data that
can be arranged in a bit string structure. It creates a array of probabilities that 
an individual will have a trait on a string, each trait being true or false.
Individuals are then created from this string and their fitness is ranked. The
best individual is then used to update the probability vector to be more likely
to give traits similar to that individual. The probability vector is also updated 
away from the worst individual. The probability vector is then mutated with 
a low probability, and the process repeats until a perfect answer is found or
the iteration limit is hit.

This program needs a max sat problem in a .cnf file format in order to function
correctly.





Description of each file's purpose:

Playground.java is the main file of the program and is used to do the initial
function calling, handle input from the user, and print output.

MaxSat.java reads the conjuctive normal form file and parses the information
to determine the number of clauses and variables. This information is needed
to both know what the problem is being solved and to determine how well the
algorthm did in solving the problem.

QuickSort.java implements quicksort that sorts a 2D array by the fitness value
inputted into the array

Population.java implements the Genetic Algorithm

PBIL.java implements Population Based Incremental Learning






Running the program:

The program is coded in java so compile and run the file Playground.java from the
command prompt as java normally does on the operatng system of your choice.

(Usually entails using javac "file" to compile and java "file" to run)

after the file name, eight parameters must be entered to use the program.

If the Genetic algorithm is desired to be run, the parameters are

filename individuals selectionType crossoverType crossProb mutProb generations algorithm

ex:

java Playground C:\Users\Ryan\Desktop\trial\problems/maxsat-random/max2sat/100v/s2v100c1400-5.cnf 100 ts 1c .7 .01 1000 g

filename (string) is the path to the .cnf file that is being passed to the program
that contains the information about the MaxSat problem to solve

individuals (int) is the number of individuals the GA will create in every iteration

selectionType (string) is either "bs", "ts", or "rs"

bs stands for Boltzmann selection where an individual will be selected with a probability 
proportion to oiliers number to the power of their fitness value

ts stands for tournament selection which, in this program, is a method for choosing indviduals
by select two random individuals and taking the better of the two 

rs stands dor ranking selection where an individual is selected with a probability proportional
to their rank according to fitness among the indiviuals in the population

crossoverType (string) stands for the type of crossover to be done, either  "1c" or "uc"

1c stands for one-point crossover where a random point in the two individuals being swapped
is chosen and they swap their values on one side of the point with the other individual to
create an offspring

uc stands for uniform crossover where each trait in the two individuals being crossed is
compared and they are swapped with a certain probablity

crossProb (double) is a value between 0 and 1 giving the probability a crossover occurs

mutProb (double) is a value between 0 and 1 giving the probabiity a mutation occurs

generations (int) is the number of iterations of populations to go through before the program stops

algorithm (string) must be g in order to run the GA. If it is p the PBIL will run


If the Population Based Incremental Learning is desired to be run, the parameters are

filename individuals positiveLearnRate negativeLearnRate mutProb mutAmount iterations algorithm

ex:

java Playground ../problems/maxsat-random/max2sat/100v/s2v100c1400-5.cnf 100 0.1 0.075 .02 .05 1000 p

filename (string) is the path to the .cnf file that is being passed to the program
that contains the information about the MaxSat problem to solve

individuals (int) is the number of individuals the GA will create in every iteration

positiveLearnRate (double) is a value between 0 and 1 that determines how much each iteration
in the PBIL will modify the probability vector towards each best indivdual

negativeLearnRate (double) is a value between 0 and 1 that determines how much each iteration
in the PBIL will modify the probability vector away from each worst indivdual

mutProb (double) is a value between 0 and 1 giving the probabiity a mutation occurs

mutAmount (double) is a value between 0 and 1 controlling the amount that each mutation
modifies the probability vector when a mutation occurs

iterations (int) is the number of iterations the algorithm goes through before the program stops

algorithm (string) must be p in order to run the PBIL. If it is g the GA will run





