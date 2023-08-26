import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.IOException;
import java.util.Random;
import java.util.Scanner; 
import java.io.FileWriter; 

public class GA_LCS {
    
    String X,Y;

    public boolean hasACommonSubsequence(String str){

        if(str.length() == 0 || (is_Subsequence(X,str) & is_Subsequence(Y,str))){

            return true;

        }

        else 
        
           return false;

    }

    public boolean is_Subsequence(String X, String str){
        int start = 0;
        int size = str.length();
        int limit = X.length();
        int i=0;

        for(; i<size && start < limit; i++){

            int tmp = X.indexOf(str.charAt(i), start);

            if(tmp == -1){

                break;

            }
            else{

                start = tmp+1;

            }

        }

        if(i==size){

            return true;

        }

        else{

            return false;

        }

    }
    public static void main(String[] args){

        GA_LCS MyInstance = new GA_LCS();
        Random obj = new Random();

        try{

            File reader1 = new File("xx.txt");  
            File reader2 = new File("yy.txt"); 
            Scanner myReader1= new Scanner(reader1);
            Scanner myReader2 = new Scanner(reader2);

            int testCase = 0;

            while(myReader1.hasNextLine() && myReader2.hasNextLine()){

                testCase++;

                MyInstance.X = myReader1.nextLine();
                //MyInstance.X = MyInstance.X.replaceAll("\\s", "");

                MyInstance.Y = myReader2.nextLine();
                //MyInstance.Y = MyInstance.X.replaceAll("\\s", "");

                int upperLimit = Math.min(MyInstance.X.length(),MyInstance.Y.length());

                int sizes[] = {4,12,20,24,32,40,50,60,64,100};
                int result = -11;

                String outputFile = "10.txt";
                String recordFile = "example.txt";

                try{

                FileWriter myWriter = new FileWriter(outputFile,true);
                FileWriter printout = new FileWriter(recordFile,true);
                printout.append("\nTest case:" + testCase + "\n");
                myWriter.append("\nTest case:" + testCase + "\n");

                    for(int i = 0; i<1; i++){

                        printout.append("\nPopulation size = " + sizes[i] + "\n");
                        Population currentPopulation;
                        Population nextPopulation;
                        Population matingPool;
                        int generationCount[] = {0,1,2,3,4,5,6,7,8};
                        int currentGen = 0;

                        currentPopulation = new Population(sizes[i], upperLimit);
                        nextPopulation = new Population(sizes[i], upperLimit);
                        matingPool = new Population(sizes[i], upperLimit);

                        currentPopulation.init();
                        long start = System.currentTimeMillis();

                        for(int gen = 0; gen < 9; gen++){

                            printout.append("\nGeneration " + gen + "\n");
                            for(int j=0; j<sizes[i]; j++){

                                try{
                                    Individual tmpp = currentPopulation.chromosomes[j];
                                    if(MyInstance.hasACommonSubsequence(tmpp.toString())){
            
                                        currentPopulation.chromosomes[j].setValidity(true);
                                    }
                                    else{
            
                                            currentPopulation.chromosomes[j].setValidity(false);
                                    }
        
                                }
                                catch(NullPointerException e){

                                }
        
                            }

                            currentPopulation.calculateFitness();
                            currentPopulation.printPhenotypes(printout);

                            int bestValue = currentPopulation.fittest().getFitness();
                            Individual currentFit = currentPopulation.fittest();
                            result = bestValue;

                            int numberOfElites = currentPopulation.countOfEquallyFit(bestValue); 
                            Individual [] elites;

                            int smaller = Math.min(sizes[i] / 2, numberOfElites);
                            
                            elites = currentPopulation.allFittest(smaller);

                            for(int count = 0; count < smaller; count++){

                                nextPopulation.chromosomes[count] = elites[count];

                            }
                        
                            printout.append("\n The selected chromosomes are\n");
                            matingPool.selection(currentPopulation);
                            printout.append("\t\t");
                            matingPool.printPhenotypes(printout);

                            int todo = smaller;

                            while(todo < sizes[i]){    

                                for(int j = 0; j<sizes[i]; j++){
                                    
                                    Population children = matingPool.crossover(j, (j+1)%sizes[i]);

                                    if(children.isConsistent()){

                                        printout.append("\nThe child of " + matingPool.chromosomes[j] + 
                                        " and " + matingPool.chromosomes[(j+1)%sizes[i]] + " is\n");

                                        int random = obj.nextInt(children.getSize());

                                        if(todo == sizes[i])   break;

                                        Individual newChild = children.chromosomes[random];

                                        printout.append(newChild.genes);

                                        children.mutation(newChild);

                                        printout.append("\nAfter mutation:\t\t"+ newChild.genes);

                                        printout.append("\n");

                                        nextPopulation.editPopulation(todo++, newChild);
                                    }
                                    else{

                                        matingPool.RandomJerk();
                                        System.out.println(matingPool.crossoverThreshold);

                                    }

                                }

                            }
                            if(currentGen == generationCount[gen]){

                                    long end = System.currentTimeMillis();
                                    printout.append("\nFittest:  " + currentFit.genes +
                                    " fitness value=" + currentFit.getFitness() );
                                    String whiteSpace = "         ";
                                    myWriter.append("Generation=" + currentGen + whiteSpace);
                                    myWriter.append("Time taken=" + (end - start) + "ms" +whiteSpace);
                                    myWriter.append("LCS_Len=" + result + "\n");
                                start = end;

                            }

                            else{

                                    gen--;
                            }
        
                            currentPopulation = nextPopulation;
                            currentGen++;

                        }

                    }
                    printout.close();
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
