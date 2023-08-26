
public class Individual{

    int fitness;
    boolean valid;
    StringBuilder genes;
    double crossoverProbability;

    public Individual(){

        fitness = -10;
        valid = false;
        crossoverProbability = 1;
    }

    public Individual(String str){

        fitness = 0;
        valid = false;
        genes = new StringBuilder(str);
        crossoverProbability = Math.random();

    }

    public int getFitness(){

        return fitness;

    }

    public void setFitness(){

        if(valid){

            fitness = genes.length();

        }
        else{

            fitness = 0;

        }

    }

    public char getAllele(int index){

        return genes.charAt(index);

    }

    public void setAllele(int index, char ch){

        genes.setCharAt(index, ch);

    }

    public int get_length(){

        return genes.length();  //It is important to distinguish between fitness and length
                                // of an individual.

    }

    public boolean getValidity(){

        return valid;

    }

    public void setValidity(boolean bool){

        valid = bool;
    }

    public String toString(){

        return genes.toString();

    }

    public void displayGenes(){

        System.out.println(genes);

    }
    
    //set functions are more interesting than get functions.
}