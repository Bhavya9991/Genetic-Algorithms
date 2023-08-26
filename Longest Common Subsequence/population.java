import java.util.Random;
import java.io.FileWriter; 
import java.io.IOException;

public class Population{

    Individual[] chromosomes;
    double mutationProbability[];
    int sizeOfPopulation;
    double crossoverThreshold;
    int limit;
    boolean consistent;

    public Population(int size, int upperLimit){

        sizeOfPopulation = size;
        chromosomes = new Individual[size];
        mutationProbability = new double[size];
        crossoverThreshold = 0.8181;
        consistent = false;
        limit = upperLimit;

    }

    public void init(){

        chromosomes[0] = new Individual("A");
        Random obj = new Random();
        int random;

        for(int i=1; i<sizeOfPopulation; i++){

            random = obj.nextInt(4);
            chromosomes[i] = new Individual(myIntToString(random));

        }

        consistent = true;

    }
    
    public void init(Individual[] children){

        for(int i=0; i<sizeOfPopulation; i++){

            chromosomes[i] = children[i];

        }

        consistent = true;

    }

    public void editPopulation(int index, Individual chromos){

        chromosomes[index] = chromos;

    }

    public int getSize(){

        return sizeOfPopulation;

    }

    public boolean isConsistent(){

        return consistent;
        
    }

    public void calculateFitness(){

        for(int i=0; i<sizeOfPopulation; i++){

            chromosomes[i].setFitness();

        }

    }

    public void selection(Population P){

        int size = P.getSize();
        Random obj = new Random();
        Individual fittestGuy;
        int start = 2;

        fittestGuy = P.fittest();

        if(isNullChromosome(fittestGuy)){
            start = 0;
        }
        else{

            chromosomes[0] = fittestGuy;
            chromosomes[1] = P.randomChromosome();

        }

        for(int i = start; i<size; i++){

            int a,b;

            a = obj.nextInt(size);
            b = obj.nextInt(size);

            if(a==b){

                a = (a++) % size;
            } 

            int f1 = P.chromosomes[a].getFitness();
            int f2 = P.chromosomes[b].getFitness();

            if(f1>f2){

                chromosomes[i] = P.chromosomes[a];

            }

            else{

                chromosomes[i] = P.chromosomes[b];

            }

        }

    }


    public Population crossover(int index1, int index2){

        Random obj = new Random();

        Population candits= new Population(4, limit);
        Individual children[] = new Individual[4];

        Individual parent1 = chromosomes[index1];
        Individual parent2 = chromosomes[index2];

        if(true){   

            StringBuilder sb1 = parent1.genes;
            StringBuilder sb2 = parent2.genes;

            int sizeTooSmall = 4;   //Discuss the significance of this variable.

            if(isNullChromosome(parent1)  || isNullChromosome(parent2))
            {
                return candits; //returning an inconsistent population of null parents.
            }

            if(parent1.get_length() < sizeTooSmall || parent2.get_length() < sizeTooSmall){

                int a,b;
                a = sb1.length();
                b = sb2.length();

                children[0] = new Individual(sb1.substring(0, a) + sb2.substring(0, b));
                children[1] = new Individual(sb1.substring(0, a) + sb2.substring(0, b));
                children[2] = new Individual(sb2.substring(0, b) + sb1.substring(0, a));
                children[3] = new Individual(sb2.substring(0, b) + sb1.substring(0, a));

            }

            else{

                int random1 = obj.nextInt(sb1.length());
                int random2 = obj.nextInt(sb2.length());

                if(random1 + random2 > limit){  
                
                    int coin = obj.nextInt(limit);
                    random1 = obj.nextInt(Math.min(coin+1,sb1.length()));

                    coin = limit - coin;
                    random2 = obj.nextInt(Math.min(coin+1,sb2.length()));

                }

                children[0] = new Individual(sb1.substring(0, random1) + sb2.substring(0, random2));

                children[1] = new Individual(sb1.substring(0, random1) + 
                                             sb2.substring(sb2.length()-1 -random2));
                children[2] = new Individual(sb2.substring(0, random2) + sb1.substring(0, random1));

                children[3] = new Individual(sb2.substring(0, random2) + 
                                             sb1.substring(sb1.length()-1 - random1));
            }

            candits.init(children);

            candits.mutation();
        }

        return candits;
    }

