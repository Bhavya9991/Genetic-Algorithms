import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner; 
import java.io.FileWriter; 

public class GA_MCM {

    int[] P;
    int n=0;
    int index = 0;
    
    public void calculateFitness(Population currenPopulation){

        int size = currenPopulation.sizeOfPopulation;

        for(int i=0; i<size; i++){

            calculateFitness(currenPopulation.chromosomes[i]);
        }

    }

    public void calculateFitness(Individual I){

        index = 0;
        I.setFitness(calculate(I.genes, 1, n));

    }

    public BigInteger calculate(int[] C, int low, int high){

        if(low == high){

            return BigInteger.ZERO;

        }

        else{

            int k = C[index];
            ++index;
            BigInteger value1 = BigInteger.valueOf(P[low-1] * P[k] * P[high]);

            BigInteger value2 = calculate(C, low, k);

            BigInteger value3 = calculate(C, k+1, high);

            return (value1.add(value2).add(value3));

        }
    }
    
    public static void main(String[] args){

        GA_MCM myInstance = new GA_MCM();
        int[] dimensions;
        int testCase = 0;
        Individual best = new Individual(1);

        try{

            System.out.println("Enter size of matrix chain");
            Scanner myReader1= new Scanner(System.in);
            myInstance.n = myReader1.nextInt();

            String fileName = "mcm-10-";

            if(myInstance.n == 99999){
                
            fileName = "mcm-2-100000.txt";

            }

            else{

                fileName = fileName + (myInstance.n+1) + ".txt";

            }

            File reader1 = new File("xx.txt");  

            Scanner myReader2 = new Scanner(reader1);

            while(myReader2.hasNextInt()){

                dimensions = new int[myInstance.n + 1];
                ++testCase;

                for(int i=0; i<=myInstance.n; i++){

                    dimensions[i] = myReader2.nextInt();

                }

                myInstance.P = dimensions;

                String outputFile = (myInstance.n + 1) + "matrices.txt"; 

                try{
                    FileWriter printChain = new FileWriter("par_" + outputFile, true);

                    FileWriter myWriter = new FileWriter(outputFile, true);

                    String whitespace = "          ";    

                    myWriter.append("\nTest case " + testCase + "\n");

                    printChain.append("\nTest case " + testCase + "\n");

                    int sizes[] = {5,12,20,24,32,40,50,60,64,100};

                    for(int i = 0; i<1; i++){
                        myWriter.append("\npopulation size=" + sizes[i] + "\n");
                        printChain.append("\npopulation size=" + sizes[i] + "\n");

                        Population currentPopulation;
                        Population nextPopulation;
                        Population matingPool;
                        int generationCount[] = {1, 2, 3, 4, 5, 6, 7, 8};
                        int currentGeneration = 0;
                        double crossLimit = 0.84;

                        currentPopulation = new Population(sizes[i]);
                        nextPopulation = new Population(sizes[i]);
                        matingPool = new Population(sizes[i]);

                        currentPopulation.init(myInstance.n - 1);
                        long startTime = System.currentTimeMillis();                        

                        for(int j = 0; j < 8 ; j++){

                            System.out.println(currentGeneration);
                            
                            myInstance.calculateFitness(currentPopulation);

                            printChain.append("\n\nGeneration " + currentGeneration + "\n");

                            currentPopulation.printPhenotypes(printChain);

                            matingPool.selection(currentPopulation);
                            best = currentPopulation.fittest();

                            printChain.append("\n The chromosomes selected are\n");

                            matingPool.printPhenotypes(printChain);

                            for(int k = 0; k < nextPopulation.sizeOfPopulation; k++){

                                Random obj = new Random();
                                int bound = matingPool.sizeOfPopulation;

                                int a = obj.nextInt(bound);
                                int b = obj.nextInt(bound);

                                Individual parent1 = matingPool.chromosomes[a];
                                Individual parent2 = matingPool.chromosomes[b];

                                if(parent1.crossoverProbability < crossLimit && 
                                        parent2.crossoverProbability < crossLimit){

                                    printChain.append("\n The child of");
                                    parent1.printParenthesization(1, parent1.length + 1, 0, printChain);
                                    printChain.append("   and   ");
                                    parent2.printParenthesization(1, parent2.length + 1, 0, printChain);
                                    printChain.append("is: \n");

                                    nextPopulation.chromosomes[k] = Population.crossover(parent1, parent2, printChain);

                                }
                                else{

                                    --k;
                                    crossLimit += 0.00001;

                                }

                            }

                            printChain.append("\nFittest:" + whitespace + best.getFitness() + whitespace);
                            best.printParenthesization(1, myInstance.n, 0, printChain);

                            if(currentGeneration == generationCount[j]){

                                long endTime = System.currentTimeMillis();

                                myWriter.append("Generation=" + currentGeneration + whitespace);
                                myWriter.append("Best=" + best.getFitness() + whitespace);
                                myWriter.append("Time taken = " + (endTime - startTime) + "\n");

                                startTime = endTime;

                            }

                            else{

                                --j;
                                
                            }

                            currentPopulation = nextPopulation;
                            currentGeneration++;

                        }

                    }
                    
                    printChain.close();
                    myWriter.close();
                }
                catch(IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
            
            myReader1.close();
            myReader2.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
