import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner; 
import java.io.FileWriter; 

public class GA_knap{

    int[] value;        //contains only positive numbers
    int[] weight;       //contains only positive numbers
    int maxAllowedWeight;   // positive only

    public GA_knap(){

        maxAllowedWeight = -1;

    }

    public GA_knap(int n){

        value = new int [n];
        weight = new int [n];
        maxAllowedWeight = -1;

    }

    public void calculateFitness(Population P){

        for(int i=0; i<P.getSize(); i++){

            calculateFitness(P.chromosomes[i]);

        }

    }

    public void calculateFitness(Individual I){

        int[] chosenItemsIndex = I.decode();
        long totalValue, totalWeight;
        totalValue = totalWeight = 0;

        for(int i=0; i < chosenItemsIndex.length; i++){

            int currentIndex = chosenItemsIndex[i] - 1; // -1 due to the "1-indexed" fact.
            totalValue += value[currentIndex];
            totalWeight += weight[currentIndex];

        }
        if(totalWeight > maxAllowedWeight){

            I.setFitness(0);
        }
        else{

            I.setFitness(totalValue);
        }

    }

    public long weightOfIndividual(Individual I){

        long totalWeight = 0;

        int[] chosenItemsIndex = I.decode();
        for(int i=0; i<chosenItemsIndex.length; i++){

            int index = chosenItemsIndex[i] - 1;     
            totalWeight += weight[index];

        }
        return totalWeight;
    }

    public long valueOfIndividual(Individual I){

        long totalWeight = 0;

        int[] chosenItemsIndex = I.decode();
        for(int i=0; i<chosenItemsIndex.length; i++){

            int index = chosenItemsIndex[i] - 1;     
            totalWeight += value[index];

        }
        return totalWeight;
    }

    public void display(){

        System.out.println("Value[] = " + Arrays.toString(value));
        System.out.println("Weight[] = " + Arrays.toString(weight));
        System.out.println("Max weight allowed = " + maxAllowedWeight);
    }