    public void mutation(){
       
        double mutationThreshold = 0.2;

        Random obj = new Random();

        int size = getSize();

        for(int i=0; i<size; i++){

            mutationProbability[i] = Math.random();

            if(mutationProbability[i] <= mutationThreshold && !isNullChromosome(chromosomes[i])){

                int random = obj.nextInt(chromosomes[i].get_length());

                char inp = chromosomes[i].getAllele(random);

                switch(inp){

                    case 'A':
                        chromosomes[i].setAllele(random, 'G');
                        break;
                    case 'T':
                        chromosomes[i].setAllele(random, 'A');
                        break;          
                    case 'G':
                        chromosomes[i].setAllele(random, 'C');
                        break;                                  
                    case 'C':
                        chromosomes[i].setAllele(random, 'T');
                        break;

                }

            }
            else{

                if(isNullChromosome(chromosomes[i])){

                    chromosomes[i].genes = new StringBuilder(myIntToString(obj.nextInt(4)));

                }

            }
        }

    }

    public Individual fittest(){
        int index = 0;
        int max = 0;
        int size = getSize();

        for(int i = 1; i<size; i++){

            if(chromosomes[i].getFitness() >= max){

                index = i;
                max = chromosomes[i].getFitness();

            }

        }

        if(max == 0){

            return new Individual("");

        }

        return chromosomes[index];

    }
    
    public Individual[] allFittest(int num){

        Individual[] elites = new Individual[num];

        int best = fittest().getFitness();

        for(int i = 0; i < getSize() && num > 0; i++){

            if(chromosomes[i].getFitness() == best){

                elites[--num] = chromosomes[i];

            }

        }

        return elites;
    }

    public boolean isNullChromosome(Individual I){

        if(I.get_length()==0){

            return true;

        }

        else{

            return false;

        }

    }

    public Individual randomChromosome(){

            Random obj = new Random();

            int size = getSize();

            return chromosomes[obj.nextInt(size)];

    }

    public void RandomJerk(){

        if(crossoverThreshold < 0.98){

            crossoverThreshold += 0.000001;

        }

    }

    public String myIntToString(int num){

        String result = "";

        switch(num){

            case 0:
                result = "A";
                break;

            case 1:
                result = "C";
                break;

            case 2:
                result = "G";
                break;
            
            case 3:
                result = "T";
                break;

        }

        return result;

    }

    public int countOfEquallyFit(int n){

        int count = 0;

        for(int i = 0; i < getSize(); i++){

            if(chromosomes[i].getFitness() == n){

                count++;
            }

        }

        return count;

    }

    public void displayPopulation(){

        for(int i=0; i<sizeOfPopulation; i++){

            System.out.print(chromosomes[i].genes + "      Fitness= ");
            System.out.print(chromosomes[i].getFitness() + "\n");

        }

    }

    public void printPhenotypes(FileWriter writer)
    throws IOException{

        for(int i=0; i<getSize(); i++){

            writer.append("\n" + chromosomes[i].genes + "   Fitness=" + chromosomes[i].getFitness() + "\n");

        }

    }

    public void chromosomesValidity(){

        for(int i=0; i<getSize(); i++){

            System.out.println(chromosomes[i] + " " + chromosomes[i].getValidity() 
                               + chromosomes[i].getFitness());

        }

    }

    public void mutation(Individual I){

        double mutationThreshold = 0.2;

        Random obj = new Random();

        double mutationProb = Math.random();

        if(mutationProb < mutationThreshold){

            int random = obj.nextInt(I.get_length());

            char inp = I.getAllele(random);

            switch(inp){

                case 'A':
                    I.setAllele(random, 'G');
                    break;
                case 'T':
                    I.setAllele(random, 'A');
                    break;          
                case 'G':
                    I.setAllele(random, 'C');
                    break;                                  
                case 'C':
                    I.setAllele(random, 'T');
                    break;

            }

        }

    }

}
