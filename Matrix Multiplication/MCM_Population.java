import java.math.BigInteger;
import java.util.Random;
import java.io.*;

public class Population {

    int sizeOfPopulation = 0;
    Individual[] chromosomes;

    public Population(int size){

        sizeOfPopulation = size;
        chromosomes = new Individual[size];

    }

    public void init(int n){

        for(int i=0; i<sizeOfPopulation; i++){

            chromosomes[i] = new Individual(n);

            for(int j = 0; j<n; j++){

                chromosomes[i].genes[j] = j+1;

            }

            randomShuffle(chromosomes[i]);    //Check

        }
        setChromosomes();

    }  
    
    public void setChromosomes(){

        for(int i=0; i<sizeOfPopulation; i++){

            chromosomes[i].setGenes();

        }

    }

    public void selection(Population P){

        Random obj = new Random();

        for(int i = 0; i<sizeOfPopulation; i++){

            Individual c,d;

            c = P.chromosomes[obj.nextInt(sizeOfPopulation)];
            d = P.chromosomes[obj.nextInt(sizeOfPopulation)];

            chromosomes[i] = Stronger(c,d);

        }

    }

    public static Individual crossover(Individual parent1, Individual parent2, FileWriter writer)
    throws IOException{

        Random obj = new Random();

        boolean[] hash = new boolean[parent1.length + 1];

        for(int i =0; i<parent1.length; i++){

            hash[i] = false;

        }

        Individual child = new Individual(parent1.length);

        int random = obj.nextInt(parent1.length);

        for(int i = 0; i<random; i++){

            child.genes[i] = parent1.genes[i];

            hash[parent1.genes[i]] = true;

        }

        for(int i = 0; i<parent2.length; i++){

            if(hash[parent2.genes[i]] != true){
            
                child.genes[random++] = parent2.genes[i];

                hash[parent2.genes[i]] = true;  
 
                if(random == parent2.length){
                    break;
                }
            }
        }

        Individual childCopy = child.copy();

        childCopy.setGenes();

        childCopy.printParenthesization(1, childCopy.length+1, 0, writer);

        mutate(child, writer);

        child.setGenes();

        child.printParenthesization(1, child.length+1, 0, writer);

        return child;
        
    }

    public static void mutate(Individual I, FileWriter writer)
    throws IOException{

        int n = I.length;

        double mutationThreshold = 0.3;

        Random obj = new Random();

        int a = obj.nextInt(n);
        int b = obj.nextInt(n);

        double mutateI = Math.random(); 

        if(mutateI < mutationThreshold){

            if(a==b){

                b = n-1;
            }
            
            int tmp = I.genes[a];
            I.genes[a] = I.genes[b];
            I.genes[b] = tmp;

            writer.append("\n The mutation has happened. Thus, the child is  ");

        }

        else{

            writer.append("\n The mutation has not happened. Thus the child is  ");            

        }
    }

    public Individual fittest(){

        Individual fit = chromosomes[0];

        for(int i = 1; i<sizeOfPopulation; i++){

             fit = Stronger(fit, chromosomes[i]);

        }

        return fit;

    }

    public Individual Stronger(Individual c, Individual d){

        BigInteger a = c.getFitness();
        BigInteger b = d.getFitness();

        if(a.compareTo(b) < 0){
            return c;
        }
        else {
            return d;
        }
    }

    public void randomShuffle(Individual I){    

        int[] result = I.genes;
        int n = I.length;
        Random obj = new Random();

        for(int i=n-1; i>0; i--){

            int x = obj.nextInt(i+1);

            int tmp = result[i];
            result[i] = result[x];
            result[x] = tmp;

        }

    }

    public void printPhenotypes(FileWriter writer)
    throws IOException{

        int size = sizeOfPopulation;

        for(int i = 0; i<size; i++){

            int n = chromosomes[i].length;

            chromosomes[i].printParenthesization(1, n+1, 0, writer);

            writer.append("       Fitness = "  + chromosomes[i].getFitness() + "\n");

        }

    }

    public void displayPopulation(){

        for(int i=0; i<sizeOfPopulation; i++){

            System.out.println(chromosomes[i].getFitness());

        }
    }
}
