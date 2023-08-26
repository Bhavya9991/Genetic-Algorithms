import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter; 
import java.io.IOException;

public class Population {

    int sizeOfPopulation;
    Individual[] chromosomes;
    static double crossoverThreshold = 0.9;

    public Population(int size){

        sizeOfPopulation = size;
        chromosomes = new Individual[size];        
    }

    public Population(Population P){

        sizeOfPopulation = P.sizeOfPopulation;
        for(int i=0; i<sizeOfPopulation; i++){

            chromosomes[i] = new Individual(P.chromosomes[i]);
        }
        
    }

    public void init(int len){

        for(int i = 0; i<sizeOfPopulation; i++){

            chromosomes[i] = new Individual(len);

        }

    }

    public Population clone(){

        return new Population(this);

    }

    public void init(Individual[] individuals){

        sizeOfPopulation = individuals.length;
        chromosomes = new Individual[individuals.length];
        for(int i=0; i<individuals.length; i++){

            chromosomes[i] = individuals[i];
        }

    }

    public void setChromosomeAt(int index, Individual I ){

        chromosomes[index] = I;

    }

    public Individual getChromosomeAt(int index){

        return chromosomes[index];
    }

    public int getSize(){

        return sizeOfPopulation;

    }

    public int findWeakest(){   

        long min = Long.MAX_VALUE;
        int index = -1;

        for(int i=0; i<sizeOfPopulation; i++){

            if(min > chromosomes[i].getFitness() && chromosomes[i].isValid()){
                min = chromosomes[i].getFitness();
                index = i;

            }
        }
        if(index == -1){
            index = 0;  
        }
        return index;
    }

    public void replaceWeakest(Individual I){

        int index = findWeakest();
        chromosomes[index] = I;

    }

    public int findStrongestIndex(){
      
        long max = -100;
        int index = -1; 
        for(int i=0; i<sizeOfPopulation; i++){

            if(max < chromosomes[i].getFitness() && chromosomes[i].isValid()){
                max = chromosomes[i].getFitness();
                index = i;

            }
        }
        
        return index;
    }

    public Individual findStrongest(){

        int index = findStrongestIndex();

        if(index != -1){
        return chromosomes[index];
        }
        else{
        return chromosomes[0];
        }
    }

    public Individual[] findAllStrongest(){

        int index = findStrongestIndex();
        long maxFitness = chromosomes[index].getFitness();
        int count = 0;

        for(int i = 0; i< sizeOfPopulation; i++){

            if(chromosomes[i].getFitness() == maxFitness){

                count++;
            }

        }

        Individual[] fittest = new Individual[count];

        for(int i=0; i<sizeOfPopulation; i++){

            if(chromosomes[i].getFitness() == maxFitness){

                fittest[--count] = chromosomes[i];
            }
        }
        return fittest;
    }

    public Individual[] selection(){    //Roulette-wheel selection

        long prefixSum[] = new long[sizeOfPopulation];
        int startingPoint = 0;
        prefixSum[0] = chromosomes[startingPoint].getFitness();

        for(int i=1; i<sizeOfPopulation; i++){

            int next = (startingPoint + i) % sizeOfPopulation; 
            prefixSum[i] = prefixSum[i-1] + chromosomes[next].getFitness();

        }
        long cumulativeSum = prefixSum[sizeOfPopulation-1];

        Random obj = new Random();
        int randomIndex = -1;
        long randomPoint;
        Individual[] selected = new Individual[sizeOfPopulation];

        for(int j = 0; j< sizeOfPopulation; j++){

            randomPoint = (Math.abs(obj.nextLong()) + 1) % (cumulativeSum+1) ;

            for(int i = 0; i<sizeOfPopulation; i++){

                if(randomPoint <= prefixSum[i]){

                    randomIndex = i;
                    break;

                }

            }

            if(randomIndex == -1){

                randomIndex = sizeOfPopulation - 1;

            }

            randomIndex = (randomIndex + startingPoint) % sizeOfPopulation;
            selected[j] = chromosomes[randomIndex];
        }
        return selected;
    }

    public static Individual crossover(Individual parent1, Individual parent2){    

        int len = parent1.getLength();
        char bits[] = new char[len];
        Random obj = new Random();

        for(int i=0; i<len; i++){

            int choice = obj.nextInt(2);

            switch(choice){

                case 0:   bits[i] = parent1.getAllele(i);    break;
            
                case 1:   bits[i] = parent2.getAllele(i);    break;                

            }

        }
        return new Individual(new String(bits));
    }

    public void mutation(){     

        for(int i = 0; i <sizeOfPopulation; i++){

            double shouldMutate = Math.random();

            if(shouldMutate < 0.1){

                chromosomes[i].modifyBit();
            
            }
        }
    }

    public static boolean mutation(Individual I){   //returns true if mutation happens, else false.

        double shouldMutate = Math.random();
        boolean mutated = false;
        if(shouldMutate < 0.25){

            I.modifyBit();
            mutated = true;
            
        }
        return mutated;
    }

    public void displayPopulation(){

        for(int i=0; i<sizeOfPopulation; i++){

            System.out.print(chromosomes[i].genes + "      Fitness= ");
            System.out.print(chromosomes[i].getFitness() + "\n");

        }
    }

    public void printPopulation(FileWriter writer)
    throws IOException{

        for(int i=0; i<sizeOfPopulation; i++){

                chromosomes[i].printPhenotypes(writer);

        }

    }

}