    public static void main(String[] args){

        GA_knap myInstance;
        Scanner obj = new Scanner(System.in);
        int testCase = 0;

        System.out.println("Enter number of objects");
        int n = obj.nextInt();
    
        myInstance = new GA_knap(n);

        try{

            String inputValue = n + "inputValue.txt";
            String inputWeight = n + "inputWeight.txt";
            String inputMaxweight = n + "inputMaxweight.txt";

            File reader1 = new File(inputValue);  
            File reader2 = new File(inputWeight); 
            File reader3 = new File(inputMaxweight); 

            Scanner myReader1 = new Scanner(reader1);
            Scanner myReader2 = new Scanner(reader2);
            Scanner myReader3 = new Scanner(reader3);

            while(myReader1.hasNextInt() && myReader2.hasNextInt() && myReader3.hasNextInt()){

                for(int i = 0; i<n; i++){

                    myInstance.value[i] = myReader1.nextInt();
                    myInstance.weight[i] = myReader2.nextInt();
                }

                ++testCase;

                String outputFile =   "0-items.txt";
                String detailFile =  "0-details.txt";
                String whitespace = "          ";    


                try{

                    FileWriter printout = new FileWriter(outputFile, true);
                    FileWriter myWriter = new FileWriter(detailFile, true);
                    printout.append("\nTest case " + testCase + "\n");
                    printout.append("\nInput description:\n Size of input = " + n + "\n");
                    printout.append("\n cost[] =" + Arrays.toString(myInstance.value));
                    printout.append("\n weight[] =" + Arrays.toString(myInstance.weight));
                    printout.append("\n Max weight allowed =" + myInstance.maxAllowedWeight + "\n");

                    int sizes[] = {100};    //Population size = 100 

                    for(int i=0; i<1; i++){

                        printout.append("\npopulation size=" + sizes[i] + "\n");

                        Population currentPopulation;
                        Population nextPopulation;
                        Individual[] selectedParents;
                        Individual[] children;
                        Individual fittest = null;

                        int currentGeneration = 0;
                        int generationCount[] = {1,2,3,4,5,6,7,8,9,10}; //The numbers must be in ascending order.

                        currentPopulation = new Population(sizes[i]);
                        nextPopulation = new Population(sizes[i]);
                        children = new Individual[sizes[i]];
                        currentPopulation.init(n);
                        myInstance.calculateFitness(currentPopulation);

                        if(true){

                            long startTime = System.currentTimeMillis();            
                            long totalTime = 0;            

                            for(int j = 0; j<10; j++){  // j<10 implies 10 stopping points

                                currentGeneration ++;

                                if(currentGeneration == generationCount[j] ){

                                    if(true){

                                        myWriter.append("\nRound " + currentGeneration + "\n");
                                        myWriter.append("Current Population" + "\n");
                                        currentPopulation.printPopulation(myWriter); 

                                        if(fittest == null){

                                            fittest = currentPopulation.findStrongest();    
                                            myWriter.append("\n" + "Fittest= " + 
                                            fittest.getFitness() + '\n');
                                            
                                        }
                                        else{
        
                                            Individual currentFittest;
                                            currentFittest = currentPopulation.findStrongest();
                                            myWriter.append("\n" + "fittest value from the previous rounds: ");
                                            myWriter.append(fittest.getFitness() + "\n");
                                            myWriter.append("fittest value of the current population= ");
                                            myWriter.append(currentFittest.getFitness() + "\n");
        
                                            if(fittest.getFitness() < currentFittest.getFitness()){
                                                
                                                fittest = currentFittest;
                                                myWriter.append("Thus, the fittest value till now is: " + 
                                                                fittest.getFitness() + "\n");
                                            }
        
                                        }
                                        
                                    }

                                }
                                
                                selectedParents = currentPopulation.selection();

                                int childGenerated = 0;

                                Random number = new Random(); 
                                while(childGenerated < sizes[i]){

                                    int parentIndex1 = number.nextInt(selectedParents.length);
                                    int parentIndex2 = number.nextInt(selectedParents.length);                                      

                                    Individual parent1, parent2, child;
                                    parent1 = selectedParents[parentIndex1];
                                    parent2 = selectedParents[parentIndex2];

                                    if(Population.crossoverThreshold > parent1.crossoverProbability 
                                        && Population.crossoverThreshold > parent1.crossoverProbability )
                                    {
                                    
                                        child = Population.crossover(parent1, parent2);
                                        myInstance.calculateFitness(child);
                    
                                        if(Population.mutation(child)){

                                            myInstance.calculateFitness(child);

                                        }

                                        else{

                                           myWriter.append("The mutation has not happened.\n");

                                        }
                                    }
                                    else{

                                        child = parent1.clone();
                                        child.modifyBit();

                                    }
                                    myInstance.calculateFitness(child);
                                    children[childGenerated++] = child;
                                }

                                if(currentGeneration == generationCount[j]){

                                    long endTime = System.currentTimeMillis();

                                    printout.append("Generation=" + currentGeneration + whitespace);
                                    printout.append(" Current Fittest value=" + currentPopulation.findStrongest().getFitness() 
                                                    + whitespace);
                                    printout.append(" Overall Fittest value till now=" + fittest.getFitness() 
                                                    + whitespace);
                                    printout.append(" Total Time taken = " + (endTime - startTime + totalTime) + "ms\n");

                                    totalTime += endTime - startTime;
                                    startTime = endTime;

                                }
                                else{
                                    --j;   
                                }
                                
                                nextPopulation.init(children);
                                
                                currentPopulation = nextPopulation;
                            }

                            printout.append("\n The overall optimal solution is: Fitness = " + fittest.getFitness());
                            printout.append("\nTotal value of the items in the knapsack = " + 
                            myInstance.valueOfIndividual(fittest));
                            printout.append(" and weight = " + myInstance.weightOfIndividual(fittest) + "\n");
                        }
                    }
                    myWriter.close();
                    printout.close();
                }
                catch(IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
            
            myReader1.close();
            myReader2.close();
            myReader3.close();
            obj.close();
        }
        
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}