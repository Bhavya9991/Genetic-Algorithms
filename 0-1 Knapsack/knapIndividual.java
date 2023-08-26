import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter; 
import java.io.IOException;

public class Individual{

    int length;
    StringBuilder genes;
    long fitness;
    boolean valid;
    double crossoverProbability;

    public Individual(){

        length = -1;
        fitness = 0;
        crossoverProbability = 0;
        valid = false;
        genes = new StringBuilder("");
    }

    public Individual(int len){

        length = len;
        genes = new StringBuilder(randomBinaryString(len));
        fitness = 0;
        valid = false;
        crossoverProbability = Math.random();

    }

    public Individual(String binarystr){

        length = binarystr.length();
        genes = new StringBuilder(binarystr);
        fitness = 0;
        valid = false;
        crossoverProbability = Math.random();        
    }

    public Individual(Individual I){

        length = I.length;
        genes = new StringBuilder(I.genes);
        fitness = I.fitness;
        valid = I.valid;
        crossoverProbability = I.crossoverProbability;
    }

    public Individual clone(){

        return new Individual(this);
    }

    public void setFitness(long n){

        if(n > 0){

            fitness = n*10;
            valid = true;

        }
        else{

            fitness = 0;
            valid = false;

        }
    }

    public long getFitness(){

        return fitness;
    }

    public int getLength(){

        return length;
    }

    public char getAllele(int index){

        return genes.charAt(index);
    }

    public boolean isValid(){

        return valid;
    }

    public String randomBinaryString(int len){

        char[] arr = new char[len];
        char zero = '0';
        double bias = Math.random();
        double chances = 0.97;

        for(int i=0; i<len; i++){

            int tmp = 1;
            
            if(bias < chances){
                tmp = 0;
                chances -= 0.00002;
                if(chances < 0.94){
                    chances = 0.97;
                }
            }
            else{
                chances += 0.0001;
                if(chances > 0.9999){
                    chances = 0.97;
                }
            }

            arr[i] = (char)(tmp + zero);    
        }

        return new String(arr);
    }

    public int[] decode(){

        int[] set;
        int count = 0;
        for(int i=0; i<length; i++){

            if(genes.charAt(i) == '1'){

                count ++;
            }
        }
        set = new int[count];
        int tmpIndex = 0;

        for(int i=0; i<length; i++){

            if(genes.charAt(i) == '1'){

                set[tmpIndex++] = i+1;

            }
        }
        return set; 
    }

    public void modifyBit(){

        Random obj = new Random();
        int index = obj.nextInt(length);
        char bit = genes.charAt(index);
        char smart = '1';
    
        double zeroness = Math.random();

        if(zeroness < 0.6){
            smart = '0';
        }

        switch (bit){

            case '0':   genes.setCharAt(index, smart);    break;
            
            case '1':   genes.setCharAt(index, smart);    break;

        }
    }

    public void displayGenes(){

        System.out.println(genes);

    }

    public void printPhenotypes(FileWriter writer)
    throws IOException{

        writer.append("\nItems set = " + Arrays.toString(decode()) +
        "   Fitness=" + getFitness() + '\n');

    }
    static double chances = 0.97;
}