import java.util.Arrays; 
import java.math.BigInteger;
import java.io.*;

public class Individual{

    BigInteger fitness = BigInteger.ZERO;
    int[] genes;
    int length = 0;
    double crossoverProbability = 1;

    public Individual(int n){

        length = n;
        genes  = new int[n];
        crossoverProbability = Math.random();

    } 

    public Individual(int[] arr, int n){

        length = n;
        genes  = arr;
        crossoverProbability = Math.random();

    }

    public void setGenes(){

        BinarySearchTree tree = new BinarySearchTree();
        genes = tree.BuildPreOrder(genes, length);

    }

    public void setFitness(BigInteger val){

        fitness = val;

    }

    public BigInteger getFitness(){

        return fitness;

    }

    public String toString(){

        return (Arrays.toString(genes));

    }

    public Individual copy(){

        Individual I = new Individual(length);

        for(int i=0; i<length; i++){

            I.genes[i] = genes[i];

        }

        I.fitness = fitness;
        I.crossoverProbability = crossoverProbability;

        return I;

    }

    public boolean isSameAs(Individual I){

        int a = length;
        int b = I.length;

        if(a==b){

            boolean val = true;

            for(int i= 0; i<a; i++){

                if(genes[i] != I.genes[i]){

                    val = false;

                }

            }

            return val;

        }

        else{

            return false;

        }

    }

    public int printParenthesization(int i, int j, int position, FileWriter writer) 
    throws IOException{
        if(i==j){

            writer.append("M"+i);

        }

        else{

            writer.append("(");

            int k = genes[position];

            position = printParenthesization(i, k, position+1, writer);

            position = printParenthesization(k+1, j, position, writer);

            writer.append(")");

        }

        return position;

    }    

}